package pe.com.maestro.commercial.client;

import pe.com.maestro.accounts.adapter.AlertAdapter;
import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.Client;
import android.os.Bundle;
import android.view.View;
import rp3.app.BaseFragment;
import rp3.data.Dictionary;
import rp3.data.models.GeneralValue;

public class ClientMessageAlertsFragment extends BaseFragment {

	public static final String ARG_CLIENT_ID = "client_id";
	
	public static ClientMessageAlertsFragment newInstance(long clientId){
		ClientMessageAlertsFragment f = new ClientMessageAlertsFragment();
		Bundle b = new Bundle();
		b.putLong(ARG_CLIENT_ID, clientId);
		f.setArguments(b);
		return f;		
	}
	
	private long clientId;
	private Client client;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_client_messages_alerts);
		clientId = getArguments().getLong(ARG_CLIENT_ID);
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		client = Client.getClient(getDataBase(), clientId, true, false);
				
		Dictionary<String, String> parameters = GeneralValue.getDictionary(getDataBase(), Constants.GENERAL_TABLE_PARAMETER);		
				
		setTextViewText(R.id.textView_noresults_message, parameters.get("6"));
		
		setViewAdapter(R.id.listView_alerts, new AlertAdapter(this.getContext(), client.getAlerts()));
		
		if(client.getAlerts().size()>0){
			setViewVisibility(R.id.textView_noresults_message, View.GONE);
		}else{
			setViewVisibility(R.id.textView_noresults_message, View.VISIBLE);
		}
	}
	
}
