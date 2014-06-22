package pe.com.maestro.commercial.sync;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.db.Contract;
import pe.com.maestro.commercial.models.FinancialProduct;
import pe.com.maestro.commercial.models.Alert;
import pe.com.maestro.commercial.models.Client;
import pe.com.maestro.commercial.models.ClientFinancialProduct;
import pe.com.maestro.commercial.models.GeoDivision;
import pe.com.maestro.commercial.models.GuideElement;
import pe.com.maestro.commercial.models.GuideSection;
import pe.com.maestro.commercial.models.Store;
import pe.com.maestro.commercial.models.StoreSection;

import android.content.ContentValues;
import android.util.Log;
import rp3.connection.WebService;
import rp3.data.models.GeneralValue;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import rp3.util.BitmapUtils;
import rp3.util.Convert;
import rp3.util.FileUtils;

public class Catalogs {

	public static int executeSync(DataBase db, int syncType) throws FileNotFoundException{
		return executeSync(db, syncType, 0);
	}
	
	public static int executeSync(DataBase db, int syncType, int elementGuideId)
			throws FileNotFoundException {
		//android.os.Debug.waitForDebugger();

		WebService service = new WebService();

		service.setConfigurationName("soap_maestro", "synchronize");

		ContentValues values = new ContentValues();
		values.put("PCName", Session.getDeviceId());
		values.put("IdProcess", 0);
		values.put("IdEvent", 7);
		values.put("SynchronizeImage", "True");
		values.put("SynchronizeType", syncType);
		
		if(syncType == 3)
			values.put("IdElement", elementGuideId);

		SoapObject param = WebServiceUtils.getMainParam(values);
		service.addParameter("XmlString", param);

		switch (syncType) {
		case 1:
			Store.deleteAll(db, Contract.Store.TABLE_NAME, true);
			StoreSection.deleteAll(db, Contract.StoreSection.TABLE_NAME, true);

			Client.deleteAll(db, Contract.Client.TABLE_NAME, true);
			Alert.deleteAll(db, Contract.Alert.TABLE_NAME, true);
			ClientFinancialProduct.deleteAll(db,
					Contract.ClientFinancialProduct.TABLE_NAME, true);

			GeneralValue.deleteAll(db,
					rp3.data.models.Contract.GeneralValue.TABLE_NAME);
			break;
		case 2:
			List<GuideSection> sections = GuideSection.getGuideSections(db,
					false);
			for (GuideSection section : sections)
				FileUtils.deleteFromInternalStorage(section.getFileName());

			GuideSection.deleteAll(db, Contract.GuideSection.TABLE_NAME);
			break;
		case 3:
			if(elementGuideId==0){			
				List<GuideElement> guideElements = GuideElement.getGuideElements(
						db, null, null);
				for (GuideElement e : guideElements) {
					FileUtils.deleteFromInternalStorage(e.getFileName());
					FileUtils.deleteFromInternalStorage(e.getDetailFileName());
				}
	
				GuideElement.deleteAll(db, Contract.GuideElement.TABLE_NAME);				
			}
			service.setUseFileResponse(true);
			break;
		case 4:
			GeoDivision.deleteAll(db, Contract.GeoDivision.TABLE_NAME, true);
			break;
		case 5:
			List<FinancialProduct> financialProducts = FinancialProduct
					.getFinancialProducts(db);
			for (FinancialProduct e : financialProducts)
				FileUtils.deleteFromInternalStorage(e.getFileName());

			FinancialProduct
					.deleteAll(db, Contract.FinancialProduct.TABLE_NAME);
			break;
		}

		try {

			try {
				service.invokeWebService();
			} catch (HttpResponseException e) {
				Log.e("Catalogs Http", e.getMessage());
				return SyncAdapter.SYNC_EVENT_CONNECTION_FAILED;
			} catch (IOException e) {
				Log.e("Catalogs IO", e.getMessage());
				return SyncAdapter.SYNC_EVENT_CONNECTION_FAILED;
			} catch (XmlPullParserException e) {
				Log.e("Catalogs Parser", e.getMessage());
				return SyncAdapter.SYNC_EVENT_ERROR;
			}

			if (service.useFileResponse()) {

				switch (syncType) {
				case 3:
					List<GuideElement> elements = GuideElementParser.parse(
							new FileInputStream(service.getFileResponse()), db, elementGuideId != 0);
					
					//GuideElement.deleteAll(db, Contract.GuideElement.TABLE_NAME);
					//Retrieve Elements by get Image
					if(elementGuideId==0){
						List<Integer> ids = new ArrayList<Integer>();
						for(GuideElement element: elements){
							if(!ids.contains(element.getGuideElementId()))
								ids.add(element.getGuideElementId());
						}
						
						for(int element: ids){
							executeSync(db, syncType, element);						
						}
					}
					
					break;
				}

			} else {

				SoapObject response = service.getSoapObjectResponse();

				SoapObject root = (SoapObject) response.getProperty(0);

				for (int i = 0; i < root.getPropertyCount(); i++) {

					SoapObject property = (SoapObject) root.getProperty(i);

					PropertyInfo pi = new PropertyInfo();
					root.getPropertyInfo(i, pi);
					String element = pi.getName();

					switch (syncType) {
					case 1:
						if (element.equals(WebServiceUtils.ARGUMENT_NAME)) {
							// storeDefaultId =
							// property.getAttributeAsString("IdStoreDefault");
							// geoPoliticaDefaultId =
							// property.getAttributeAsString("IdGeoPoliticaDefault");

							// String prefStoreEx =
							// PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID);
							// if(TextUtils.isEmpty(prefStoreEx))
							// PreferenceManager.setValue(Constants.PREF_DEFAULT_STORE_ID,
							// storeDefaultId);

							// PreferenceManager.setValue(Constants.PREF_DEFAULT_GEOPOLITICAL_ID,
							// Integer.valueOf(geoPoliticaDefaultId));

						} else if (element.equals("Rp3Store")) {
							Store store = new Store();
							store.setStoreId(property
									.getPropertyAsString("IdStore"));
							store.setName(property.getPropertyAsString("Store"));

							Store.insert(db, store);
						} else if (element.equals("RP3StoreSection")) {
							StoreSection section = new StoreSection();
							section.setName(property.getPropertyAsString("Ds"));
							section.setStoreSectionId(Integer.valueOf(property
									.getPropertyAsString("Id")));

							StoreSection.insert(db, section);
						} else if (element.equals("RP3AddressStructure")) {
							GeneralValue gv = new GeneralValue();
							gv.setGeneralTableId(Long.valueOf(property
									.getPrimitivePropertyAsString("Type")));
							gv.setCode(property
									.getPrimitivePropertyAsString("Id"));
							gv.setValue(property
									.getPrimitivePropertyAsString("Ds"));

							GeneralValue.insert(db, gv);
						} else if (element.equals("RP3StepCustomerAttention")) {
							GeneralValue gv = new GeneralValue();
							gv.setGeneralTableId(Constants.GENERAL_TABLE_STEP_CUSTOMER_ATTENTION);
							gv.setCode(property
									.getPrimitivePropertyAsString("Id"));
							gv.setValue(property
									.getPrimitivePropertyAsString("Ds"));
							gv.setOrder(Convert.getInt(property
									.getPrimitivePropertyAsString("Id")));

							GeneralValue.insert(db, gv);
						} else if (element.equals("RP3Parameter")) {
							GeneralValue gv = new GeneralValue();
							gv.setGeneralTableId(Constants.GENERAL_TABLE_PARAMETER);
							gv.setCode(property
									.getPrimitivePropertyAsString("Id"));
							gv.setValue(property
									.getPrimitivePropertyAsString("Ds"));

							GeneralValue.insert(db, gv);
						}
						break;

					case 2:
						if (element.equals("RP3GuideSection")) {
							GuideSection section = new GuideSection();
							section.setName(property.getPropertyAsString("Ds"));
							section.setID(Long.valueOf(property
									.getPropertyAsString("Id")));

							GuideSection.insert(db, section);

							FileUtils.saveInternalStorage(
									section.getFileName(),
									BitmapUtils.decodeBitmapFromBase64(property
											.getPropertyAsString("Img")));
						}
						break;
					case 3:

						// if(element.equals("RP3ElementsByGuideSection")){
						// GuideElement guideElement = new GuideElement();
						// guideElement.setName(property.getPropertyAsString("Ds"));
						// guideElement.setGuideElementId(Integer.valueOf(property.getPropertyAsString("Id")));
						// guideElement.setGuideSectionId(Long.valueOf(property.getPropertyAsString("IdSec")));
						// guideElement.setSecuencial(Integer.valueOf(property.getPropertyAsString("Sec")));
						// guideElement.setHasDetailInfo(
						// Convert.getBoolean(property.getPropertyAsString("ImgInfoExists"))
						// );
						//
						// GuideElement.insert(db, guideElement);
						//
						// FileUtils.saveInternalStorage(guideElement.getFileName(),
						// BitmapUtils.decodeBitmapFromBase64(property.getPropertyAsString("Img")));
						//
						// if(guideElement.hasDetailInfo())
						// FileUtils.saveInternalStorage(guideElement.getDetailFileName(),
						// BitmapUtils.decodeBitmapFromBase64(property.getPropertyAsString("ImgInfo")));
						//
						//
						// }
						break;
					case 4:
						if (element.equals("RP3GeoDivision")) {
							GeoDivision geo = new GeoDivision();
							geo.setID(Integer.valueOf(property
									.getPropertyAsString("IdGeoPolitica")));
							geo.setDepartmentId(Integer.valueOf(property
									.getPropertyAsString("IdDepartamento")));
							geo.setDepartmentName(property
									.getPropertyAsString("Departamento"));
							geo.setDistrictId(Integer.valueOf(property
									.getPropertyAsString("IdDistrito")));
							geo.setDistrictName(property
									.getPropertyAsString("Distrito"));
							geo.setProvinceId(Integer.valueOf(property
									.getPropertyAsString("IdProvincia")));
							geo.setProvinceName(property
									.getPropertyAsString("Provincia"));

							GeoDivision.insert(db, geo);
						}
						break;
					case 5:
						if (element.equals("RP3Product")) {
							FinancialProduct fp = new FinancialProduct();
							fp.setID(Convert.getLong(property
									.getPrimitivePropertyAsString("Id")));
							fp.setName(property
									.getPrimitivePropertyAsString("Ds"));

							FinancialProduct.insert(db, fp);

							FileUtils.saveInternalStorage(fp.getFileName(),
									BitmapUtils.decodeBitmapFromBase64(property
											.getPropertyAsString("Img")));
						}
						break;
					}

				}

			}
		} finally {
			service.close();
		}

		return SyncAdapter.SYNC_EVENT_SUCCESS;
	}

}
