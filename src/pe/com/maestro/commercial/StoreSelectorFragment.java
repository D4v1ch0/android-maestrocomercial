package pe.com.maestro.commercial;

import pe.com.maestro.commercial.db.Contract;
import pe.com.maestro.commercial.models.Store;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;

public class StoreSelectorFragment extends BaseFragment {

	private Cursor storesCursor;
	private StoreSelectorChange callback;
	
	public static StoreSelectorFragment newInstance(){
		StoreSelectorFragment f = new StoreSelectorFragment();
		return f;
	}
	
	public interface StoreSelectorChange {
		public void onChangeStore();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		if(isDialog())
			setContentView(R.layout.dialog_store_selector);
		else
			setContentView(R.layout.activity_store_selector);				
	}
	
	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);
		
		if(activity instanceof StoreSelectorChange){
			callback = (StoreSelectorChange)activity;
		}else{
			
		}			
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		storesCursor = Store.getStoresCursor(getDataBase());
		
		final Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner_store);
		spinner.setAdapter(new SimpleCursorAdapter(getContext(), R.layout.rowlist_spinner, storesCursor,
				new String[] { Contract.Store.FIELD_NAME }, new int[] {R.id.textView_content}, 0));
		
		
		setSpinnerSelectionByFieldCursor(R.id.spinner_store, Contract.Store.FIELD_STOREID, 
				PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID) );
				
		
		setButtonClickListener(R.id.button_ok, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {				
				if(getSpinnerSelectedIntID(R.id.spinner_store) != 0){
					String storeId = getSpinnerSelectedFieldCursor(R.id.spinner_store, Contract.Store.FIELD_STOREID);				
					PreferenceManager.setValue(Constants.PREF_DEFAULT_STORE_ID, storeId);					
				}
				
				if(isDialog())
					dismiss();
				
				callback.onChangeStore();
								
			}
		});
	}
	
	
	
}
