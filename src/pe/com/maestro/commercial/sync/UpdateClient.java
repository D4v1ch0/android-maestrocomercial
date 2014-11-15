package pe.com.maestro.commercial.sync;

import java.io.IOException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpResponseException;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.models.Client;
import rp3.configuration.PreferenceManager;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import rp3.util.Format;
import android.content.ContentValues;
import android.os.Bundle;

public abstract class UpdateClient {
	
	public static final String ARG_CLIENT_ID = "client_id";
	
	public static int executeSync(DataBase db, long clientId, Bundle dataOut){
		Client client = Client.getClient(db, clientId, false, false);
		//android.os.Debug.waitForDebugger();
		WebService service = new WebService();
		service.setConfigurationName("soap_maestro", "SaveCustomerData");
		
		ContentValues values = new ContentValues();
		values.put("PCName", Session.getDeviceId());
		values.put("IdProcess", client.getInternalProcessId());
		values.put("IdEvent", 15);
		values.put("IdCashier", Session.getUser().getLogonName());
		values.put("IdStore", PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID));
		values.put("StoreSection", PreferenceManager.getInt(Constants.PREF_DEFAULT_STORE_SECTION_ID));
		
		values.put("DocumentNumber", client.getDocumentNumber());
		values.put("FirstName", client.getFirstName());
		values.put("SecondName", client.getMiddleName());
		values.put("LastName", client.getLastName());
		values.put("MothersMaidenName", client.getSecondLastName());
		values.put("Birthday", Format.getDataBaseDateString(client.getBirthdate()));
		values.put("GeographicalDivision", client.getGeopoliticalId());
		values.put("Address", client.getAddress());
		values.put("Cellphone", client.getCellphone());
		values.put("Phone", client.getPhoneNumber());
		values.put("Email", client.getEmail());			   
				
		values.put("Cellphone2", client.getCellphone2());
		values.put("Phone2", client.getPhoneNumber2());
		values.put("TipoViaId", client.getRoadTypeId());
		values.put("TipoViaDs", client.getRoadTypeName());
		values.put("TipoZonaId", client.getZoneTypeId());
		values.put("TipoZonaDs", client.getZoneTypeName());
		values.put("SituacionVivienda", client.getLivingSituation());
		values.put("Perfil", client.getProfileId());
		values.put("Segmento1", client.getAddressSegment1());
		values.put("Segmento2", client.getAddressSegment2());
		values.put("Segmento3", client.getAddressSegment3());
		values.put("Segmento4", client.getAddressSegment4());
		values.put("Segmento5", client.getAddressSegment5());
		values.put("Segmento6", client.getAddressSegment6());
		values.put("Segmento7", client.getAddressSegment7());
		values.put("Segmento8", client.getAddressSegment8());
		values.put("Segmento9", client.getAddressSegment9());
		values.put("Referencia", client.getAddressReference());
		
		values.put("Rp3RepairStrategyCouponType", client.getRepairStrategyType());
		values.put("Rp3RepairStrategyCouponId", client.getRepairStrategyId());
		
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
		SoapObject element = (SoapObject) root.getProperty(WebServiceUtils.ARGUMENT_NAME);
		
		dataOut.putInt("ErrorId", Integer.valueOf(element.getAttributeAsString("ErrorId")));
		dataOut.putString("ErrorMessage", element.getAttributeAsString("ErrorMessage"));
		
		return SyncAdapter.SYNC_EVENT_SUCCESS;	
	}
	
}
