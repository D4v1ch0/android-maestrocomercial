package pe.com.maestro.commercial.client;

import pe.com.maestro.commercial.MainActivity;
import pe.com.maestro.commercial.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import rp3.app.BaseActivity;

public class PromptClientUpdateActivity extends BaseActivity {

	public static final String EXTRA_CLIENT_ID = "client_id";	 
	
	//private final String STATE_CLIENT_ID = "clientid";
	
	private long clientId;
	
	public static Intent newIntent(Context c, long clientId){
		Intent i = new Intent(c, PromptClientUpdateActivity.class);
		i.putExtra(EXTRA_CLIENT_ID, clientId);
		return i;
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_prompt_client_update);
		
		clientId = getIntent().getExtras().getLong(EXTRA_CLIENT_ID,0);
		
		setButtonClickListener(R.id.button_update_client, new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//if(Screen.isMinLargeLayoutSize(PromptClientUpdateActivity.this))
				//	showDialogFragment(ClientEditFragment.newInstance(clientId, null), "EditClient");
				//else{
					finish();
					startActivity( ClientEditActivity.newIntent(PromptClientUpdateActivity.this, clientId, null) );
				//}
			}
		});
		
		setButtonClickListener(R.id.button_cancel, new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED);
				finish();
				startActivity(MainActivity.newIntent(PromptClientUpdateActivity.this, MainActivity.NAV_MESSAGES, true));
			}
		});
			
		
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		default:
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	
	@Override
	public void onFragmentResult(String tagName, int resultCode, Bundle data) {		
		super.onFragmentResult(tagName, resultCode, data);		
		setResult(RESULT_OK);
		finish();
	}
	
	
	
	
}
