package pe.com.maestro.commercial.client;

import pe.com.maestro.accounts.adapter.ClientFinancialProductAdapter;
import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.MainActivity;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.Client;
import pe.com.maestro.commercial.models.ClientFinancialProduct;
import pe.com.maestro.commercial.sync.FinancialProductResponse;
import pe.com.maestro.commercial.sync.SyncAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import rp3.app.BaseFragment;
import rp3.content.SimpleCallback;
import rp3.data.Dictionary;
import rp3.data.Message;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;
import android.widget.AdapterView;

public class FinancialProductSelectorFragment extends BaseFragment {

	public static final String ARG_CLIENT_ID = "client_id";
	public final int REQUEST_EDIT_CLIENT = 1;
	
	public static FinancialProductSelectorFragment newInstance(long clientId){
		FinancialProductSelectorFragment f = new FinancialProductSelectorFragment();
		Bundle b = new Bundle();
		b.putLong(ARG_CLIENT_ID, clientId);
		f.setArguments(b);
		return f;
	}
	
	private Client client;
	private ClientFinancialProductAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_financial_product_selector);
		
		setRetainInstance(true);
		
		long clientId = getArguments().getLong(ARG_CLIENT_ID);
		
		client = Client.getClient(getDataBase(), clientId, false, true);
		
		adapter = new ClientFinancialProductAdapter(getActivity(),client.getClientFinancialProducts());
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
						
		GridView gv = (GridView)rootView.findViewById(R.id.gridView_financial_product);
		gv.setAdapter(adapter);
		
		Dictionary<String, String> parameters = GeneralValue.getDictionary(getDataBase(), Constants.GENERAL_TABLE_PARAMETER);
		setTextViewText(R.id.textView_label_financial_product_amount, parameters.get("7"));
		setTextViewText(R.id.textView_label_financial_product_description, parameters.get("8"));
		setTextViewText(R.id.textView_label_financial_product_validity_date, parameters.get("9"));
		
		setTextViewNumberText(R.id.textView_financial_product_amount, client.getAmountFinancialProduct());
		setTextViewText(R.id.textView_financial_product_description, client.getFinancialProductAlertMessage());
		setTextViewDateText(R.id.textView_financial_product_validity_date, client.getValidityFinancialProductDate());
		
		
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) { 				
												
				ClientFinancialProduct cp = client.getClientFinancialProducts().get(position);
				if(!cp.isSelected()){
					for(ClientFinancialProduct p : client.getClientFinancialProducts()){
						p.setIsSelected(false);
					}
					cp.setIsSelected(true);										
					adapter.notifyDataSetChanged();					
				}
			}
		});			
		
		setButtonClickListener(R.id.button_positive_confirmation, new View.OnClickListener() {			
			@Override
			public void onClick(View view) {		
				if(client.getClientFinancialProductSelected() == null)
					showDialogMessage(R.string.message_validation_select_financial_product);
				else
				{
					Client.update(getDataBase(), client);
					sendSelection();
				}
			}
		});
		
		setButtonClickListener(R.id.button_negative_confirmation, new View.OnClickListener() {			
			@Override
			public void onClick(View view) {
				client.clearClientFinancialProductSelection();
				Client.update(getDataBase(), client);
				
				startActivity(FinancialProductReasonNotInterestActivity.newIntent(FinancialProductSelectorFragment.this.getContext(), client.getID()));
			}
		});
		
	}
		
	private void sendSelection(){
		showDialogProgress(R.string.message_title_send, R.string.message_please_wait);
		
		Bundle bundle = new Bundle();
		bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_FINANCIAL_PRODUCT_SELECTED);
		bundle.putLong(FinancialProductResponse.ARG_CLIENT_ID, client.getID());
		requestSync(bundle);
	}
	
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_FINANCIAL_PRODUCT_SELECTED)){
			closeDialogProgress();
			
			if(messages.hasErrorMessage()){
				showDialogMessage(messages, new SimpleCallback() {				
					@Override
					public void onExecute(Object... params) {
						finish();
					}
				});
			}else{
				showDialogMessage(new Message(getText(R.string.message_default_success).toString()), new SimpleCallback() {				
					@Override
					public void onExecute(Object... params) {
						startPromptClientEdit();
					}
				});					
			}
		}
	}	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);		
		if(requestCode == REQUEST_EDIT_CLIENT){
			finishActivity();
			startActivity(MainActivity.newIntent(this.getContext(), MainActivity.NAV_MESSAGES, true));
		}
	}
		
	private void startPromptClientEdit(){		
		startActivityForResult(PromptClientUpdateActivity.newIntent(this.getActivity(), client.getID()), REQUEST_EDIT_CLIENT);
	}
}
