package pe.com.maestro.commercial;

import pe.com.maestro.commercial.StoreSelectorFragment.StoreSelectorChange;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import rp3.app.BaseActivity;

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
		startActivity(MainActivity.newIntent(this));
		finish();
	}
	
}
