package pe.com.maestro.commercial.sync;


import pe.com.maestro.commercial.db.DbOpenHelper;
import rp3.db.sqlite.DataBase;
import rp3.sync.SyncAudit;
import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

public class SyncAdapter extends rp3.content.SyncAdapter {							
	
	public static String SYNC_TYPE_GENERAL = "general";
	public static String SYNC_TYPE_ALERT = "alert";
	public static String SYNC_TYPE_GUIDE = "guide";
	public static String SYNC_TYPE_UPDATE_CLIENT = "updateclient";
	public static String SYNC_TYPE_VENDOR_REPORT = "vendorreport";
	
	public static String SYNC_TYPE_FINANCIAL_PRODUCT_SELECTED = "financial_product_selected";
	
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);	
	}
	

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {													
		super.onPerformSync(account, extras, authority, provider, syncResult);		
		
		String syncType = extras.getString(ARG_SYNC_TYPE);
					
		DataBase db = null;		
		int result = 0;
		
		try{
			db = DataBase.newDataBase(DbOpenHelper.class);
			
			if(syncType == null || syncType.equals(SYNC_TYPE_GENERAL)){								
				db.beginTransaction();
				
				result = Catalogs.executeSync(db,1);
				addDefaultMessage(result);
				
				boolean includeImages = extras.getBoolean(Catalogs.ARG_INCLUDE_GUIDES, false);
				
				if(result == SYNC_EVENT_SUCCESS){
					result = Catalogs.executeSync(db,2);
				    addDefaultMessage(result);
				}				
				if(includeImages){
					if(result == SYNC_EVENT_SUCCESS){
						result = Catalogs.executeSync(db,3);
					    addDefaultMessage(result);
					}
				}
				if(result == SYNC_EVENT_SUCCESS){
					result = Catalogs.executeSync(db,4);
				    addDefaultMessage(result);
				}
				if(result == SYNC_EVENT_SUCCESS){
					result = Catalogs.executeSync(db,5);
				    addDefaultMessage(result);
				}
				
				if(result == SYNC_EVENT_SUCCESS)
					db.commitTransaction();
				else
					db.rollBackTransaction();	
				
			} else if( syncType.equals(SYNC_TYPE_GUIDE) ){				
				
				db.beginTransaction();
				
				result = Catalogs.executeSync(db,3);
				addDefaultMessage(result);				
				
				db.commitTransaction();			
				
			} else if( syncType.equals(SYNC_TYPE_ALERT) ){
				String clientDocumentId = extras.getString(Alerts.ARG_CLIENT_DOCUMENT_NUMBER);
				
				db.beginTransaction();
								
				result = Alerts.executeSync(db,clientDocumentId);
				addDefaultMessage(result);				
				
				db.commitTransaction();								
				
			} else if(syncType.equals(SYNC_TYPE_FINANCIAL_PRODUCT_SELECTED)){
				
				long clientId = extras.getLong(FinancialProductResponse.ARG_CLIENT_ID);
				
				db.beginTransaction();
				
				Bundle dataOut = new Bundle();				
				result = FinancialProductResponse.executeSync(db, clientId, dataOut);				
				db.commitTransaction();				
				addDefaultMessage(result);
				
				if(dataOut.containsKey("ErrorId")){
					int errorId = dataOut.getInt("ErrorId");
					if(errorId!=0)
						addErrorMessage(dataOut.getString("ErrorMessage"));
				}						
			} else if(syncType.equals(SYNC_TYPE_UPDATE_CLIENT)){
				
				long clientId = extras.getLong(UpdateClient.ARG_CLIENT_ID);
				
				db.beginTransaction();
				
				Bundle dataOut = new Bundle();				
				result = UpdateClient.executeSync(db, clientId, dataOut);				
				db.commitTransaction();				
				addDefaultMessage(result);
				
				if(dataOut.containsKey("ErrorId")){
					int errorId = dataOut.getInt("ErrorId");
					if(errorId!=0)
						addErrorMessage(dataOut.getString("ErrorMessage"));
				}				
			} else if(syncType.equals(SYNC_TYPE_VENDOR_REPORT)){
				db.beginTransaction();
				
				int year = extras.getInt(VendorReport.ARG_YEAR);
				int month = extras.getInt(VendorReport.ARG_MONTH);				
				int goalType = extras.getInt(VendorReport.ARG_GOAL_TYPE);				
				
				result = VendorReport.executeSync(db, year, month, goalType);
				
				db.commitTransaction();				
				addDefaultMessage(result);
			}
			
			SyncAudit.insert(syncType, result);
			
		} catch (Exception e) {			
			Log.e(TAG, "E: " + e.getMessage());
			addDefaultMessage(SYNC_EVENT_ERROR);
			SyncAudit.insert(syncType, SYNC_EVENT_ERROR);
		} 
		finally{			
			db.endTransaction();
			db.close();
			
			notifySyncFinish();
		}								
	}
	
}
