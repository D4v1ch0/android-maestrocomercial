package pe.com.maestro.accounts.adapter;

import java.util.List;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.GuideSection;
import pe.com.maestro.commercial.models.GuideElement;
import rp3.util.FileUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableGuideSectionAdapter extends BaseExpandableListAdapter {

	Context context;
	List<GuideSection> data;
	private LayoutInflater inflater = null;
	
	static class ViewHolder {
		TextView textView_section;
		ImageView icon;
	}
	
	public ExpandableGuideSectionAdapter(Context c, List<GuideSection> data){
		context = c;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}		
    
	@Override
	public Object getChild(int groupPosition, int childPosition) {	
		return data.get(groupPosition).getHeaderGuideElements().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {		
		return ((GuideElement)getChild(groupPosition,childPosition)).getID();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.rowlist_guide_section, null);
			
			holder = new ViewHolder();			
			holder.textView_section = (TextView)convertView.findViewById(R.id.textView_section);			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		GuideElement element = (GuideElement)getChild(groupPosition, childPosition);
		if(element!=null){
			holder.textView_section.setText(element.getName());					
		}
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {		
		return data.get(groupPosition).getHeaderGuideElements().size();
	}

	@Override
	public Object getGroup(int groupPosition) {		
		return data.get(groupPosition);
	}

	@Override
	public int getGroupCount() {		
		return data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {		
		return ((GuideSection)data.get(groupPosition)).getID();
	}

	@Override
	public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {		
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.rowlist_guide_section_group, null);
			
			holder = new ViewHolder();			
			holder.textView_section = (TextView)convertView.findViewById(R.id.textView_section);
			holder.icon = (ImageView)convertView.findViewById(R.id.imageView_icon);
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		GuideSection section = (GuideSection)getGroup(position);
		if(section!=null){
			holder.textView_section.setText(section.getName());
			FileUtils.setBitmapFromInternalStorageAsync(holder.icon, section.getFileName());			
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {		
		return true;
	}

	
}
