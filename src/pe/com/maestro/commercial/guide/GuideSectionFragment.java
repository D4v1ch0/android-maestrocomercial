package pe.com.maestro.commercial.guide;

import java.util.List;

import pe.com.maestro.accounts.adapter.ExpandableGuideSectionAdapter;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.GuideElement;
import pe.com.maestro.commercial.models.GuideSection;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import rp3.app.BaseFragment;

public class GuideSectionFragment extends BaseFragment {

	private GuideSectionListener guideSectionListenerCallback; 
	
	public static GuideSectionFragment newInstance(){
		GuideSectionFragment f = new GuideSectionFragment();
		return f;
	}
	
	public interface GuideSectionListener{
		void onSelectedSection(long guideSectionId, int headerGuideElementId);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_guide_section);
	}
	
	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);
		
		guideSectionListenerCallback = (GuideSectionListener)getParentFragment();
	}
	
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		final List<GuideSection> data = GuideSection.getGuideSections(getDataBase(), true);
		ExpandableGuideSectionAdapter adapter = new ExpandableGuideSectionAdapter(getContext(), data);
		
		setViewAdapter(R.id.listView_guideSection, adapter); 
		setViewOnChildClickListener(R.id.listView_guideSection, new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View view, int groupPosition,
					int childPosition, long childId) {			
				GuideElement headerElement = data.get(groupPosition).getHeaderGuideElements().get(childPosition);
				guideSectionListenerCallback.onSelectedSection(headerElement.getGuideSectionId(), headerElement.getGuideElementId());
				
				int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
			    parent.setItemChecked(index, true);
			    
				return true;
			}
		});
		
//		setViewOnItemClickListener(R.id.listView_guideSection, new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long id) {
////				TextView v = (TextView) arg1.findViewById(R.id.textView_section);
////				v.setTextColor(getResources().getColor(R.color.highlight_title));
//				guideSectionListenerCallback.onSelectedSection(id);				
//			}
//		});				
	}		
	
	
	
	public void setActivateOnItemClick(boolean choice){
		View v = getRootView().findViewById(R.id.listView_guideSection);
			
		
		if(v instanceof ListView){
			if(choice)
				((ListView)v).setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			else
				((ListView)v).setChoiceMode(ListView.CHOICE_MODE_NONE);
		}else if(v instanceof ExpandableListView){
			if(choice)
				((ExpandableListView)v).setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
			else
				((ExpandableListView)v).setChoiceMode(ExpandableListView.CHOICE_MODE_NONE);
		}
		else{
			if(choice)
				((GridView)v).setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			else
				((GridView)v).setChoiceMode(ListView.CHOICE_MODE_NONE);
		}
	}
	
}
