package pe.com.maestro.commercial.models;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import pe.com.maestro.commercial.db.Contract;
import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.util.CursorUtils;

public class VendorReport extends EntityBase<VendorReport> {
	
	public static final int RECORD_MODE_DAILY = 1;
	public static final int RECORD_MODE_MONTH = 2;
	
	public static final int GOAL_TYPE_PRIMARY = 1;
	public static final int GOAL_TYPE_SECONDARY = 2;
	
	private long id;
	private String user;
	private String name;
	private int day;
	private double goalValue;
	private double realValue;
	private double percent;
	private int recordMode;
	private int goalType;
	private int month;
	private int year;
	
	@Override
	public long getID() {		
		return id;
	}
	@Override
	public void setID(long id) {
		this.id = id;
	}
	@Override
	public boolean isAutoGeneratedId() {		
		return true;
	}
	@Override
	public String getTableName() {		
		return Contract.VendorReport.TABLE_NAME;
	}
	@Override
	public void setValues() {
		setValue(Contract.VendorReport.COLUMN_GOAL_TYPE, goalType);
		setValue(Contract.VendorReport.COLUMN_GOAL_VALUE, goalValue);
		setValue(Contract.VendorReport.COLUMN_NAME, name);
		setValue(Contract.VendorReport.COLUMN_PERCENT, percent);
		setValue(Contract.VendorReport.COLUMN_REAL_VALUE, realValue);
		setValue(Contract.VendorReport.COLUMN_DAY, day);
		setValue(Contract.VendorReport.COLUMN_MONTH, month);
		setValue(Contract.VendorReport.COLUMN_YEAR, year);
		setValue(Contract.VendorReport.COLUMN_RECORD_MODE, recordMode);
		setValue(Contract.VendorReport.COLUMN_USER, user);
	}
	
	@Override
	public Object getValue(String key) {
		return null;
	}
	@Override
	public String getDescription() {		
		return this.name;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public double getGoalValue() {
		return goalValue;
	}
	public void setGoalValue(double goalValue) {
		this.goalValue = goalValue;
	}
	public double getRealValue() {
		return realValue;
	}
	public void setRealValue(double realValue) {
		this.realValue = realValue;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public int getRecordMode() {
		return recordMode;
	}
	public void setRecordMode(int recordMode) {
		this.recordMode = recordMode;
	}
	public int getGoalType() {
		return goalType;
	}
	public void setGoalType(int goalType) {
		this.goalType = goalType;
	}	
	
	public static List<VendorReport> getVendorReport(DataBase db, int recordMode){
		List<VendorReport> report = new ArrayList<VendorReport>();
	
		Cursor c = db.query(Contract.VendorReport.TABLE_NAME, new String[] 
				{
					Contract.VendorReport._ID,					
					Contract.VendorReport.COLUMN_GOAL_VALUE,
					Contract.VendorReport.COLUMN_NAME,
					Contract.VendorReport.COLUMN_PERCENT,
					Contract.VendorReport.COLUMN_REAL_VALUE,
					Contract.VendorReport.COLUMN_DAY,
					Contract.VendorReport.COLUMN_MONTH,
					Contract.VendorReport.COLUMN_YEAR,					
					Contract.VendorReport.COLUMN_RECORD_MODE,
					Contract.VendorReport.COLUMN_USER,
					Contract.VendorReport.COLUMN_GOAL_TYPE,
				}, Contract.VendorReport.COLUMN_RECORD_MODE + " = ?", 
					new String[] { String.valueOf(recordMode) }, null, null, Contract.VendorReport.COLUMN_MONTH	+ " DESC, " + Contract.VendorReport.COLUMN_DAY + " DESC"
				);
		
		if(c.moveToFirst()){
			do{
				VendorReport r = new VendorReport();
				r.setGoalType( CursorUtils.getInt(c, Contract.VendorReport.COLUMN_GOAL_TYPE ) );
				r.setGoalValue( CursorUtils.getDouble(c, Contract.VendorReport.COLUMN_GOAL_VALUE ) );
				r.setName( CursorUtils.getString(c, Contract.VendorReport.COLUMN_NAME ) );
				r.setPercent( CursorUtils.getDouble(c, Contract.VendorReport.COLUMN_PERCENT ) );
				r.setRealValue( CursorUtils.getDouble(c, Contract.VendorReport.COLUMN_REAL_VALUE ) );
				r.setDay( CursorUtils.getInt(c, Contract.VendorReport.COLUMN_DAY ) );
				r.setMonth( CursorUtils.getInt(c, Contract.VendorReport.COLUMN_MONTH ) );
				r.setYear( CursorUtils.getInt(c, Contract.VendorReport.COLUMN_YEAR ) );
				r.setRecordMode( CursorUtils.getInt(c, Contract.VendorReport.COLUMN_RECORD_MODE ) );
				r.setUser( CursorUtils.getString(c, Contract.VendorReport.COLUMN_USER ) );
				r.setID( CursorUtils.getLong(c, Contract.VendorReport._ID ) );
				report.add(r);
			}while(c.moveToNext());
		}
		
		return report;
		
	}
}