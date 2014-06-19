package pe.com.maestro.commercial.guide;

import java.util.List;

import pe.com.maestro.accounts.adapter.GuideElementPagerAdapter;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.GuideElement;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import rp3.app.BaseFragment;

public class GuideElementContentPagerFragment extends BaseFragment {
	
	public static final String ARG_SECTION_ID = "sectionid";
	public static final String ARG_GUIDE_ELEMENT_ID = "elementGuideId";
	
	private long sectionId = 0;
	private int elementGuideId = 0;
	private GuideElementChangeListener guideElementChangeCallback;
	
	public interface GuideElementChangeListener{
		public void onCurrentElementChange(long id);
	}
	
	
	public static GuideElementContentPagerFragment newInstance(long sectionId, int elementGuideId){
		GuideElementContentPagerFragment f = new GuideElementContentPagerFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_SECTION_ID, sectionId);
		args.putInt(ARG_GUIDE_ELEMENT_ID, elementGuideId);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);
		
		if(getParentFragment()!=null){
			guideElementChangeCallback = (GuideElementChangeListener)getParentFragment();
		}else{
			guideElementChangeCallback = (GuideElementChangeListener)getActivity();
		}
			
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_guide_element);
		
		sectionId = getArguments().getLong(ARG_SECTION_ID,0);
		elementGuideId = getArguments().getInt(ARG_GUIDE_ELEMENT_ID,0);
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		final List<GuideElement> elements = GuideElement.getGuideElements(getDataBase(), sectionId, elementGuideId);
		
		ViewPager pager = (ViewPager)rootView.findViewById(R.id.pager);		
		pager.setAdapter( new GuideElementPagerAdapter(getChildFragmentManager(), elements));
		
		if(elements.size()>0){
			GuideElement element = elements.get(0);
			guideElementChangeCallback.onCurrentElementChange(element.getID());
		}
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				if(pos>=0){
					GuideElement element = elements.get(pos);
					if(element!=null)
						guideElementChangeCallback.onCurrentElementChange(element.getID());
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

}
