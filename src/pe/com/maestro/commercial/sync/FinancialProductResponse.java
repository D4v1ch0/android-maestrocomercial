package pe.com.maestro.commercial.sync;

import java.io.IOException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpResponseException;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.models.Client;
import pe.com.maestro.commercial.models.ClientFinancialProduct;
import rp3.configuration.PreferenceManager;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import android.content.ContentValues;
import android.os.Bundle;

public abstract class FinancialProductResponse {

	public static final String ARG_CLIENT_ID = "client_id";
	
	public static int executeSync(DataBase db, long clientId, Bundle dataOut){
		Client client = Client.getClient(db, clientId, false, true);
		
		WebService service = new WebService();
		service.setConfigurationName("soap_maestro", "FinancialProductSelected");
		
		ContentValues values = new ContentValues();
		values.put("PCName", Session.getDeviceId());
		values.put("IdProcess", client.getInternalProcessId());
		values.put("IdEvent", 13);
		values.put("IdCashier", Session.getUser().getLogonName());
		values.put("DocumentNumber", client.getDocumentNumber());
		values.put("IdStore", PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID));
		values.put("StoreSection", PreferenceManager.getInt(Constants.PREF_DEFAULT_STORE_SECTION_ID));		
		
		ClientFinancialProduct fp = client.getClientFinancialProductSelected();
		
		values.put("FinancialProductSelected", fp != null);
		if(fp!=null){
			values.put("FinancialProductIdProduct", fp.getFinancialProductId() );
			values.put("FinancialProductIdMessage", fp.getFinancialProductId() );
		}
		
		values.put("ReasonOfNoInterest", client.getFinancialProductReasonOfNotInterest());
		
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
