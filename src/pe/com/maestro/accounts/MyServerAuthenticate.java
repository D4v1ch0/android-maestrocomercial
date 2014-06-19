package pe.com.maestro.accounts;

import org.ksoap2.serialization.SoapObject;
import android.accounts.AccountManager;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.sync.WebServiceUtils;

import rp3.accounts.ServerAuthenticate;
import rp3.accounts.User;
import rp3.configuration.PreferenceManager;
import rp3.connection.WebService;
import rp3.runtime.Session;

public class MyServerAuthenticate implements ServerAuthenticate {
	
	public Bundle signUp(final String name, final String email, final String pass, String authType){
		return null;
	}
	
    public Bundle signIn(final String user, final String pass, String authType) {
    	Bundle bundle = new Bundle();
    	
    	WebService method = new WebService();
    	method.setConfigurationName("soap_maestro", "login");
    	
    	ContentValues values = new ContentValues();
    	values.put("IdCashier", user);
    	values.put("Password", pass);
    	values.put("IdStore", "H31");
    	values.put("PCName", Session.getDeviceId());
    	values.put("IdProcess", 0);
    	values.put("IdEvent", 9);
    	
		method.addParameter("XmlString",  WebServiceUtils.getMainParam(values));
    	
    	try {
			method.invokeWebService();
			
			SoapObject response = method.getSoapObjectResponse();
	    	
	    	SoapObject r = (SoapObject)((SoapObject) response.getProperty(0)).getProperty(WebServiceUtils.ARGUMENT_NAME);
	    	
	    	String success = r.getAttributeAsString("ErrorId");
	    	
	    	if(TextUtils.isEmpty(PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID))){	    			
	    		PreferenceManager.setValue(Constants.PREF_DEFAULT_STORE_ID, r.getAttributeAsString("IdStoreDefault"));
	    	}
	    	
	    	PreferenceManager.setValue(Constants.PREF_DEFAULT_GEOPOLITICAL_ID, Integer.valueOf(r.getAttributeAsString("IdGeoPoliticaDefault")));	    	
	    	
	    	bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, r.getAttributeAsString("ErrorMessage"));
	    	bundle.putString(AccountManager.KEY_AUTHTOKEN, success.equals("0") ? "OK" : "");
	    	bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, success.equals("0"));	    	
	    	
		} catch (Exception e) {
			String error = e.getMessage();
			if(TextUtils.isEmpty(error))
				error = Session.getContext().getText(R.string.message_error_general).toString();
			
			bundle.putBoolean(ServerAuthenticate.KEY_HAS_ERROR, true);
			bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, error);
		}
    	return bundle;	       	    	
    }

	@Override
	public boolean requestSignIn() {		
		Bundle data = signIn(Session.getUser().getLogonName(), Session.getUser().getPassword(), User.DEFAULT_TOKEN_TYPE);
		String token = data.getString(AccountManager.KEY_AUTHTOKEN);
		return token ==null || token.length() == 0;
	}
}
