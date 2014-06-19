package pe.com.maestro.accounts.adapter;

import java.util.List;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.GuideSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GuideSectionAdapter extends BaseAdapter {

	private Context context;
	private List<GuideSection> data;
	private LayoutInflater inflater = null;
	
	static class ViewHolder {
		TextView textView_section;		
	}
	
	public GuideSectionAdapter(Context c, List<GuideSection> data){
		this.context = c;
		this.data = data;
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
		return ((GuideSection)getItem(arg0)).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.rowlist_guide_section, null);
			
			holder = new ViewHolder();			
			holder.textView_section = (TextView)convertView.findViewById(R.id.textView_section);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		GuideSection section = (GuideSection)getItem(position);
		if(section!=null){
			holder.textView_section.setText(section.getName());			
			//holder.textView_section.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_important, 0, 0);			
		}
		
		return convertView;
	}

}
