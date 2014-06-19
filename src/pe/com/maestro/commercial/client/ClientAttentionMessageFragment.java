package pe.com.maestro.commercial.client;


import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.R;
import android.os.Bundle;
import android.view.View;
import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;

public class ClientAttentionMessageFragment extends BaseFragment {

	public static ClientAttentionMessageFragment newInstance(){
		ClientAttentionMessageFragment f = new ClientAttentionMessageFragment();
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_client_messages_attention);
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
						
		setViewAdapter(R.id.listView_client_attention, new SimpleGeneralValueAdapter(this.getContext(), 
				GeneralValue.getGeneralValues(getDataBase(), Constants.GENERAL_TABLE_STEP_CUSTOMER_ATTENTION, true)));
	}
	
}
