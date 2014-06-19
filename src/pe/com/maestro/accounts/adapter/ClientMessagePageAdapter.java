package pe.com.maestro.accounts.adapter;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.client.ClientAttentionMessageFragment;
import pe.com.maestro.commercial.client.ClientMessageAlertsFragment;
import pe.com.maestro.commercial.client.ClientMessageFinancialProductFragment;
import pe.com.maestro.commercial.models.Client;
import rp3.data.Dictionary;
import rp3.data.models.GeneralValue;
import rp3.db.sqlite.DataBase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ClientMessagePageAdapter extends FragmentPagerAdapter{
		
	private Client client;
	
	Dictionary<String, String> parameters;
	public ClientMessagePageAdapter(FragmentManager fm, DataBase db, Client client) {
		super(fm);		
		parameters = GeneralValue.getDictionary(db, Constants.GENERAL_TABLE_PARAMETER);
		this.client = client;
	}

	@Override
	public CharSequence getPageTitle(int position) {		
		switch (position) {
		case 0:
			return parameters.get("2"); 		
		case 1:
			return parameters.get("5"); 	
		default:
			return parameters.get("1");
		}
	}
	
	@Override
	public Fragment getItem(int pos) {
		switch (pos) {
		case 0:
			return ClientMessageFinancialProductFragment.newInstance(client.getID(), client.hasFinancialProductAlert());			
		case 1:
			return ClientMessageAlertsFragment.newInstance(client.getID());		
		default:
			return ClientAttentionMessageFragment.newInstance();
		}
	}

	@Override
	public int getCount() {		
		return 3;
	}

}
