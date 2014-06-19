package pe.com.maestro.commercial.client;

import pe.com.maestro.commercial.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import rp3.app.BaseActivity;

public class ClientEditActivity extends BaseActivity {

	public static final String EXTRA_CLIENT_ID = "clientId";
	public static final String EXTRA_CLIENT_DOCUMENT_NUMBER = "clientdocuemntNumber";
	
	public static Intent newIntent(Context c, long clientId, String documentNumberID){
		Intent i = new Intent(c, ClientEditActivity.class);
		i.putExtra(EXTRA_CLIENT_DOCUMENT_NUMBER, documentNumberID);
		i.putExtra(EXTRA_CLIENT_ID, clientId);
		return i;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_client);
		
		long clientId = getIntent().getExtras().getLong(EXTRA_CLIENT_ID);
		String documentNumber = getIntent().getExtras().getString(EXTRA_CLIENT_DOCUMENT_NUMBER);
		
		if(savedInstanceState==null)
			setFragment(R.id.content, ClientEditFragment.newInstance(clientId,documentNumber));
	}
	
}
