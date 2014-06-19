package pe.com.maestro.commercial;


import android.os.Bundle;

import pe.com.maestro.accounts.MyServerAuthenticate;
import pe.com.maestro.commercial.db.DbOpenHelper;
import pe.com.maestro.commercial.sync.SyncAdapter;
import rp3.configuration.Configuration;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.data.MessageCollection;
import rp3.sync.SyncAudit;


public class StartActivity extends rp3.app.StartActivity {
	
	public StartActivity() {			
		rp3.accounts.Authenticator.setServerAuthenticate(new MyServerAuthenticate());		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {				
		super.onCreate(savedInstanceState);				
		Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);		
		//File imgFile = new  File("E1");
		//ImageView img = (ImageView) findViewById(R.id.imageView_icon);
		//img.setImageURI(Uri.fromFile(imgFile));
	}	
	
	@Override
	public void onContinue() {		
		super.onContinue();
						
		Long days = SyncAudit.getDaysOfLastSync(SyncAdapter.SYNC_TYPE_GENERAL, SyncAdapter.SYNC_EVENT_SUCCESS);
		
		if(days == null || days > 0){
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
			requestSync(bundle);
		}else{
			callNextActivity();
		}		
		
	}
	
	@Override
	public void onSyncComplete(Bundle data, final MessageCollection messages) {
		if(messages.getCuount()>0)
			showDialogMessage(messages, new SimpleCallback() {				
				@Override
				public void onExecute(Object... params) {
					if(!messages.hasErrorMessage())
						callNextActivity();
					else
						finish();
				}
			});
		else
			callNextActivity();
	}
	
	private void callNextActivity(){
//		if(PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID)!=null)
//		{
//			startActivity(MainActivity.newIntent(StartActivity.this));
//		}
//		else
//		{
//			startActivity(StoreSelectorActivity.newIntent(StartActivity.this));
//		}
		startActivity(StoreSelectorActivity.newIntent(StartActivity.this));
		finish();
	}
	
}
