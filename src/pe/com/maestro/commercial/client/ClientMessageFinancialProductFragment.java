package pe.com.maestro.commercial.client;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import rp3.app.BaseFragment;
import rp3.data.Dictionary;
import rp3.data.models.GeneralValue;

public class ClientMessageFinancialProductFragment extends BaseFragment {

	public static final String ARG_CLIENT_ID = "clientId";
	public static final String ARG_HAS_PRODUCTS = "hasproducts";
	
	public static ClientMessageFinancialProductFragment newInstance(long clientId, boolean hasProducts){
		ClientMessageFinancialProductFragment f = new ClientMessageFinancialProductFragment();
		Bundle b = new Bundle();
		b.putLong(ARG_CLIENT_ID, clientId);
		b.putBoolean(ARG_HAS_PRODUCTS, hasProducts);
		f.setArguments(b);
		return f;
	}
	
	private boolean hasFinancialProductAlert = false;
	private long clientId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_client_messages_financial_product);
		
		clientId = getArguments().getLong(ARG_CLIENT_ID);
		hasFinancialProductAlert = getArguments().getBoolean(ARG_HAS_PRODUCTS);
	}
		
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		Dictionary<String, String> parameters = GeneralValue.getDictionary(getDataBase(), Constants.GENERAL_TABLE_PARAMETER);						
		
		TextView textViewFin  = (TextView)rootView.findViewById(R.id.textView_financial_product_message);		
		if(hasFinancialProductAlert){
			textViewFin.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_financial_product_message, 0, 0);			
			textViewFin.setText(parameters.get("3"));			
		}else{
			textViewFin.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_about, 0, 0);
			textViewFin.setText(parameters.get("4"));
		}
		
		textViewFin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(hasFinancialProductAlert)
					startActivity(FinancialProductSelectorActivity.newIntent(getActivity(), clientId));
				else{
					finishActivity();	
					startPromptClientEdit();
				}
			}
		});
	}
	
	private void startPromptClientEdit(){		 		
		startActivity(PromptClientUpdateActivity.newIntent(this.getContext(), clientId));
	}
	
}
