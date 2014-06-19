package pe.com.maestro.commercial.guide;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.GuideElement;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import rp3.app.BaseActivity;
import rp3.util.ViewUtils;

public class GuideElementDetailActivity extends BaseActivity {

	public static final String EXTRA_GUIDE_ELEMENT_ID = "elementId";
	
	public static Intent newIntent(Context c, long guideElementID){
		Intent i = new Intent(c, GuideElementDetailActivity.class);
		i.putExtra(EXTRA_GUIDE_ELEMENT_ID, guideElementID);
		return i;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);			
		
		setContentView(R.layout.fragment_guide_element_display);
		setHomeAsUpEnabled(true, true);
		
		long id = getIntent().getExtras().getLong(EXTRA_GUIDE_ELEMENT_ID);
		GuideElement element = GuideElement.getById(getDataBase(), id);
		
		ViewUtils.setImageViewBitmapFromInternalStorageAsync(getRootView(), R.id.imageView_element, element.getDetailFileName());
	}
	
}
