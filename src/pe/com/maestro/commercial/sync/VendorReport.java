package pe.com.maestro.commercial.sync;

import java.io.IOException;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpResponseException;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.db.Contract;
import rp3.configuration.PreferenceManager;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import android.content.ContentValues;

public class VendorReport {

	public static final String ARG_YEAR = "year";
	public static final String ARG_MONTH = "month";	
	public static final String ARG_GOAL_TYPE = "goalType";

	public static int executeSync(DataBase db, int year, int month, int goalType) {

		WebService service = new WebService();
		service.setConfigurationName("soap_maestro", "VendorReport");

		ContentValues values = new ContentValues();
		values.put("PCName", Session.getDeviceId());
		values.put("IdProcess", 0);
		values.put("IdEvent", 17);
		values.put("IdCashier", Session.getUser().getLogonName());
		
		values.put("IdStore", PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID));
		
		values.put("ReportYear", year);
		values.put("ReportMonth", month);
		//values.put("ReportViewType", recordMode);
		values.put("ReportGoalType", goalType);		
		
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
		SoapObject root = (SoapObject) response.getProperty(0);
		
		pe.com.maestro.commercial.models.VendorReport.deleteAll(db,  Contract.VendorReport.TABLE_NAME, true);
		
		for(int i = 0; i < root.getPropertyCount(); i++){				
			
			SoapObject property = (SoapObject)root.getProperty(i);
			
			PropertyInfo pi = new PropertyInfo();
			root.getPropertyInfo(i, pi);
			String element = pi.getName();			
			
			if(element.equals(WebServiceUtils.ARGUMENT_NAME)){
				
			}else if(element.equals("RP3Report")){
				pe.com.maestro.commercial.models.VendorReport r = new pe.com.maestro.commercial.models.VendorReport();
				
				r.setRecordMode( Integer.valueOf(property.getPropertyAsString("Vista")) );
				
				if(r.getRecordMode() == pe.com.maestro.commercial.models.VendorReport.RECORD_MODE_DAILY){
					r.setDay( Integer.valueOf(property.getPropertyAsString("Grupo")) );
					r.setMonth( month );
				}else{
					r.setMonth( Integer.valueOf(property.getPropertyAsString("Grupo")) );
					r.setDay( 1 );
				}
				r.setYear(year);
				
				r.setUser( property.getPropertyAsString("Usuario") );
				r.setName( property.getPropertyAsString("Nombre") );
				r.setGoalValue( Double.valueOf(property.getPropertyAsString("Meta")) );
				r.setRealValue( Double.valueOf(property.getPropertyAsString("Real")) );
				r.setPercent( Double.valueOf(property.getPropertyAsString("Cumplimiento")) );
				r.setGoalType( goalType );								
				
				pe.com.maestro.commercial.models.VendorReport.insert(db, r);
			}
		}
		return SyncAdapter.SYNC_EVENT_SUCCESS;
	}
}
