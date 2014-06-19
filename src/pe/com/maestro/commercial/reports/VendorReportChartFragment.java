package pe.com.maestro.commercial.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.VendorReport;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import rp3.app.BaseFragment;
import rp3.content.SimpleObjectLoader;
import rp3.util.Format;

public class VendorReportChartFragment extends BaseFragment {

	public static VendorReportChartFragment newInstance(){
		VendorReportChartFragment f = new VendorReportChartFragment();
		return f;
	}
		
	LoaderReport loaderReport;
	List<VendorReport> data;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.fragment_vendor_report_chart);
		loaderReport = new LoaderReport();
	}
	
	private int getSize(int dp){
		DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
		float val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, metrics);
		return (int)val;
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);				
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {		
		super.onActivityCreated(arg0);
		if(getParentFragment() == null)
			queryChart();
	}
	
	public void queryChart(){
		executeLoader(0, null, loaderReport);
	}
	
	private void setDrawChart(){
		XYMultipleSeriesRenderer renderer = getBarRenderer();
        myChartSettings(renderer);
		ViewGroup viewContentChart = (ViewGroup)getRootView().findViewById(R.id.chart);
		viewContentChart.removeAllViews();
		GraphicalView v = ChartFactory.getBarChartView(this.getActivity(), getBarDataset(renderer), renderer, Type.DEFAULT);		
		viewContentChart.addView(v);
	}
	
	private XYMultipleSeriesDataset getBarDataset(XYMultipleSeriesRenderer renderer) {
		
		//data = VendorReport.getVendorReport(getDataBase(), VendorReport.RECORD_MODE_MONTH);
		
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();        
        
        ArrayList<String> legendTitles = new ArrayList<String>();
        legendTitles.add(getText(R.string.label_vendor_report_goal).toString());
        legendTitles.add(getText(R.string.label_vendor_report_real).toString());
        
        
        CategorySeries seriesGoal = new CategorySeries(legendTitles.get(0));
        CategorySeries seriesReal = new CategorySeries(legendTitles.get(1));
        
        if(data.size()>0){
        	String title = null;
        	VendorReport vr = data.get(0);
        	Calendar calendar = Calendar.getInstance();
        	calendar.set(vr.getYear(), vr.getMonth(), vr.getAction());
        	if(vr.getGoalType() == VendorReport.GOAL_TYPE_PRIMARY){        		
        		title = getText(R.string.label_goalType_Primary).toString();
        	}else{
        		title = getText(R.string.label_goalType_Secondary).toString();
        	}
        	title += " " + Format.getDateFormat(calendar, "MMMM yyyy");
        	renderer.setChartTitle(title);
        }
        
        int i = 1;
        for(VendorReport r : data){
        	seriesGoal.add(r.getName(),r.getGoalValue());
        	seriesReal.add(r.getName(),r.getRealValue());
        	
        	renderer.addXTextLabel(i++, r.getName());
        }
        
        dataset.addSeries(seriesGoal.toXYSeries());
        dataset.addSeries(seriesReal.toXYSeries());
        
        return dataset;
    }
 
    public XYMultipleSeriesRenderer getBarRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(getSize(15));
        renderer.setChartTitleTextSize(getSize(18));
        renderer.setLabelsTextSize(getSize(15));
        renderer.setLegendTextSize(getSize(15));
                
        renderer.setMargins(new int[] { getSize(30) , getSize(25), getSize(30), getSize(5) });//top, Iz, Bot, Ri
        
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(getContext().getResources().getColor(R.color.chart1));
        renderer.addSeriesRenderer(r);
        
        r = new XYSeriesRenderer();        
        r.setColor(getContext().getResources().getColor(R.color.chart2));
        renderer.addSeriesRenderer(r);
        
        return renderer;
    }
	
    private void myChartSettings(XYMultipleSeriesRenderer renderer) {        
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setApplyBackgroundColor(true);
        //renderer.setMarginsColor(getResources().getColor(R.color.button_face));
        renderer.setMarginsColor(getResources().getColor(android.R.color.background_light));
        //renderer.setMarginsColor(Color.TRANSPARENT);
        
        //v.setBackgroundColor(getResources().getColor(R.color.bg_main));
        renderer.setXAxisMin(0.5);
        //renderer.setXAxisMax(10.5);
        renderer.setYAxisMin(0);
        //renderer.setYAxisMax(210);
        //renderer.setLegendHeight(getSize(20));
        renderer.setFitLegend(true);      
        //renderer.setFitLegend(true);
        //renderer.setYLabelsAlign(Align.LEFT, 0);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setBarSpacing(0.75);
        renderer.setXTitle(getText(R.string.label_vendor_report_title_x).toString());
        //renderer.setYTitle("Performance");
        renderer.setShowGrid(true);
        renderer.setGridColor(Color.GRAY);
        renderer.setXLabels(0); // sets the number of integer labels to appear
        
        if(this.data.size()==1){
        	renderer.setBarWidth(getSize(30));
        	renderer.setXAxisMax(1.5);
        }else if(this.data.size()>1){
        	renderer.setXAxisMax(data.size() + 0.5);
        }
        
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0,Color.BLACK);
    }
    
    public void setRecords(List<VendorReport> records){
		this.data = records;
		setDrawChart();
	}
    
    public class LoaderReport implements LoaderCallbacks<List<VendorReport>> {

		@Override
		public Loader<List<VendorReport>> onCreateLoader(int arg0, Bundle bundle) {
			return new SimpleObjectLoader<List<VendorReport>>(getContext()) {
				@Override
				public List<VendorReport> loadInBackground() {
					return VendorReport.getVendorReport(getDataBase(),
							VendorReport.RECORD_MODE_MONTH);
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<List<VendorReport>> arg0,
				List<VendorReport> data) {

			setRecords(data);
		}

		@Override
		public void onLoaderReset(Loader<List<VendorReport>> arg0) {
			setRecords(new ArrayList<VendorReport>());
		}

	}
}
