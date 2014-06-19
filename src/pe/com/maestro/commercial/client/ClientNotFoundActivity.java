package pe.com.maestro.commercial.client;

import pe.com.maestro.commercial.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import rp3.app.BaseActivity;

public class ClientNotFoundActivity extends BaseActivity {

	public static final String EXTRA_CLIENT_DOCUMENT_NUMBER = "documentnumber";	 
	
	public static Intent newIntent(Context c, String clientDocumentNumber){
		Intent i = new Intent(c, ClientNotFoundActivity.class);
		i.putExtra(EXTRA_CLIENT_DOCUMENT_NUMBER, clientDocumentNumber);
		return i;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_client_not_found);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		final String documentNumber = getIntent().getExtras().getString(EXTRA_CLIENT_DOCUMENT_NUMBER);
		
		setButtonClickListener(R.id.button_add_client, new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				
				//if(Screen.isMinLargeLayoutSize(ClientNotFoundActivity.this)){					
				//	showDialogFragment(ClientEditFragment.newInstance(0,documentNumber), "ClientCreateActivity");					
				//}else{
					finish();
					startActivity(ClientEditActivity.newIntent(ClientNotFoundActivity.this,0,documentNumber));
				//}
			}
		});
		
		setButtonClickListener(R.id.button_cancel, new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				finish();			
			}
		});
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
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
		finish();
	}
}