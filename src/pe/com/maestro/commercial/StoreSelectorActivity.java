package pe.com.maestro.commercial;

import pe.com.maestro.commercial.StoreSelectorFragment.StoreSelectorChange;
import pe.com.maestro.commercial.sync.SyncAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.sync.SyncAudit;

public class StoreSelectorActivity extends BaseActivity implements StoreSelectorChange {
	
	public static Intent newIntent(Context c){
		Intent intent = new Intent(c, StoreSelectorActivity.class);
		return intent;
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);			
		
		setContentView(R.layout.layout_simple_content);
		
		setFragment(R.id.content, StoreSelectorFragment.newInstance());
	}

	@Override
	public void onChangeStore() {				
		Long days = SyncAudit.getDaysOfLastSync(SyncAdapter.SYNC_TYPE_GUIDE, SyncAdapter.SYNC_EVENT_SUCCESS);
		if(days > 1 || PreferenceManager.getBoolean(Constants.PREF_REQUEST_GUIDE_SYNC)){
			showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);		
			Bundle data = new Bundle();
			data.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GUIDE);
			requestSync(data);			
		}else{			
			callNextActivity();
		}				
	}
	
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GUIDE)){
			PreferenceManager.setValue(Constants.PREF_REQUEST_GUIDE_SYNC, false);
			callNextActivity();
		}
	}
	
	private void callNextActivity(){
		startActivity(MainActivity.newIntent(this));
		finish();
	}
}
