package pe.com.maestro.commercial.guide;


import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.GuideElement;
import android.os.Bundle;
import android.view.View;
import rp3.app.BaseFragment;

public class GuideElementFragment extends BaseFragment {

	public static final String ARG_GUIDE_ELEMENT_ID = "elementId";	
	
	private GuideElement guideElement = null;
	
	public static GuideElementFragment newInstance(long guideElementId) {
        final GuideElementFragment f = new GuideElementFragment();
        final Bundle args = new Bundle();
        args.putLong(ARG_GUIDE_ELEMENT_ID, guideElementId);
        f.setArguments(args);
        return f;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_guide_element_display);
		long id = getArguments().getLong(ARG_GUIDE_ELEMENT_ID,0);
		guideElement = GuideElement.getById(getDataBase(), id);
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
				
		setImageViewBitmapFromInternalStorageAsync(R.id.imageView_element, guideElement.getFileName());				
	}
}
