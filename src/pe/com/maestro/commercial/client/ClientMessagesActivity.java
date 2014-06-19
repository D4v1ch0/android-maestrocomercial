package pe.com.maestro.commercial.client;

import pe.com.maestro.accounts.adapter.ClientMessagePageAdapter;
import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.Client;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import rp3.app.BaseActivity;
import rp3.data.Dictionary;
import rp3.data.models.GeneralValue;

public class ClientMessagesActivity extends BaseActivity {

	public static final String EXTRA_CLIENT_DOCUMENT_NUMBER = "document_number";	
	
	private final String SAVE_DOCUMENTID = "document_number";
	
	private String clientDocumentId;
	
	public static Intent newIntent(Context c, String clientDocumentNumber){
		Intent i = new Intent(c, ClientMessagesActivity.class);
		i.putExtra(EXTRA_CLIENT_DOCUMENT_NUMBER, clientDocumentNumber);
		return i;  
	}
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_client_messages);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		clientDocumentId = getIntent().getExtras().getString(EXTRA_CLIENT_DOCUMENT_NUMBER);
		
		if(!hasFragment(R.id.content_client_view_message))
			setFragment(R.id.content_client_view_message, ClientMessageDataFragment.newInstance(clientDocumentId));	
		
		Client client = Client.getClient(getDataBase(), clientDocumentId, false, false);
		if(findViewById(R.id.pager)!=null){		
			setViewPagerAdapter(R.id.pager, new ClientMessagePageAdapter(getCurrentFragmentManager(), getDataBase(), client));
		}else{
			Dictionary<String, String> parameters = GeneralValue.getDictionary(getDataBase(), Constants.GENERAL_TABLE_PARAMETER);		
			setTextViewText(R.id.textView_title_client_attention, parameters.get("1"));		
			setTextViewText(R.id.textView_title_client_messages, parameters.get("5"));
			setTextViewText(R.id.textView_title_financial_product, parameters.get("2"));
			
			beginSetFragment();
			if(!hasFragment(R.id.content_client_message_alerts))
				setFragment(R.id.content_client_message_alerts, ClientMessageAlertsFragment.newInstance(client.getID()));
			if(!hasFragment(R.id.content_client_message_attention))
				setFragment(R.id.content_client_message_attention, ClientAttentionMessageFragment.newInstance());
			if(!hasFragment(R.id.content_client_message_financial_product))
				setFragment(R.id.content_client_message_financial_product, ClientMessageFinancialProductFragment.newInstance(client.getID(), client.hasFinancialProductAlert()));
			endSetFragment();
		}				
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		outState.putString(SAVE_DOCUMENTID, clientDocumentId);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			startPromptClientEdit();
			break;

		default:
			break;
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {		
		super.onBackPressed();
		startPromptClientEdit();
	}
	
	private void startPromptClientEdit(){
		long id = Client.getClientID(getDataBase(), clientDocumentId); 		
		startActivity(PromptClientUpdateActivity.newIntent(this, id));
	}
	
}
