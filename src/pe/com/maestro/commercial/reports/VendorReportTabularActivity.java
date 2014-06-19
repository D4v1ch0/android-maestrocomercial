package pe.com.maestro.commercial.reports;

import pe.com.maestro.commercial.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import rp3.app.BaseActivity;

public class VendorReportTabularActivity extends BaseActivity {

	public static Intent newIntent(Context c){
		Intent i = new Intent(c, VendorReportTabularActivity.class);
		return i;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_simple_content, R.menu.activity_vendor_report_tabular);
		
		setFragment(R.id.content, VendorReportTabularFragment.newInstance());
		
		setHomeAsUpEnabled(true, true);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.action_chart){
			startActivity( VendorReportChartActivity.newIntent(this) );
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
}
