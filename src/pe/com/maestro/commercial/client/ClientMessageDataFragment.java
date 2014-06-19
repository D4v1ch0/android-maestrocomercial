package pe.com.maestro.commercial.client;


import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.Client;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import rp3.app.BaseFragment;

public class ClientMessageDataFragment extends BaseFragment {

	public static final String ARG_CLIENT_DOCUMENT_NUMBER = "client_document_number";
	
	public Client client;
	
	public static ClientMessageDataFragment newInstance(String clientDocumentId){
		ClientMessageDataFragment f = new ClientMessageDataFragment();
		Bundle b = new Bundle();
		b.putString(ARG_CLIENT_DOCUMENT_NUMBER, clientDocumentId);
		
		f.setArguments(b);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
				
		setRetainInstance(true);		
		
		String clientDocumentNumber = getArguments().getString(ARG_CLIENT_DOCUMENT_NUMBER, "");
		client = Client.getClient(getDataBase(), clientDocumentNumber, true, false);
		
		if(client!=null){
			setContentView(R.layout.fragment_client_view_message);
		}else{			
			finish();
			startActivity(ClientNotFoundActivity.newIntent(getActivity(), clientDocumentNumber));			
		}
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		setTextViewText(R.id.textView_client, client.getFullName());
		setTextViewText(R.id.textView_address, client.getAddress());
		setTextViewText(R.id.textView_defaultPhoneNumber, client.getPhoneNumber());
		setTextViewText(R.id.textView_documentNumber, client.getDocumentNumber());
		setTextViewText(R.id.textView_financialProductMessage, client.getFinancialProductMessage());		
		
		if(TextUtils.isEmpty(client.getFinancialProductMessage()))
			setViewVisibility(R.id.textView_financialProductMessage, View.GONE);
		
//		Dictionary<String, String> parameters = GeneralValue.getDictionary(getDataBase(), Constants.GENERAL_TABLE_PARAMETER);		
		
//		setTextViewText(R.id.textView_title_client_attention, parameters.get("1"));
//		setTextViewText(R.id.textView_title_financial_product, parameters.get("2"));
//		setTextViewText(R.id.textView_title_client_messages, parameters.get("5"));
//		setTextViewText(R.id.textView_noresults_message, parameters.get("6"));
		
//		TextView textViewFin  = (TextView)rootView.findViewById(R.id.textView_financial_product_message);		
//		if(client.hasFinancialProductAlert()){
//			textViewFin.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_important, 0, 0);			
//			textViewFin.setText(parameters.get("3"));			
//		}else{
//			textViewFin.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_important, 0, 0);
//			textViewFin.setText(parameters.get("4"));
//		}
//		
//		textViewFin.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				if(client.hasFinancialProductAlert())
//					startActivity(FinancialProductSelectorActivity.newIntent(getActivity(), client.getID()));
//				else{
//					finishActivity();
//					startPromptClientEdit();
//				}
//			}
//		});
		
		
		
//		setViewAdapter(R.id.listView_alerts, new AlertAdapter(this.getContext(), client.getAlerts()));
//		setViewAdapter(R.id.listView_client_attention, new SimpleGeneralValueAdapter(this.getContext(), 
//				GeneralValue.getGeneralValues(getDataBase(), Constants.GENERAL_TABLE_STEP_CUSTOMER_ATTENTION, true)));		
		
//		if(client.getAlerts().size()>0){
//			setViewVisibility(R.id.textView_noresults_message, View.GONE);
//		}else{
//			setViewVisibility(R.id.textView_noresults_message, View.VISIBLE);
//		}
	}	

}
