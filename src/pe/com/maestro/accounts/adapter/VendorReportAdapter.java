package pe.com.maestro.accounts.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.VendorReport;
import rp3.data.Dictionary;
import rp3.data.DictionaryEntry;
import rp3.util.Format;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VendorReportAdapter extends BaseAdapter {

	private Context context;
	private List<VendorReport> data;
	private LayoutInflater inflater = null;	
	
	static class ViewHolder {
		TextView textView_name;
		TextView textView_goal;
		TextView textView_real;
		TextView textView_percent;
	}
	
	public VendorReportAdapter(Context c, List<VendorReport> records){
		this.context = c;
		this.data = new ArrayList<VendorReport>();
		if(records!=null)
			this.data.addAll(records);		
		inflater = LayoutInflater.from(context);		
		init();
	}
	
	@Override
	public int getCount() {		
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {		
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {		
		return ((VendorReport)getItem(arg0)).getID();
	}

	private void init(){
		int lastRecordNumber = 0;
		Dictionary<Integer,VendorReport> dic = new Dictionary<Integer,VendorReport>();
		
		int pos = 0;
		for(VendorReport r : data){
			if(lastRecordNumber != r.getDay()){
				lastRecordNumber = r.getDay();
				dic.set(pos, r);
			}
			pos++;
		}
		
		int incrementInsert = 0;
		for(DictionaryEntry<Integer, VendorReport> entry : dic.getEntries()){
			
			VendorReport category = new VendorReport();
			
			category.setID(0);
			category.setDay(entry.getValue().getDay());
			category.setMonth(entry.getValue().getMonth());
			category.setYear( entry.getValue().getYear());
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(category.getYear(), category.getMonth()-1, category.getDay());
			
			String display = Format.getDateFormat(calendar, "EEEE dd MMMM"); 			
			
			category.setName(display);
			
			data.add(entry.getKey() + incrementInsert, category);
			incrementInsert++;
		}
	}
	
	@Override
	public int getViewTypeCount() {		
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {		
		VendorReport r = (VendorReport)getItem(position);
		if(r.getID() == 0) 
			return 1;
		else
			return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		VendorReport record = (VendorReport)getItem(position);
		if (convertView == null) {
						
			holder = new ViewHolder();						
			
			if(record.getID() != 0){
				convertView = inflater.inflate(R.layout.rowlist_vendor_report_detail, null);
				
				holder.textView_goal = (TextView)convertView.findViewById(R.id.textView_goal);
				holder.textView_real = (TextView)convertView.findViewById(R.id.textView_real);
				holder.textView_percent = (TextView)convertView.findViewById(R.id.textView_percent);
			}else{
				convertView = inflater.inflate(R.layout.rowlist_vendor_report_category, null);				
			}
			
			holder.textView_name = (TextView)convertView.findViewById(R.id.textView_name);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		if(record!=null){
			holder.textView_name.setText( record.getName() );
			
			if(record.getID()!=0){
				holder.textView_goal.setText( Format.getDefaultNumberFormat( record.getGoalValue() ) );
				holder.textView_real.setText( Format.getDefaultNumberFormat( record.getRealValue() ) );
				holder.textView_percent.setText( Format.getPercentFormat( record.getPercent() ) );
			}
		}
		
		return convertView;
	}

}
