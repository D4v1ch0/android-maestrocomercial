package pe.com.maestro.commercial.client;

import pe.com.maestro.commercial.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import rp3.app.BaseActivity;

public class FinancialProductSelectorActivity extends BaseActivity {

	public static final String EXTRA_CLIENT_ID = "clientId";
	
	public static Intent newIntent(Context c,long clientId){
		Intent i = new Intent(c,FinancialProductSelectorActivity.class);
		i.putExtra(EXTRA_CLIENT_ID, clientId);
		return i;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_financial_product_selector);
		
		long clientId = getIntent().getExtras().getLong(EXTRA_CLIENT_ID, 0);
		
		
		if(savedInstanceState==null)
			setFragment(R.id.content, FinancialProductSelectorFragment.newInstance(clientId));
	}
				
	
}
