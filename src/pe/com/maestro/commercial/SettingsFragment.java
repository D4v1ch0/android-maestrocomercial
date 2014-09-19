package pe.com.maestro.commercial;

import java.util.List;

import pe.com.maestro.commercial.models.Store;
import rp3.configuration.PreferenceManager;
import rp3.runtime.Session;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;

public class SettingsFragment extends rp3.configuration.SettingsFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		String id = getArguments().getString("id");
		
        if ("connectivity".equals(id)) {
            addPreferencesFromResource(R.xml.preferences_connectivity);
        } else if ("general".equals(id)) {
            addPreferencesFromResource(R.xml.preferences_general);
            
            ListPreference listPreference = (ListPreference) findPreference("default_store_id");
            
            List<Store> stores = Store.getStores(getDataBase());
            
            String[] entries = new String[stores.size()-1];
            String[] values = new String[stores.size()-1];
            
            int i = 0;
            for(Store s: stores){
            	entries[i] = s.getName();
            	values[i] = s.getStoreId();
            }
            
            listPreference.setEntries(entries);
            listPreference.setEntryValues(values);
            
            closeDataBase();
        }
	}
	
	@Override
	public void onPreferenceChanged(Preference pref) {		
		super.onPreferenceChanged(pref);
		
		if(pref.getKey().equals("server")){
			if(Session.IsLogged()){
				Session.logOut();				
				startActivity( new Intent(this.getActivity(), StartActivity.class));
				getActivity().finish();
			}
			pref.setSummary(PreferenceManager.getString(pref.getKey()));
		}
		
	}	
	
}
