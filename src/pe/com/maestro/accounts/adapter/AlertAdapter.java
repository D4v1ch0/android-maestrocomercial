package pe.com.maestro.accounts.adapter;

import java.util.List;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.Alert;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlertAdapter extends BaseAdapter {

	private Context context; 
	private List<Alert> data;
	private LayoutInflater inflater = null;
	
	static class ViewHolder {
		TextView textView_message;		
	}
	 
	public AlertAdapter(Context c, List<Alert> alerts){
		this.context = c;
		this.data = alerts;
		inflater = LayoutInflater.from(context);
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
		return ((Alert)getItem(arg0)).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.rowlist_alert, null);
			
			holder = new ViewHolder();			
			holder.textView_message = (TextView)convertView.findViewById(R.id.textView_message);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Alert alert = (Alert)getItem(position);
		if(alert!=null){
			holder.textView_message.setText(alert.getMessage());
			if(alert.isFinancialProductAlert()){
				holder.textView_message.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_important, 0, 0);
				holder.textView_message.setTypeface(null, Typeface.BOLD);
			}else{
				holder.textView_message.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
		}
		
		return convertView;
	}

}
