package pe.com.maestro.commercial.sync;

import org.ksoap2.serialization.SoapObject;

import android.content.ContentValues;

public class WebServiceUtils {

	public static final String NAMESPACE = "http://tempuri.org/";
	public static final String ARGUMENT_NAME = "Rp3IdentificationSequence";
	
	public static SoapObject getIdentificationSequence(ContentValues attrs){
		SoapObject xml = new SoapObject(null, ARGUMENT_NAME);
		
		for(String key : attrs.keySet()){
			xml.addAttribute(key, attrs.get(key));
		}
		return xml;
	}
	
	public static SoapObject getMainParam(ContentValues values){		
		SoapObject xml = new SoapObject();
		SoapObject crm = new SoapObject(null,"RP3CRM");
		SoapObject iden = getIdentificationSequence(values);
		
		crm.addSoapObject(iden);			
		xml.addSoapObject(crm);
		return xml;
	}
	
	
}
