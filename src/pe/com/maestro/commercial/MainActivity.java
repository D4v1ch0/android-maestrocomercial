package pe.com.maestro.commercial;

import java.util.List;

import pe.com.maestro.commercial.StoreSelectorFragment.StoreSelectorChange;
import pe.com.maestro.commercial.client.ClientSearchFragment;
import pe.com.maestro.commercial.guide.MainGuideFragment;
import pe.com.maestro.commercial.models.Store;
import pe.com.maestro.commercial.reports.MainVendorReportFragment;
import pe.com.maestro.commercial.sync.Catalogs;
import pe.com.maestro.commercial.sync.SyncAdapter;

import rp3.app.NavActivity;
import rp3.app.nav.NavItem;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.runtime.Session;
import rp3.util.Screen;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends rp3.app.NavActivity implements StoreSelectorChange {

	public static final int NAV_MESSAGES = 1;
	public static final int NAV_PROMO = 2;
	public static final int NAV_MANAGEMENT_VENDORS = 3;
	public static final int NAV_SYNC = 4;
	public static final int NAV_STORE_CHANGE = 5;
	public static final int NAV_SETTINGS = 6;
	public static final int NAV_CLOSE_SESSION = 7;
	
	public static final int DIALOG_SYNC = 1;
	public static final String DIALOG_FRAGMENT_STORE_SELECTOR = "store";
		
	public static final String EXTRA_NAV_START = "nav";
	
	public static Intent newIntent(Context c){
		Intent i = new Intent(c, MainActivity.class);
		return i;
	}
	
	public static Intent newIntent(Context c, int navStart, boolean clearTop){
		Intent i = new Intent(c, MainActivity.class);
		i.putExtra(EXTRA_NAV_START, navStart);		
		if(clearTop)
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
						
		if(savedInstanceState == null){
			int startNav = NAV_MESSAGES;
			if(getIntent().hasExtra(EXTRA_NAV_START))
				startNav = getIntent().getExtras().getInt(EXTRA_NAV_START, NAV_MESSAGES);
			
			setNavigationSelection(startNav);  			
			
			setStoreSubtitle();
		}
		
		if(Screen.isMinLargeLayoutSize(this))
			setSlindigEnabled(false);
		
		showNavHeader(true);
		setNavHeaderTitle(Session.getUser().getLogonName());		
	}
	
	private void setStoreSubtitle(){
		Store defaultStore = Store.getStore(getDataBase(), PreferenceManager.getString(Constants.PREF_DEFAULT_STORE_ID));
		if(defaultStore!=null)
			setNavHeaderSubtitle(defaultStore.getName());
	}
	
	@Override
	public void navConfig(List<NavItem> navItems, NavActivity currentActivity) {		
		super.navConfig(navItems, currentActivity);
		
		NavItem messages = new NavItem(NAV_MESSAGES, R.string.title_option_alerts, R.drawable.ic_action_email);
		NavItem promo = new NavItem(NAV_PROMO, R.string.title_option_promo, R.drawable.ic_action_picture_dark);
		//NavItem clients = new NavItem(NAV_CLIENT, "Clientes", R.drawable.ic_action_person);		
		NavItem managementVendors = new NavItem(NAV_MANAGEMENT_VENDORS, R.string.title_option_vendor_report, R.drawable.ic_action_chart_dark);
		
		NavItem settingsGroup  = new NavItem(0, R.string.app_name, 0,NavItem.TYPE_CATEGORY);
		
		NavItem sync = new NavItem(NAV_SYNC, R.string.label_update, R.drawable.ic_action_refresh_dark, NavItem.TYPE_ACTION);
		NavItem changeStore = new NavItem(NAV_STORE_CHANGE, R.string.title_option_change_store, R.drawable.ic_action_change_dark, NavItem.TYPE_ACTION);
		NavItem settings = new NavItem(NAV_SETTINGS, R.string.action_settings, R.drawable.ic_action_settings_dark);
		NavItem logOut = new NavItem(NAV_CLOSE_SESSION, R.string.action_logout, R.drawable.ic_action_shutdown_dark, NavItem.TYPE_ACTION);
		
		settingsGroup.addChildItem(sync);
		settingsGroup.addChildItem(changeStore);
		settingsGroup.addChildItem(settings);
		settingsGroup.addChildItem(logOut);
		
		navItems.add(messages);
		navItems.add(promo);
		navItems.add(managementVendors);		
		navItems.add(settingsGroup);
	}
	
	@Override
	public void onNavItemSelected(NavItem item) {		
		super.onNavItemSelected(item);
		
		switch (item.getId()) {
		case NAV_MESSAGES:
			setNavFragment(new ClientSearchFragment(), item.getTitle());
			break;
		case NAV_PROMO:
			
			if(PreferenceManager.getBoolean(Constants.PREF_REQUEST_GUIDE_SYNC)){
				showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);		
				Bundle data = new Bundle();
				data.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GUIDE);
				requestSync(data);
			}else{
				setNavFragment( MainGuideFragment.newInstance(), item.getTitle() );
			}															
			
			break;
		case NAV_SYNC:			
			showDialogConfirmation(DIALOG_SYNC, R.string.message_confirmation_execute_sync, R.string.message_confirmation_title_sync);			
			break;
		case NAV_MANAGEMENT_VENDORS:
			setNavFragment( MainVendorReportFragment.newInstance() , item.getTitle() );
			break;
		case NAV_STORE_CHANGE:
			showDialogFragment( StoreSelectorFragment.newInstance(), DIALOG_FRAGMENT_STORE_SELECTOR, R.string.title_option_change_store);
			break;
		case NAV_SETTINGS:			
			Intent i = new Intent(this, pe.com.maestro.commercial.SettingsActivity.class);
			startActivity(i);
			break;
		case NAV_CLOSE_SESSION:			
			Session.logOut();
			startActivity( new Intent(this, StartActivity.class));
			finish();
			break;
		}
	}
	
	
	@Override
	public void onPositiveConfirmation(int id) {
		super.onPositiveConfirmation(id);
		
		if(id == DIALOG_SYNC){
			showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);			
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
			bundle.putBoolean(Catalogs.ARG_INCLUDE_GUIDES, true);
			requestSync(bundle);
		}
	}
	
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GENERAL)){
			closeDialogProgress();
			if(messages.hasErrorMessage()){
				showDialogMessage(messages);
			}else{
				reset();
			}			
		}else if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GUIDE)){
			PreferenceManager.setValue(Constants.PREF_REQUEST_GUIDE_SYNC, false);
			reset();
		}
	}

	@Override
	public void onChangeStore() {
		setStoreSubtitle();
		PreferenceManager.setValue(Constants.PREF_REQUEST_GUIDE_SYNC, true);
		reset();
	}
}
