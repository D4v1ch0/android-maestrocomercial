package pe.com.maestro.commercial.models;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import pe.com.maestro.commercial.db.Contract;
import rp3.db.sqlite.DataBase;
import rp3.util.CursorUtils;

public class GuideElement extends rp3.data.entity.EntityBase<GuideElement> {

	private long id;
	private int guideElementId;
	private String name;
	private int secuencial;
	private long guideSectionId;	
	private boolean hasDetailInfo;	

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
		return Contract.GuideElement.TABLE_NAME;
	}

	@Override
	public void setValues() {
		setValue(Contract.GuideElement.COLUMN_GUIDE_ELEMENT_ID, guideElementId);
		setValue(Contract.GuideElement.COLUMN_GUIDE_SECTION_ID, guideSectionId);
		setValue(Contract.GuideElement.COLUMN_NAME, name);
		setValue(Contract.GuideElement.COLUMN_SECUENCIAL, secuencial);
		setValue(Contract.GuideElement.COLUMN_HAS_DETAIL_INFO, hasDetailInfo);
	}

	@Override
	public Object getValue(String key) {	
		return null;
	}

	@Override
	public String getDescription() {		
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGuideElementId() {
		return guideElementId;
	}

	public void setGuideElementId(int guideElementId) {
		this.guideElementId = guideElementId;
	}
	
	public int getSecuencial() {
		return secuencial;
	}

	public void setSecuencial(int secuencial) {
		this.secuencial = secuencial;
	}

	public long getGuideSectionId() {
		return guideSectionId;
	}

	public void setGuideSectionId(long guideSectionId) {
		this.guideSectionId = guideSectionId;
	}

	public boolean hasDetailInfo() {
		return hasDetailInfo;
	}

	public void setHasDetailInfo(boolean hasDetailInfo) {
		this.hasDetailInfo = hasDetailInfo;
	}
	
	public String getFileName(){
		return "S" + String.valueOf(this.id);
	}
	
	public String getDetailFileName(){
		return "S_I" + String.valueOf(this.id);
	}
	
	
	
	public static List<GuideElement> getHeadersGuideElements(DataBase db){
		List<GuideElement> elements = new ArrayList<GuideElement>();		
		
		Cursor c = db.query(true, Contract.GuideElement.TABLE_NAME, 
				new String[] {						
						Contract.GuideElement.COLUMN_GUIDE_ELEMENT_ID,
						Contract.GuideElement.COLUMN_GUIDE_SECTION_ID,
						Contract.GuideElement.COLUMN_NAME
					},null, null,
				null,
				null,
				Contract.GuideElement.COLUMN_SECUENCIAL
				);
		
		if(c.moveToFirst()){
			do{
				GuideElement element = new GuideElement();
				element = new GuideElement();				
				element.setGuideElementId( CursorUtils.getInt(c, Contract.GuideElement.FIELD_GUIDE_ELEMENT_ID) );
				element.setGuideSectionId( CursorUtils.getLong(c, Contract.GuideElement.FIELD_GUIDE_SECTION_ID) );
				element.setName( CursorUtils.getString(c, Contract.GuideElement.FIELD_NAME) );				
				
				elements.add(element);
				
			}while(c.moveToNext());
		}
		
		return elements;
	}
	
	public static List<GuideElement> getGuideElements(DataBase db, Long sectionId, Integer elementId){
		List<GuideElement> elements = new ArrayList<GuideElement>();
		String where = null;
		String[] args = null;
		
		if(sectionId != null && elementId != null){
			where = Contract.GuideElement.COLUMN_GUIDE_SECTION_ID + " = ? AND " +  Contract.GuideElement.COLUMN_GUIDE_ELEMENT_ID + " = ?";
			args = new String[] { String.valueOf(sectionId), String.valueOf(elementId) };
		}				
		
		Cursor c = db.query(Contract.GuideElement.TABLE_NAME, 
				new String[] {
						Contract.GuideElement._ID,
						Contract.GuideElement.COLUMN_GUIDE_ELEMENT_ID,
						Contract.GuideElement.COLUMN_GUIDE_SECTION_ID,
						Contract.GuideElement.COLUMN_NAME,
						Contract.GuideElement.COLUMN_HAS_DETAIL_INFO,
						Contract.GuideElement.COLUMN_SECUENCIAL
					},where, args,				
				null,
				null,
				Contract.GuideElement.COLUMN_SECUENCIAL				
				);
		
		if(c.moveToFirst()){
			do{
				GuideElement element = new GuideElement();
				element = new GuideElement();
				element.setGuideElementId( CursorUtils.getInt(c, Contract.GuideElement.FIELD_GUIDE_ELEMENT_ID) );
				element.setSecuencial( CursorUtils.getInt(c, Contract.GuideElement.FIELD_SECUENCIAL) );
				element.setGuideSectionId( CursorUtils.getLong(c, Contract.GuideElement.FIELD_GUIDE_SECTION_ID) );
				element.setName( CursorUtils.getString(c, Contract.GuideElement.FIELD_NAME) );
				element.setHasDetailInfo( CursorUtils.getBoolean(c, Contract.GuideElement.FIELD_HAS_DETAIL_INFO) );
				element.setID(CursorUtils.getLong(c, Contract.GuideElement._ID));
				
				elements.add(element);
				
			}while(c.moveToNext());
		}
		
		return elements;
	}
	
	public static GuideElement getById(DataBase db, long id){
		GuideElement element = null;
		Cursor c = db.query(Contract.GuideElement.TABLE_NAME, 
				new String[] {
						Contract.GuideElement.COLUMN_GUIDE_ELEMENT_ID,
						Contract.GuideElement.COLUMN_GUIDE_SECTION_ID,
						Contract.GuideElement.COLUMN_NAME,
						Contract.GuideElement.COLUMN_HAS_DETAIL_INFO,
						Contract.GuideElement.COLUMN_SECUENCIAL
					},
				Contract.GuideElement._ID + " = ? ",
				id
				);
		
		if(c.moveToFirst()){			
			element = new GuideElement();
			element.setGuideElementId( CursorUtils.getInt(c, Contract.GuideElement.FIELD_GUIDE_ELEMENT_ID) );
			element.setSecuencial( CursorUtils.getInt(c, Contract.GuideElement.FIELD_SECUENCIAL) );
			element.setGuideSectionId( CursorUtils.getLong(c, Contract.GuideElement.FIELD_GUIDE_SECTION_ID) );
			element.setName( CursorUtils.getString(c, Contract.GuideElement.FIELD_NAME) );
			element.setHasDetailInfo( CursorUtils.getBoolean(c, Contract.GuideElement.FIELD_HAS_DETAIL_INFO) );
			element.setID(id);
		}
		return element;
	}
}
