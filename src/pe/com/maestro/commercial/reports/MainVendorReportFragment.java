package pe.com.maestro.commercial.reports;

import java.util.Calendar;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.sync.SyncAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.util.Format;

public class MainVendorReportFragment extends BaseFragment {

	public static MainVendorReportFragment newInstance() {
		MainVendorReportFragment f = new MainVendorReportFragment();
		return f;
	}

	VendorReportTabularFragment tabularFragment;
	VendorReportChartFragment chartFragment;

	boolean useTabular = false;
	boolean useChart = false;
	int goalType;
	Calendar datePeriod;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		setContentView(R.layout.fragment_main_vendor_report);
		
		datePeriod = Calendar.getInstance();
	}

	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		setTextPeriod();
		
		if (getRootView().findViewById(R.id.content_tabular) != null) {
			useTabular = true;

			if (tabularFragment == null)
				tabularFragment = VendorReportTabularFragment.newInstance();

			if (!hasFragment(R.id.content_tabular))
				setFragment(R.id.content_tabular, tabularFragment);
		}

		if (getRootView().findViewById(R.id.content_tabular) != null) {
			useChart = true;

			if (chartFragment == null)
				chartFragment = VendorReportChartFragment.newInstance();

			if (!hasFragment(R.id.content_chart))
				setFragment(R.id.content_chart, chartFragment);
		}
		// ArrayAdapter<String> adapter = new a

		setSpinnerSimpleAdapter(R.id.spinner_goalType,
				R.array.array_vendorReport_goaltypes);

		setButtonClickListener(R.id.button_period, new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogDatePicker(0, true);
			}
		});
		setButtonClickListener(R.id.button_loadReport, new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadData();
			}
		});
	}

	private void loadData() {
		goalType = getSpinnerSelectedPosition(R.id.spinner_goalType) + 1;

		Bundle bundle = new Bundle();
		bundle.putString(SyncAdapter.ARG_SYNC_TYPE,
				SyncAdapter.SYNC_TYPE_VENDOR_REPORT);
		bundle.putInt(pe.com.maestro.commercial.sync.VendorReport.ARG_YEAR,
				datePeriod.get(Calendar.YEAR));
		bundle.putInt(pe.com.maestro.commercial.sync.VendorReport.ARG_MONTH,
				datePeriod.get(Calendar.MONTH) + 1);

		bundle.putInt(
				pe.com.maestro.commercial.sync.VendorReport.ARG_GOAL_TYPE,
				goalType);

		requestSync(bundle);

		showDialogProgress(R.string.message_title_query,
				R.string.message_please_wait);
	}

	@Override
	public void onDailogDatePickerChange(int id, Calendar c) {
		super.onDailogDatePickerChange(id, c);
		datePeriod  = c;		
		setTextPeriod();
	}
	
	private void setTextPeriod(){
		setViewText(R.id.button_period, Format.getDateFormat(datePeriod, "MMMM yyyy"));
	}

	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {
		super.onSyncComplete(data, messages);

		if (data != null && SyncAdapter.SYNC_TYPE_VENDOR_REPORT.equals(data.getString(SyncAdapter.ARG_SYNC_TYPE))) {
			closeDialogProgress();
			if (messages.hasErrorMessage()) {
				showDialogMessage(messages);
			} else {

				if (useTabular)
					tabularFragment.queryReport();
				else
					startActivity(VendorReportTabularActivity.newIntent(this
							.getContext()));

				 if(useChart)
					 chartFragment.queryChart();

			}
		}
	}

	

}
