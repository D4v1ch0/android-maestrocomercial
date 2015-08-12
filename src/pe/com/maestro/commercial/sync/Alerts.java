package pe.com.maestro.commercial.sync;

import java.io.IOException;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpResponseException;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.db.Contract;
import pe.com.maestro.commercial.models.Alert;
import pe.com.maestro.commercial.models.Client;
import pe.com.maestro.commercial.models.ClientFinancialProduct;
import rp3.configuration.PreferenceManager;
import rp3.connection.WebService;
import rp3.data.models.GeneralValue;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import rp3.util.Convert;
import android.content.ContentValues;

public abstract class Alerts {
	
	public static final String ARG_CLIENT_DOCUMENT_NUMBER = "client_document_Number";
	
	public static int executeSync(DataBase db, String clientDocumentNumber, String derivationCode) {
		//android.os.Debug.waitForDebugger();
		WebService service = new WebService();
		service.setConfigurationName("soap_maestro", "GetAlerts");
		
		ContentValues values = new ContentValues();
		values.put("PCName", Session.getDeviceId());
		values.put("IdProcess", 0);
		values.put("IdEvent", 11);
		values.put("IdCashier", Session.getUser().getLogonName());
		values.put("DocumentNumber", clientDocumentNumber);
		values.put("IdStore", PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID));
		values.put("StoreSection", PreferenceManager.getInt(Constants.PREF_DEFAULT_STORE_SECTION_ID));
        values.put("CodigoDerivacion", derivationCode);
		
		SoapObject param = WebServiceUtils.getMainParam(values);		
		service.addParameter("XmlString", param);
		
		
		try {
			service.invokeWebService();
		} catch (HttpResponseException e) {
			return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
		} catch (IOException e) {
			return SyncAdapter.SYNC_EVENT_CONNECTION_FAILED;
		} catch (Exception e) {
			return SyncAdapter.SYNC_EVENT_ERROR;
		}
		
		SoapObject response = service.getSoapObjectResponse();
		SoapObject root = (SoapObject)response.getProperty(0);
				
		//long oldClientId = Client.getClientID(db, clientDocumentNumber);
		//if(oldClientId!=-1){			
			//Alert.deleteClientAlerts(db, oldClientId);
			//ClientFinancialProduct.deleteClientFinancialProducts(db, oldClientId);
		//}
		GeneralValue.delete(db, Constants.GENERAL_TABLE_REPAIR_STRATEGY);
		
		Alert.deleteAll(db, Contract.Alert.TABLE_NAME);
		ClientFinancialProduct.deleteAll(db, Contract.ClientFinancialProduct.TABLE_NAME, true);
		
		Client.deleteClientByDocument(db, clientDocumentNumber);		
		Client client = new Client();		
		
		for(int i = 0; i < root.getPropertyCount(); i++){				
			
			SoapObject property = (SoapObject)root.getProperty(i);
			
			PropertyInfo pi = new PropertyInfo();
			root.getPropertyInfo(i, pi);
			String element = pi.getName();						
			
			
			if(element.equals(WebServiceUtils.ARGUMENT_NAME)){
				client = new Client();
				
				client.setLastName(property.getAttributeAsString("LastName"));
				client.setFirstName(property.getAttributeAsString("FirstName"));
				
				//if(!property.hasAttribute("Cellphone2")) {
						//TextUtils.isEmpty(client.getLastName()) && TextUtils.isEmpty(client.getFirstName())){	
					//break;
				//}
								
				client.setDocumentNumber(property.getAttributeAsString("DocumentNumber"));				
				client.setMiddleName(property.getAttributeAsString("MiddleName"));
				client.setLastName(property.getAttributeAsString("LastName"));
				client.setSecondLastName(property.getAttributeAsString("MothersMaidenName"));
				
				String birthdayString = property.getAttributeAsString("Birthday");
				
				client.setBirthdate(Convert.getDateFromString(birthdayString));
				
				client.setGeopoliticalId(Long.parseLong(property.getAttributeAsString("GeographicalDivision")));
				client.setAddress(property.getAttributeAsString("Address"));
				client.setCellphone(property.getAttributeAsString("Cellphone"));
				client.setPhoneNumber(property.getAttributeAsString("Phone"));
				client.setEmail(property.getAttributeAsString("Email"));
				client.setHasFinancialProduct(property.getAttributeAsString("OwnPresta").equals("true")?true:false);
				client.setFinancialProductMessage(property.getAttributeAsString("OwnPrestaMessage"));
				client.setHasFinancialProductAlert(property.getAttributeAsString("Rp3FinancialProductsExist").equals("true")?true:false);
				client.setFinancialProductAlertMessage(property.getAttributeAsString("ProductoAOfrecer"));	
				client.setValidityFinancialProductDate(Convert.getDateFromString(property.getAttributeAsString("VigenciaHasta")));
				client.setAmountFinancialProduct(Convert.getDouble(property.getAttributeAsString("LineaAprobada")));
				client.setInternalProcessId(Integer.valueOf(property.getAttributeAsString("IdProcess")));
				
				client.setCellphone2( property.getAttributeAsString("Cellphone2"));
				client.setPhoneNumber2( property.getAttributeAsString("Phone2"));
				client.setRoadTypeId( property.getAttributeAsString("TipoViaId"));
				client.setRoadTypeName( property.getAttributeAsString("TipoViaDs"));
				client.setZoneTypeId( property.getAttributeAsString("TipoZonaId"));
				client.setZoneTypeName( property.getAttributeAsString("TipoZonaDs"));
				client.setLivingSituation( property.getAttributeAsString("SituacionVivienda"));
				client.setProfileId( property.getAttributeAsString("Perfil"));
				client.setAddressSegment1( property.getAttributeAsString("Segmento1"));
				client.setAddressSegment2( property.getAttributeAsString("Segmento2"));
				client.setAddressSegment3( property.getAttributeAsString("Segmento3"));
				client.setAddressSegment4( property.getAttributeAsString("Segmento4"));
				client.setAddressSegment5( property.getAttributeAsString("Segmento5"));
				client.setAddressSegment6( property.getAttributeAsString("Segmento6"));
				client.setAddressSegment7( property.getAttributeAsString("Segmento7"));
				client.setAddressSegment8( property.getAttributeAsString("Segmento8"));
				client.setAddressSegment9( property.getAttributeAsString("Segmento9"));
				client.setAddressReference( property.getAttributeAsString("Referencia"));
				client.setApplyRepairStrategy(property.getAttributeAsString("Rp3RepairStrategyExistsDetail").equals("true")?true:false);
				client.setRepairStrategyType( Convert.getInt(property.getAttributeAsString("Rp3RepairStrategyCouponType")) );				
						
				Client.insert(db, client);
				
			}else if(element.equals("RP3Alert")){
				Alert alert = new Alert();
				alert.setID(Long.parseLong(property.getPropertyAsString("IdMessage")));
				alert.setMessage(property.getPropertyAsString("Message"));
				alert.setClientId(client.getID());
												
				Alert.insert(db, alert);
			}else if(element.equals("RP3FinancialProduct")){
				ClientFinancialProduct product = new ClientFinancialProduct();				
				product.setInternalMessageId(Convert.getInt(property.getPropertyAsString("IdMessage")));
				product.setFinancialProductId(Convert.getLong(property.getPropertyAsString("IdProduct")));				
				product.setClientId(client.getID());
				product.setIsSelected(false);
				ClientFinancialProduct.insert(db, product);
			}else if(element.equals("RP3RepairStrategy")){
				GeneralValue gv = new GeneralValue();
				gv.setCode(property.getPropertyAsString("Id"));
				gv.setValue(property.getPropertyAsString("Ds"));
				gv.setGeneralTableId(Constants.GENERAL_TABLE_REPAIR_STRATEGY);
				GeneralValue.insert(db, gv);
			}					
			
		}
		
		return SyncAdapter.SYNC_EVENT_SUCCESS;	
	}
	
}