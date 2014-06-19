package pe.com.maestro.commercial.client;

import java.util.List;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.StoreSection;
import pe.com.maestro.commercial.sync.Alerts;
import pe.com.maestro.commercial.sync.SyncAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleDictionaryAdapter;
import rp3.data.MessageCollection;
import rp3.data.SimpleDictionary;

public class ClientSearchFragment extends BaseFragment {

	private String documentNumberSearch = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_client_query);
		setRetainInstance(true);				
	}		
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);				
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
				
		//setTextViewText(R.id.textView_client_document_id, "08941797");
		final List<StoreSection> sections = StoreSection.getStoreSections(getDataBase());
				
		setSpinnerAdapter(R.id.spinner_store, new SimpleDictionaryAdapter(this.getActivity(), 
				SimpleDictionary.getFromIdentifiables(sections, true, R.string.hint_select_store_section)));				
		
		setViewOnItemSelectedListener(R.id.spinner_store, new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				
				StoreSection s = sections.get(pos);
				if(s.getID() != -1)
					PreferenceManager.setValue(Constants.PREF_DEFAULT_STORE_SECTION_ID, s.getStoreSectionId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {								
			}
		});
		
		setButtonClickListener(R.id.button_query, new AdapterView.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {	
				
				if(getSpinnerSelectedIntID(R.id.spinner_store)==-1){
					showDialogMessage(R.string.message_validation_select_store_section);
				}
				else{
					showDialogProgress(R.string.message_title_query, R.string.message_please_wait);
					documentNumberSearch = getTextViewString(R.id.textView_client_document_id);
					
					Bundle bundle = new Bundle();
					bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ALERT);
					bundle.putString(Alerts.ARG_CLIENT_DOCUMENT_NUMBER, documentNumberSearch);				
					requestSync(bundle);
				}				
			}
		});
	}
	
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);				
		
		if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_ALERT)){
			closeDialogProgress();
			if(messages.hasErrorMessage()){
				showDialogMessage(messages);
			}else{
				String documentNumberSearchFinal = getTextViewString(R.id.textView_client_document_id);
				if(documentNumberSearch.equals(documentNumberSearchFinal))
					startActivity(ClientMessagesActivity.newIntent(this.getContext(), documentNumberSearchFinal));
			}
		}
				
	}
	
}
