package pe.com.maestro.commercial.reports;

import pe.com.maestro.commercial.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import rp3.app.BaseActivity;

public class VendorReportChartActivity extends BaseActivity {

	public static Intent newIntent(Context c){
		Intent i = new Intent(c, VendorReportChartActivity.class);
		return i;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_simple_content);
		
		setHomeAsUpEnabled(true, true);		
		
		if(!hasFragment(R.id.content))
			setFragment(R.id.content, VendorReportChartFragment.newInstance());
	}
	
}
