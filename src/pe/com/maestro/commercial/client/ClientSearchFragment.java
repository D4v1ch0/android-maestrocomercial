package pe.com.maestro.commercial.client;

import java.util.List;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.StoreSection;
import pe.com.maestro.commercial.sync.Alerts;
import pe.com.maestro.commercial.sync.SyncAdapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleDictionaryAdapter;
import rp3.data.MessageCollection;
import rp3.data.SimpleDictionary;
import rp3.widget.NumberPad;

public class ClientSearchFragment extends BaseFragment {

	private String documentNumberSearch = "";
    private NumberPad Pad;
	
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
        Pad = ((NumberPad)rootView.findViewById(R.id.number_pad_search));
				
		SimpleDictionary sectionList = SimpleDictionary.getFromIdentifiables(sections, true, 
				R.string.hint_select_store_section);
		
		setSpinnerAdapter(R.id.spinner_store, new SimpleDictionaryAdapter(this.getActivity(), 
				sectionList));				
		
		setViewOnItemSelectedListener(R.id.spinner_store, new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				if(pos>0){
					StoreSection s = sections.get(pos-1); //item en blanco
					if(s.getID() != -1)
						PreferenceManager.setValue(Constants.PREF_DEFAULT_STORE_SECTION_ID, s.getStoreSectionId());
				}								
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {								
			}
		});

        //setTextViewText(R.id.textView_client_codigo_derivacion, PreferenceManager.getString(Constants.PREF_CODIGO_DERIVACION));

        rootView.findViewById(R.id.textView_client_codigo_derivacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pad.setTargetView(R.id.textView_client_codigo_derivacion);
            }
        });
        rootView.findViewById(R.id.textView_client_document_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pad.setTargetView(R.id.textView_client_document_id);
            }
        });
		
		setButtonClickListener(R.id.button_query, new AdapterView.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                documentNumberSearch = getTextViewString(R.id.textView_client_document_id);
                PreferenceManager.setValue(Constants.PREF_CODIGO_DERIVACION, getTextViewString(R.id.textView_client_codigo_derivacion));

                if (getSpinnerSelectedIntID(R.id.spinner_store) == -1) {
                    showDialogMessage(R.string.message_validation_select_store_section);
                } else if (TextUtils.isEmpty(documentNumberSearch)) {
                    showDialogMessage(R.string.message_validation_dni_query_required);
                } else if (documentNumberSearch.length() != 8) {
                    showDialogMessage(R.string.message_validation_document_number_length_invalid);
                } else {
                    showDialogProgress(R.string.message_title_query, R.string.message_please_wait);

                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ALERT);
                    bundle.putString(Alerts.ARG_CLIENT_DOCUMENT_NUMBER, documentNumberSearch);
                    bundle.putString(Constants.ARG_CODIGO_DERIVACION, PreferenceManager.getString(Constants.PREF_CODIGO_DERIVACION));
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
