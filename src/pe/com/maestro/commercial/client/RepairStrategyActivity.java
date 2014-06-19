package pe.com.maestro.commercial.client;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.MainActivity;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.Client;
import pe.com.maestro.commercial.sync.SyncAdapter;
import pe.com.maestro.commercial.sync.UpdateClient;
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

public class RepairStrategyActivity extends BaseActivity {
	
	public static final String EXTRA_CLIENT_ID = "clientId";
	private Client client;
	
	public static Intent newIntent(Context c, long clientId){
		Intent i = new Intent(c, RepairStrategyActivity.class);		
		i.putExtra(EXTRA_CLIENT_ID, clientId);
		return i;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		long clientId = getIntent().getExtras().getLong(EXTRA_CLIENT_ID);
		
		client = Client.getClient(getDataBase(), clientId, false, true);
		
		setContentView(R.layout.activity_repair_strategy);
		
		
		Dictionary<String, String> parameters = GeneralValue.getDictionary(getDataBase(), Constants.GENERAL_TABLE_PARAMETER);
		
		setSpinnerAdapter(R.id.spinner_cupoun, 
				new SimpleGeneralValueAdapter(this, getDataBase(), Constants.GENERAL_TABLE_REPAIR_STRATEGY, true));
		
		if(client.getRepairStrategyType() == Client.REPAIR_STRATEGY_TYPE_MESSAGE)
			setTextViewText(R.id.textView_repairStrategyMessage, parameters.get("10"));
		else
			setTextViewText(R.id.textView_repairStrategyMessage, parameters.get("11"));	
		
		setButtonClickListener(R.id.button_ok, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				update();
			}
		});
		
//		setButtonClickListener(R.id.button_cancel, new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				update();
//			}
//		});
	}
	
	private void update(){
		client.setRepairStrategyId( getSpinnerGeneralValueSelectedCode(R.id.spinner_cupoun) );
		
		if(Client.update(getDataBase(), client)){
			showDialogProgress(R.string.message_title_send, R.string.message_please_wait);			
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_CLIENT);
			bundle.putLong(UpdateClient.ARG_CLIENT_ID, client.getID());
			requestSync(bundle);
		}
	}
	
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		closeDialogProgress();
		
		if(messages.hasErrorMessage()){
			showDialogMessage(messages, new SimpleCallback() {
				
				@Override
				public void onExecute(Object... params) {
					close();
				}
			});
		}else {
			showDialogMessage(new Message(getText(R.string.message_default_success).toString()), new SimpleCallback() {				
				@Override
				public void onExecute(Object... params) {
					close();
				}
			});				
		}
		
	}
	
	private void close(){
		finish();			
		startActivity(MainActivity.newIntent(this, MainActivity.NAV_MESSAGES, true));
	}
}
