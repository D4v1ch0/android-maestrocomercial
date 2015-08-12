package pe.com.maestro.commercial.guide;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.guide.GuideElementContentPagerFragment.GuideElementChangeListener;
import pe.com.maestro.commercial.models.GuideElement;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import rp3.app.BaseFragment;


public class MainGuideFragment extends BaseFragment implements GuideSectionFragment.GuideSectionListener, GuideElementChangeListener {

	private GuideSectionFragment guideSectionFragment;
	private GuideElementContentPagerFragment guideElementContentPagerFragment;
	private ViewGroup viewGroupSection;
	private MenuItem menuDetailInfo;
	private GuideElement currentElement;
	
	private boolean twoPane = false;
	private long currentGuideElementID = -1;
	
	public static MainGuideFragment newInstance(){
		MainGuideFragment f = new MainGuideFragment();
		return f;
	}		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);		
		setContentView(R.layout.fragment_guide, R.menu.fragment_guide_section);	
		
		guideSectionFragment = GuideSectionFragment.newInstance();	
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);				
		
		currentGuideElementID = -1;
		
		beginSetFragment();
		setFragment(R.id.content_guideSection, guideSectionFragment);
		endSetFragment();
		
		if(rootView.findViewById(R.id.content_guideElement) != null){
			twoPane = true;			
			viewGroupSection = (ViewGroup) rootView.findViewById(R.id.content_guideSection);				
		}							
	}
	
	@Override
	public void onAfterCreateOptionsMenu(Menu menu) {		
		super.onAfterCreateOptionsMenu(menu);
		menuDetailInfo = menu.findItem(R.id.action_labels);
		if(currentElement!=null)
			menuDetailInfo.setVisible(currentElement.hasDetailInfo());
	}
	
	@Override
	public void onResume() {		
		super.onResume();
		
		if(twoPane)
			guideSectionFragment.setActivateOnItemClick(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_overflow:
			expandCollapseSection();
			return true;
		case R.id.action_labels:
			if(currentGuideElementID > 0)
				startActivity(GuideElementDetailActivity.newIntent(this.getActivity(), currentGuideElementID));
		return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	
	}
	
	private void expandCollapseSection(){
        if(viewGroupSection != null) {
            if (viewGroupSection.getVisibility() == View.VISIBLE)
                viewGroupSection.setVisibility(View.GONE);
            else
                viewGroupSection.setVisibility(View.VISIBLE);
        }
	}
	
	private void hideSection(){		
		viewGroupSection.setVisibility(View.GONE);		
	}
	
	@Override
	public void onSelectedSection(long guideSectionId, int headerGuideElementId) {		
		if(twoPane){									
			hideSection();
			guideElementContentPagerFragment = GuideElementContentPagerFragment.newInstance(guideSectionId, headerGuideElementId);
			setFragment(R.id.content_guideElement, guideElementContentPagerFragment);
						
		}else {
			startActivity( GuideElementContentPagerActivity.newIntent(this.getContext(), guideSectionId, headerGuideElementId) );
		}		
	}

	@Override
	public void onCurrentElementChange(GuideElement element, long id) {
		currentGuideElementID = id;		
		currentElement = element;
		this.getActivity().invalidateOptionsMenu();
	}
	
}
