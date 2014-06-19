package pe.com.maestro.commercial.reports;

import java.util.List;

import pe.com.maestro.accounts.adapter.VendorReportAdapter;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.VendorReport;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import rp3.app.BaseFragment;
import rp3.content.SimpleObjectLoader;

public class VendorReportTabularFragment extends BaseFragment {

	private List<VendorReport> records;
	LoaderReport loaderReport;
	
	public static VendorReportTabularFragment newInstance(){
		VendorReportTabularFragment f = new VendorReportTabularFragment();
		return f;
	}
	
	public void setRecords(List<VendorReport> records){
		this.records = records;
		setViewAdapter(R.id.listView_tabular, new VendorReportAdapter(this.getContext(), this.records));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_tabular_vendor_report);
		loaderReport = new LoaderReport();
	}
		
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
	}
	
	public void queryReport(){		
		executeLoader(0, null, loaderReport);
	}
	
	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);			
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {		
		super.onActivityCreated(arg0);
		if(getParentFragment() == null)
			queryReport();
	}
	
	public class LoaderReport implements LoaderCallbacks<List<VendorReport>>{
		
		@Override
		public Loader<List<VendorReport>> onCreateLoader(int arg0,
				Bundle bundle) {													
			return new SimpleObjectLoader<List<VendorReport>>(getContext()) {
				@Override
				public List<VendorReport> loadInBackground() {					
					return VendorReport.getVendorReport(getDataBase(), VendorReport.RECORD_MODE_DAILY);
				}
			};			
		}

		@Override
		public void onLoadFinished(Loader<List<VendorReport>> arg0,
				List<VendorReport> data){														
			
			setRecords(data);
		}

		@Override
		public void onLoaderReset(Loader<List<VendorReport>> arg0) {			
			setRecords(null);
		}
		
	}
	
}
