package pe.com.maestro.commercial;

import rp3.configuration.PreferenceManager;
import android.preference.Preference;

public class SettingsActivity extends rp3.configuration.SettingsActivity {
	
	
	@Override
	public void onPreferenceChanged(Preference pref) {		
		super.onPreferenceChanged(pref);
		
		//pref.setSummary(PreferenceManager.getString(pref.getKey()));
	}	

}
