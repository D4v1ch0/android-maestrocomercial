package pe.com.maestro.commercial.guide;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.guide.GuideElementContentPagerFragment.GuideElementChangeListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import rp3.app.BaseActivity;

public class GuideElementContentPagerActivity extends BaseActivity implements GuideElementChangeListener {

	public static final String EXTRA_SECTION_ID = "sectionId";
	public static final String EXTRA_GUIDE_ELEMENT_ID = "elementGuideId";
	
	private long currentGuideElementID = -1;
	
	public static Intent newIntent(Context c, long sectionId, int elementGuideId){
		Intent i = new Intent(c, GuideElementContentPagerActivity.class);
		i.putExtra(EXTRA_SECTION_ID, sectionId);
		i.putExtra(EXTRA_GUIDE_ELEMENT_ID, elementGuideId);
		return i;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_simple_content, R.menu.activity_guide_element_pager);
		currentGuideElementID = -1;
		
		long sectionId = getIntent().getExtras().getLong(EXTRA_SECTION_ID,0);
		int elementGuideId = getIntent().getExtras().getInt(EXTRA_GUIDE_ELEMENT_ID,0);
				
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(savedInstanceState==null){
			setFragment(R.id.content, GuideElementContentPagerFragment.newInstance(sectionId, elementGuideId));
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_labels:
			if(currentGuideElementID > 0)
				startActivity(GuideElementDetailActivity.newIntent(this, currentGuideElementID));
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onCurrentElementChange(long id) {
		currentGuideElementID = id;		
	}
	
	
}
