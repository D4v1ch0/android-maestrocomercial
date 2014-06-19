package pe.com.maestro.commercial.client;

import java.util.Calendar;

import pe.com.maestro.commercial.Constants;
import pe.com.maestro.commercial.MainActivity;
import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.Client;
import pe.com.maestro.commercial.models.GeoDivision;
import pe.com.maestro.commercial.sync.SyncAdapter;
import pe.com.maestro.commercial.sync.UpdateClient;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import rp3.app.BaseFragment;
import rp3.content.SimpleCallback;
import rp3.content.SimpleDictionaryAdapter;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.Message;
import rp3.data.MessageCollection;
import rp3.util.Format;

public class ClientEditFragment extends BaseFragment {

	public static final int REQUEST_REPAIR_STRATEGY = 1;
	
	public static final String ARG_CLIENT_ID = "clientId";
	public static final String ARG_CLIENT_DOCUMENT_NUMBER = "clientdocumentNumber";
	
	public static final ClientEditFragment newInstance(long clientId, String clientDocumentNumber){
		ClientEditFragment f = new ClientEditFragment();
		Bundle args =new Bundle();
		args.putString(ARG_CLIENT_DOCUMENT_NUMBER, clientDocumentNumber);
		args.putLong(ARG_CLIENT_ID, clientId);
		f.setArguments(args);
		return f;
	}
	
	
	private Client client;
	//private GeoDivision defaultGeoDivision;
	private int saveProvinceId;
	private int saveDistrictId;
	private int saveDepartmentId;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setContentView(R.layout.fragment_edit_client);			
		
		//defaultGeoDivision = GeoDivision.getGeoDivision(getDataBase(),
		//		PreferenceManager.getInt(Constants.PREF_DEFAULT_GEOPOLITICAL_ID));
		
		long clientId = getArguments().getLong(ARG_CLIENT_ID,0);
		String insertDocumentNumberID = "";
		
		if(clientId!=0){
			client = Client.getClient(getDataBase(), clientId, false, false);		
			if(client!=null){
				GeoDivision geo = GeoDivision.getGeoDivision(getDataBase(), client.getGeopoliticalId());
				
				if(geo!=null){
					saveDepartmentId = geo.getDepartmentId();
					saveProvinceId = geo.getProvinceId();
					saveDistrictId = geo.getDistrictId();
				}
			}
			
		}else{
			insertDocumentNumberID = getArguments().getString(ARG_CLIENT_DOCUMENT_NUMBER);						
		}
		
//		if(saveDepartmentId == 0 && defaultGeoDivision!=null){
//			saveDepartmentId = defaultGeoDivision.getDepartmentId();
//			saveProvinceId = defaultGeoDivision.getProvinceId();
//			saveDistrictId = defaultGeoDivision.getDistrictId();
//		}
		
		
		if(client==null){
			client = new Client();
			client.setDocumentNumber(insertDocumentNumberID);
		}
		
		client.setContext(this);
	}
				
	private void setBirthdate(){
		if(client.getBirthdate()!=null)
			setViewText(R.id.button_birthday, getText(R.string.label_birthdate_short).toString() + " " +Format.getDefaultDateFormat(client.getBirthdate()));
		else
			setViewText(R.id.button_birthday, getText(R.string.label_birthdate_short).toString());
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
				
		if(isDialog()){
			getDialog().setTitle(R.string.title_activity_client_update);
		}
									
		if(client.getID() == 0){
			setViewVisibility(R.id.editText_documentNumberID, View.VISIBLE);
			setViewVisibility(R.id.textView_documentNumberID, View.GONE);
		}else{
			setViewVisibility(R.id.editText_documentNumberID, View.GONE);
			setViewVisibility(R.id.textView_documentNumberID, View.VISIBLE);
		}
		
		/*Fill Data*/		
		setTextViewText(R.id.editText_documentNumberID, client.getDocumentNumber());
		setTextViewText(R.id.textView_documentNumberID, client.getDocumentNumber());
		setTextViewText(R.id.editText_firstName, client.getFirstName());
		setTextViewText(R.id.editText_middleName, client.getMiddleName());
		setTextViewText(R.id.editText_lastName, client.getLastName());
		setTextViewText(R.id.editText_secondLastName, client.getSecondLastName());
		
		setTextViewText(R.id.editText_cellphone, client.getCellphone());
		setTextViewText(R.id.editText_phoneNumber, client.getPhoneNumber());
		setTextViewText(R.id.editText_email, client.getEmail());
		
		setBirthdate();	
			
		setButtonClickListener(R.id.button_birthday, new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(client.getBirthdate()!=null)
					showDialogDatePicker(0, client.getBirthdate());
				else
					showDialogDatePicker(0);
			}
		});
		
		setTextViewText(R.id.editText_addressReference, client.getAddressReference());
		setTextViewText(R.id.editText_addressSegment1, client.getAddressSegment1());
		setTextViewText(R.id.editText_addressSegment2, client.getAddressSegment2());
		setTextViewText(R.id.editText_addressSegment3, client.getAddressSegment3());
		setTextViewText(R.id.editText_addressSegment4, client.getAddressSegment4());		
		setTextViewText(R.id.editText_addressSegment5, client.getAddressSegment5());
		setTextViewText(R.id.editText_addressSegment6, client.getAddressSegment6());
		setTextViewText(R.id.editText_addressSegment7, client.getAddressSegment7());
		setTextViewText(R.id.editText_addressSegment8, client.getAddressSegment8());
		setTextViewText(R.id.editText_addressSegment9, client.getAddressSegment9());
						
		setTextViewText(R.id.editText_cellphone2, client.getCellphone2());
		setTextViewText(R.id.editText_phoneNumber2, client.getPhoneNumber2());
		setTextViewText(R.id.editText_roadTypeName, client.getRoadTypeName());
		setTextViewText(R.id.editText_zoneTypeName, client.getZoneTypeName());		
		
		setSpinnerAdapter(R.id.spinner_livingSituation, 
				new SimpleGeneralValueAdapter(this.getContext(), getDataBase(), Constants.GENERAL_TABLE_LIVING_SITUATION, true));
		
		setSpinnerAdapter(R.id.spinner_roadType, 
				new SimpleGeneralValueAdapter(this.getContext(), getDataBase(), Constants.GENERAL_TABLE_ROADTYPE, true));
		
		setSpinnerAdapter(R.id.spinner_zoneType, 
				new SimpleGeneralValueAdapter(this.getContext(), getDataBase(), Constants.GENERAL_TABLE_ZONETYPE, true));
		
		setSpinnerAdapter(R.id.spinner_profile, 
				new SimpleGeneralValueAdapter(this.getContext(), getDataBase(), Constants.GENERAL_TABLE_PROFILE, true));
		
		setSpinnerGeneralValueSelection(R.id.spinner_livingSituation, client.getLivingSituation());
		setSpinnerGeneralValueSelection(R.id.spinner_roadType, client.getRoadTypeId());
		setSpinnerGeneralValueSelection(R.id.spinner_zoneType, client.getZoneTypeId());
		setSpinnerGeneralValueSelection(R.id.spinner_profile, client.getProfileId());		
		
		setSpinnerAdapter(R.id.spinner_department, new SimpleDictionaryAdapter(this.getContext(), 
				GeoDivision.getDepartments(getDataBase())));						
		
		setButtonClickListener(R.id.button_accept, new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				updateClient();
			}
		});
		
		setButtonClickListener(R.id.button_cancel, new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				setResult(RESULT_CANCELED, null);
				finish();
				startActivity(MainActivity.newIntent(ClientEditFragment.this.getContext(), MainActivity.NAV_MESSAGES, true));
			}
		});
		
		setViewOnItemSelectedListener(R.id.spinner_department, new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				
				setSpinnerAdapter(R.id.spinner_province, new SimpleDictionaryAdapter(ClientEditFragment.this.getContext(), 
						GeoDivision.getProvinces(getDataBase(), id)));
				
				setSpinnerSelectionById(R.id.spinner_province, saveProvinceId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {			
			}
		});
		
		setViewOnItemSelectedListener(R.id.spinner_province, new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				
				setSpinnerAdapter(R.id.spinner_district, new SimpleDictionaryAdapter(ClientEditFragment.this.getContext(), 
						GeoDivision.getDistrict(getDataBase(), id)));
				
				setSpinnerSelectionById(R.id.spinner_district, saveDistrictId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {			
			}
		});
		
		
		setSpinnerSelectionById(R.id.spinner_department, saveDepartmentId);								
		
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle arg0) {		
		super.onSaveInstanceState(arg0);
		saveProvinceId = getSpinnerSelectedIntID(R.id.spinner_province);
		saveDistrictId = getSpinnerSelectedIntID(R.id.spinner_district);
	}
	
	private void updateClient(){
		
		client.setFirstName(getTextViewString(R.id.editText_firstName));
		client.setMiddleName( getTextViewString(R.id.editText_middleName) );
		client.setLastName( getTextViewString(R.id.editText_lastName) );
		client.setSecondLastName( getTextViewString(R.id.editText_secondLastName) );
		client.setDocumentNumber( getTextViewString(R.id.editText_documentNumberID) );
		
		client.setEmail( getTextViewString(R.id.editText_email) );
		client.setCellphone( getTextViewString(R.id.editText_cellphone) );
		client.setPhoneNumber( getTextViewString(R.id.editText_phoneNumber) );						
		
		client.setAddressReference(getTextViewString(R.id.editText_addressReference));
		client.setAddressSegment1(getTextViewString(R.id.editText_addressSegment1));
		client.setAddressSegment2(getTextViewString(R.id.editText_addressSegment2));
		client.setAddressSegment3(getTextViewString(R.id.editText_addressSegment3));
		client.setAddressSegment4(getTextViewString(R.id.editText_addressSegment4));
		client.setAddressSegment5(getTextViewString(R.id.editText_addressSegment5));
		client.setAddressSegment6(getTextViewString(R.id.editText_addressSegment6));
		client.setAddressSegment7(getTextViewString(R.id.editText_addressSegment7));
		client.setAddressSegment8(getTextViewString(R.id.editText_addressSegment8));
		client.setAddressSegment9(getTextViewString(R.id.editText_addressSegment9));
		
		client.setCellphone2(getTextViewString(R.id.editText_cellphone2));
		client.setPhoneNumber2(getTextViewString(R.id.editText_phoneNumber2));
		client.setRoadTypeName(getTextViewString(R.id.editText_roadTypeName));
		client.setZoneTypeName(getTextViewString(R.id.editText_zoneTypeName));
		
		client.setRoadTypeId(getSpinnerGeneralValueSelectedCode(R.id.spinner_roadType));
		client.setZoneTypeId(getSpinnerGeneralValueSelectedCode(R.id.spinner_zoneType));
		client.setLivingSituation(getSpinnerGeneralValueSelectedCode(R.id.spinner_livingSituation));
		client.setProfileId(getSpinnerGeneralValueSelectedCode(R.id.spinner_profile));

		int departmentId = getSpinnerSelectedIntID(R.id.spinner_department);
		int provinceId = getSpinnerSelectedIntID(R.id.spinner_province);
		int districtId = getSpinnerSelectedIntID(R.id.spinner_district);
		
		long geoId = GeoDivision.getIDFromParts(getDataBase(), departmentId, provinceId, districtId);
		client.setGeopoliticalId(geoId);		
		
		boolean success = false;
		if(client.getID() == 0)
			success = Client.insert(getDataBase(), client);
		else
			success = Client.update(getDataBase(), client);
		
		if(success){
			if(client.applyRepairStrategy()){
				startActivityForResult(RepairStrategyActivity.newIntent(this.getContext(), client.getID()), REQUEST_REPAIR_STRATEGY);
			}else{			
				showDialogProgress(R.string.message_title_send, R.string.message_please_wait);
			
				Bundle bundle = new Bundle();
				bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_CLIENT);
				bundle.putLong(UpdateClient.ARG_CLIENT_ID, client.getID());
				requestSync(bundle);
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case REQUEST_REPAIR_STRATEGY:
			close();
			break;
		}
	}
	
	@Override
	public void onDailogDatePickerChange(int id, Calendar c) {		
		super.onDailogDatePickerChange(id, c);		
		client.setBirthdate(c.getTime());
		setBirthdate();
	}
	
	@Override
	public void onEntityItemValidationFailed(Message m, Object e) {		
		super.onEntityItemValidationFailed(m, e);
		
		switch(m.getKey()){
			case Client.KEY_DOCUMENT_NUMBER:
				setViewError(R.id.editText_documentNumberID, m);
			break;
			case Client.KEY_FIRST_NAME:
				setViewError(R.id.editText_firstName, m);
			break;
			case Client.KEY_LAST_NAME:
				setViewError(R.id.editText_lastName, m);
			case Client.KEY_CELLPHONE:
				setViewError(R.id.editText_cellphone, m);
			break;
			case Client.KEY_PHONE:
				setViewError(R.id.editText_phoneNumber, m);
			break;
			case Client.KEY_EMAIL:
				setViewError(R.id.editText_email, m);
			break;
			case Client.KEY_PROFILE_ID:
				setViewError(R.id.spinner_profile, m);
			break;
		}
	}
	
	@Override
	public void onEntityValidationFailed(MessageCollection mc, Object e) {		
		super.onEntityValidationFailed(mc, e);
		
		if(mc.hasErrorMessage()){
			showDialogMessage(R.string.message_validation_client_update);
		}
		
	}
	
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		closeDialogProgress();
		
		if(messages.hasErrorMessage()){
			showDialogMessage(messages);
		}else {
			
			showDialogMessage(new Message(getText(R.string.message_default_success).toString()), new SimpleCallback() {				
				@Override
				public void onExecute(Object... params) {
					close();
				}
			});										
		}
		
	}
	
	private void close(){
		finish();		
		setResult(RESULT_OK, null);
		startActivity(MainActivity.newIntent(this.getContext(), MainActivity.NAV_MESSAGES, true));
	}
}
