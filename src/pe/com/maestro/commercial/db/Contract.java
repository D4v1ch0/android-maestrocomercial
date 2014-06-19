package pe.com.maestro.commercial.db;

import android.provider.BaseColumns;

public final class Contract {

	public static abstract class Store implements BaseColumns {
	
		public static final String TABLE_NAME = "tbStore";
		
		public static final String COLUMN_STORE_ID = "StoreId";
		public static final String COLUMN_NAME = "Name";
		
		public static final String FIELD_STOREID = COLUMN_STORE_ID;
		public static final String FIELD_NAME = COLUMN_NAME;	
	}
	
	public static abstract class GeoDivision implements BaseColumns{
		public static final String TABLE_NAME = "tbGeoDivision";
			
		public static final String COLUMN_DEPARTMENT_ID = "DepartmentId";
		public static final String COLUMN_DEPARTMENT_NAME = "Department";
		public static final String COLUMN_PROVINCE_ID = "ProvinceId";
		public static final String COLUMN_PROVINCE_NAME = "Province";
		public static final String COLUMN_DISTRICT_ID = "DistrictId";
		public static final String COLUMN_DISTRICT_NAME = "District";
				
		public static final String FIELD_DEPARTMENT_ID = COLUMN_DEPARTMENT_ID;
		public static final String FIELD_DEPARTMENT_NAME = COLUMN_DEPARTMENT_NAME;
		public static final String FIELD_PROVINCE_ID = COLUMN_PROVINCE_ID;
		public static final String FIELD_PROVINCE_NAME = COLUMN_PROVINCE_NAME;
		public static final String FIELD_DISTRICT_ID = COLUMN_DISTRICT_ID;
		public static final String FIELD_DISTRICT_NAME = COLUMN_DISTRICT_NAME;
		
		public static final String WHERE_PARTS = COLUMN_DEPARTMENT_ID + " = ? AND " +
						COLUMN_PROVINCE_ID + " = ? AND " +
						COLUMN_DISTRICT_ID + " = ? ";
	}
		
	public static abstract class StoreSection implements BaseColumns{
		public static final String TABLE_NAME = "tbStoreSection";
		
		public static final String COLUMN_STORESECTION_ID = "StoreSectionId";
		public static final String COLUMN_NAME = "Name";
		
		public static final String FIELD_STORESECTION_ID = COLUMN_STORESECTION_ID;
		public static final String FIELD_NAME = COLUMN_NAME;
	}		
	
	public static abstract class Alert implements BaseColumns{
		public static final String TABLE_NAME = "tbAlert";
		
		public static final String COLUMN_CONTENT = "Message";	
		public static final String COLUMN_CLIENT_ID = "ClientId";
		
		public static final String FIELD_CONTENT = COLUMN_CONTENT;
		public static final String FIELD_CLIENT_ID = COLUMN_CLIENT_ID;
	}
	
	public static abstract class FinancialProduct implements BaseColumns{
		public static final String TABLE_NAME = "tbFinancialProduct";
		
		public static final String COLUMN_NAME = "Name";
		public static final String FIELD_NAME = COLUMN_NAME;
	}
	
	public static abstract class ClientFinancialProduct implements BaseColumns{
		public static final String TABLE_NAME = "tbClientFinancialProduct";
	    
		public static final String COLUMN_FINANCIAL_PRODUCT_ID = "FinancialProductId";
		public static final String COLUMN_CLIENT_ID = "ClientId";
		public static final String COLUMN_INTERNAL_MESSAGE_ID = "InternalMessageId";
		public static final String COLUMN_IS_SELECTED = "IsSelected";				
		public static final String FIELD_FINANNCIAL_PRODUCT_ID = COLUMN_FINANCIAL_PRODUCT_ID;
		public static final String FIELD_CLIENT_ID = COLUMN_CLIENT_ID;
		public static final String FIELD_IS_SELECTED = COLUMN_IS_SELECTED;
		public static final String FIELD_FINANCIAL_PRODUCT_NAME = FinancialProduct.TABLE_NAME + "_" + FinancialProduct.COLUMN_NAME;
		public static final String FIELD_INTERNAL_MESSAGE_ID = COLUMN_INTERNAL_MESSAGE_ID;
		
		public static final String QUERY_CLIENT_FINANCIAL_PRODUCT = "ClientFinancialProduct";		
	}
	
	public static abstract class GuideSection implements BaseColumns{
		public static final String TABLE_NAME = "tbGuideSection";
		
		public static final String COLUMN_NAME = "Name";
		
		public static final String FIELD_NAME = COLUMN_NAME;
	}
	
	public static abstract class GuideElement implements BaseColumns{
		public static final String TABLE_NAME = "tbGuideElement";
		
		public static final String COLUMN_GUIDE_ELEMENT_ID = "GuideElementId";
		public static final String COLUMN_GUIDE_SECTION_ID = "GuideSectionId";
		public static final String COLUMN_NAME = "Name";
		public static final String COLUMN_SECUENCIAL = "Secuencial";
		public static final String COLUMN_HAS_DETAIL_INFO = "HasDetailInfo";		
				
		public static final String FIELD_GUIDE_ELEMENT_ID = COLUMN_GUIDE_ELEMENT_ID;
		public static final String FIELD_GUIDE_SECTION_ID = COLUMN_GUIDE_SECTION_ID;
		public static final String FIELD_NAME = COLUMN_NAME;
		public static final String FIELD_SECUENCIAL = COLUMN_SECUENCIAL;
		public static final String FIELD_HAS_DETAIL_INFO = COLUMN_HAS_DETAIL_INFO;
	}
	
	public static abstract class Client implements BaseColumns{
		public static final String TABLE_NAME = "tbClient";
		
		public static final String COLUMN_DOCUMENT_ID = "DocumentId";
		public static final String COLUMN_FIRST_NAME = "FirstName";
		public static final String COLUMN_MIDDLE_NAME = "MiddleName";
		public static final String COLUMN_LAST_NAME = "LastName";
		public static final String COLUMN_SECOND_LAST_NAME = "SecondLastName";
		public static final String COLUMN_CELLPHONE = "CellPhone";
		public static final String COLUMN_PHONENUMBER = "PhoneNumber";
		public static final String COLUMN_ADDRESS = "Address";
		public static final String COLUMN_BIRTHDAY = "Birthday";		
		public static final String COLUMN_EMAIL = "Email";
		public static final String COLUMN_GEOPOLITICAL_ID = "GeopoliticalId";		
		public static final String COLUMN_HAS_FINANCIAL_PRODUCT = "HasFinancialProduct";
		public static final String COLUMN_FINANCIAL_PRODUCT_MESSAGE = "FinancialProductMessage";		
		public static final String COLUMN_HAS_FINANCIAL_PRODUCT_ALERT = "HasFinancialProductAlert";
		public static final String COLUMN_FINANCIAL_PRODUCT_ALERT_MESSAGE = "FinancialProductAlertMessage";		
		public static final String COLUMN_INTERNAL_PROCESS_ID = "InternalProcessId";		   
		public static final String COLUMN_PHONENUMBER2 = "PhoneNumber2";
		public static final String COLUMN_CELLPHONE2 = "CellPhone2";
		public static final String COLUMN_ROADTYPE_ID = "RoadTypeId";
		public static final String COLUMN_ROADTYPE_NAME = "RoadTypeName";
		public static final String COLUMN_ZONETYPE_ID = "ZoneTypeId";
		public static final String COLUMN_ZONETYPE_NAME = "ZoneTypeName";
		public static final String COLUMN_LIVING_SITUATION = "LivingSituation";
		public static final String COLUMN_PROFILE_ID = "ProfileId";		
		public static final String COLUMN_ADDRESS_SEGMENT1 = "AddressSegment1";
		public static final String COLUMN_ADDRESS_SEGMENT2 = "AddressSegment2";
		public static final String COLUMN_ADDRESS_SEGMENT3 = "AddressSegment3";
		public static final String COLUMN_ADDRESS_SEGMENT4 = "AddressSegment4";
		public static final String COLUMN_ADDRESS_SEGMENT5 = "AddressSegment5";
		public static final String COLUMN_ADDRESS_SEGMENT6 = "AddressSegment6";
		public static final String COLUMN_ADDRESS_SEGMENT7 = "AddressSegment7";
		public static final String COLUMN_ADDRESS_SEGMENT8 = "AddressSegment8";
		public static final String COLUMN_ADDRESS_SEGMENT9 = "AddressSegment9";
		public static final String COLUMN_ADDRESS_REFERENCE = "AddressReference";		
		public static final String COLUMN_AMOUNT_FINANCIAL_PRODUCT = "AmountFinancialProduct";
		public static final String COLUMN_VALIDITY_FINANCIAL_PRODUCT_DATE= "ValidityFinancialProductDate";
		public static final String COLUMN_APPLY_REPAIR_STRATEGY = "ApplyRepairStrategy";
		public static final String COLUMN_REPAIR_STRATEGY_TYPE = "RepairStrategyType";
		public static final String COLUMN_REPAIR_STRATEGY_ID = "RepairStrategyId";
		public static final String COLUMN_FINANCIAL_PRODUCT_REASON_NOT_INTEREST = "FinancialProductReasonOfNotInterest";
		
		public static final String FIELD_DOCUMENT_ID = COLUMN_DOCUMENT_ID;
		public static final String FIELD_FIRST_NAME = COLUMN_FIRST_NAME;
		public static final String FIELD_MIDDLE_NAME = COLUMN_MIDDLE_NAME;
		public static final String FIELD_LAST_NAME = COLUMN_LAST_NAME;
		public static final String FIELD_SECOND_LAST_NAME = COLUMN_SECOND_LAST_NAME;
		public static final String FIELD_CELLPHONE = COLUMN_CELLPHONE;
		public static final String FIELD_PHONENUMBER = COLUMN_PHONENUMBER;
		public static final String FIELD_ADDRESS = COLUMN_ADDRESS;
		public static final String FIELD_BIRTHDAY = COLUMN_BIRTHDAY;		
		public static final String FIELD_EMAIL = COLUMN_EMAIL;
		public static final String FIELD_GEOPOLITICAL_ID = COLUMN_GEOPOLITICAL_ID;		
		public static final String FIELD_HAS_FINANCIAL_PRODUCT = COLUMN_HAS_FINANCIAL_PRODUCT;
		public static final String FIELD_FINANCIAL_PRODUCT_MESSAGE = COLUMN_FINANCIAL_PRODUCT_MESSAGE;
		public static final String FIELD_HAS_FINANCIAL_PRODUCT_ALERT = COLUMN_HAS_FINANCIAL_PRODUCT_ALERT;
		public static final String FIELD_FINANCIAL_PRODUCT_ALERT_MESSAGE = COLUMN_FINANCIAL_PRODUCT_ALERT_MESSAGE;		
		public static final String FIELD_INTERNAL_PROCESS_ID = COLUMN_INTERNAL_PROCESS_ID;
		
		public static final String FIELD_PHONENUMBER2 = COLUMN_PHONENUMBER2;
		public static final String FIELD_CELLPHONE2 = COLUMN_CELLPHONE2;
		public static final String FIELD_ROADTYPE_ID = COLUMN_ROADTYPE_ID;
		public static final String FIELD_ROADTYPE_NAME = COLUMN_ROADTYPE_NAME;
		public static final String FIELD_ZONETYPE_ID = COLUMN_ZONETYPE_ID;
		public static final String FIELD_ZONETYPE_NAME = COLUMN_ZONETYPE_NAME;
		public static final String FIELD_LIVING_SITUATION = COLUMN_LIVING_SITUATION;
		public static final String FIELD_PROFILE_ID = COLUMN_PROFILE_ID;		
		public static final String FIELD_ADDRESS_SEGMENT1 = COLUMN_ADDRESS_SEGMENT1;
		public static final String FIELD_ADDRESS_SEGMENT2 = COLUMN_ADDRESS_SEGMENT2;
		public static final String FIELD_ADDRESS_SEGMENT3 = COLUMN_ADDRESS_SEGMENT3;
		public static final String FIELD_ADDRESS_SEGMENT4 = COLUMN_ADDRESS_SEGMENT4;
		public static final String FIELD_ADDRESS_SEGMENT5 = COLUMN_ADDRESS_SEGMENT5;
		public static final String FIELD_ADDRESS_SEGMENT6 = COLUMN_ADDRESS_SEGMENT6;
		public static final String FIELD_ADDRESS_SEGMENT7 = COLUMN_ADDRESS_SEGMENT7;
		public static final String FIELD_ADDRESS_SEGMENT8 = COLUMN_ADDRESS_SEGMENT8;
		public static final String FIELD_ADDRESS_SEGMENT9 = COLUMN_ADDRESS_SEGMENT9;
		public static final String FIELD_ADDRESS_REFERENCE = COLUMN_ADDRESS_REFERENCE;		
		public static final String FIELD_AMOUNT_FINANCIAL_PRODUCT = COLUMN_AMOUNT_FINANCIAL_PRODUCT;
		public static final String FIELD_VALIDITY_FINANCIAL_PRODUCT_DATE = COLUMN_VALIDITY_FINANCIAL_PRODUCT_DATE;
		public static final String FIELD_APPLY_REPAIR_STRATEGY = COLUMN_APPLY_REPAIR_STRATEGY;
		public static final String FIELD_REPAIR_STRATEGY_TYPE = COLUMN_REPAIR_STRATEGY_TYPE;
		public static final String FIELD_REPAIR_STRATEGY_ID = COLUMN_REPAIR_STRATEGY_ID;
		public static final String FIELD_FINANCIAL_PRODUCT_REASON_NOT_INTEREST = COLUMN_FINANCIAL_PRODUCT_REASON_NOT_INTEREST;
	}
	
	public static abstract class VendorReport implements BaseColumns{
		public static final String TABLE_NAME = "tbVendorReport";
						
		public static final String COLUMN_RECORD_MODE = "RecordMode";
		public static final String COLUMN_GOAL_VALUE = "GoalValue";
		public static final String COLUMN_REAL_VALUE = "RealValue";
		public static final String COLUMN_USER = "User";
		public static final String COLUMN_NAME = "Name";
		public static final String COLUMN_PERCENT = "Percent";
		public static final String COLUMN_GOAL_TYPE = "GoalType";
		public static final String COLUMN_DAY = "Day";
		public static final String COLUMN_MONTH = "Month";
		public static final String COLUMN_YEAR = "Year";
				
		public static final String DISPLAY_RECORD_MODE = COLUMN_RECORD_MODE;
		public static final String DISPLAY_GOAL_VALUE = COLUMN_GOAL_VALUE;
		public static final String DISPLAY_REAL_VALUE = COLUMN_REAL_VALUE;
		public static final String DISPLAY_USER = COLUMN_USER;
		public static final String DISPLAY_NAME = COLUMN_NAME;
		public static final String DISPLAY_PERCENT = COLUMN_PERCENT;
		public static final String DISPLAY_GOAL_TYPE = COLUMN_GOAL_TYPE;
		public static final String DISPLAY_DAY = COLUMN_DAY;
		public static final String DISPLAY_MONTH = COLUMN_MONTH;
		public static final String DISPLAY_YEAR = COLUMN_YEAR;
	}
}
