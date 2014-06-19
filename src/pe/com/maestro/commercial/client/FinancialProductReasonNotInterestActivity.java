package pe.com.maestro.commercial.client;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.MainActivity;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.Client;
import pe.com.maestro.commercial.sync.FinancialProductResponse;
import pe.com.maestro.commercial.sync.SyncAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import rp3.app.BaseActivity;
import rp3.content.SimpleCallback;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.Dictionary;
import rp3.data.Message;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;

public class FinancialProductReasonNotInterestActivity extends BaseActivity {
	
	public final int REQUEST_EDIT_CLIENT = 1;
	public static final String EXTRA_CLIENT_ID = "clientId";
	private Client client;
	
	public static Intent newIntent(Context c, long clientId){
		Intent i = new Intent(c, FinancialProductReasonNotInterestActivity.class);		
		i.putExtra(EXTRA_CLIENT_ID, clientId);
		return i;
	}
	
	
	private long clientId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		clientId = getIntent().getExtras().getLong(EXTRA_CLIENT_ID);
		
		client = Client.getClient(getDataBase(), clientId, false, true);
		
		setContentView(R.layout.activity_financial_product_reason_non_interest);
		
		Dictionary<String,String> dic = GeneralValue.getDictionary(getDataBase(), Constants.GENERAL_TABLE_PARAMETER);
		
		this.setTitle(dic.get("14"));
		
		this.setTextViewText(R.id.textView_message, dic.get("15"));
		setSpinnerAdapter(R.id.spinner_reasons, new SimpleGeneralValueAdapter(this, getDataBase(), 
				Constants.GENERAL_TABLE_REASONS_NON_INTEREST_FINANCIAL_PRODUCT, true));
		
		setButtonClickListener(R.id.button_ok, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(getSpinnerSelectedLongID(R.id.spinner_reasons)==-1)
					showDialogMessage(R.string.message_validation_select_financial_product_reason_not_interest);
				else
					sendSelection();
			}
		});
		
		setButtonClickListener(R.id.button_cancel, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {				
				finish();
			}
		});
	}		
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);		
		if(requestCode == REQUEST_EDIT_CLIENT){
			finish();
			startActivity(MainActivity.newIntent(this, MainActivity.NAV_MESSAGES, true));
		}
	}
	
	private void startPromptClientEdit(){		
		startActivityForResult(PromptClientUpdateActivity.newIntent(this, clientId), REQUEST_EDIT_CLIENT);
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
	
	private void sendSelection(){
		client.setFinancialProductReasonOfNotInterest(getSpinnerGeneralValueSelectedCode(R.id.spinner_reasons));
		if(Client.update(getDataBase(), client)){		
			showDialogProgress(R.string.message_title_send, R.string.message_please_wait);
			
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_FINANCIAL_PRODUCT_SELECTED);
			bundle.putLong(FinancialProductResponse.ARG_CLIENT_ID, clientId);
			requestSync(bundle);
		}
	}
	
}
