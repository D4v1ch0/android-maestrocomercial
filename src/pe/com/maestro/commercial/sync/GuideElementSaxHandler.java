package pe.com.maestro.commercial.sync;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pe.com.maestro.commercial.models.GuideElement;
import rp3.db.sqlite.DataBase;
import rp3.util.BitmapUtils;
import rp3.util.Convert;
import rp3.util.FileUtils;

public class GuideElementSaxHandler extends DefaultHandler {

	private String tempVal;
	private GuideElement guideElement;

	private List<GuideElement> guideElements;
	private DataBase db;
	
	public GuideElementSaxHandler(DataBase db){
		this.db = db;
		guideElements = new ArrayList<GuideElement>();
	}


	public List<GuideElement> getGuidesElement() {
		return guideElements;
	}

	// Event Handlers
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// reset
		tempVal = "";
		if (qName.equalsIgnoreCase("RP3ElementsByGuideSection")) {			
			guideElement = new GuideElement();
			GuideElement.insert(db, guideElement);
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		tempVal += new String(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("RP3ElementsByGuideSection")) {			
			guideElements.add(guideElement);		
			GuideElement.update(db, guideElement);
		} else if (qName.equalsIgnoreCase("Ds")) {
			guideElement.setName(tempVal);
		} else if (qName.equalsIgnoreCase("IdSec")) {
			guideElement.setGuideSectionId(Long.valueOf(tempVal));
		} else if (qName.equalsIgnoreCase("Id")) {
			guideElement.setGuideElementId(Integer.valueOf(tempVal));
		} else if (qName.equalsIgnoreCase("Sec")) {
			guideElement.setSecuencial(Integer.valueOf(tempVal));
		} else if (qName.equalsIgnoreCase("ImgInfoExists")) {
			guideElement.setHasDetailInfo(Convert.getBoolean(tempVal));
		} else if(qName.equalsIgnoreCase("Img")){
			FileUtils.saveInternalStorage(guideElement.getFileName(),
					BitmapUtils.decodeBitmapFromBase64(tempVal));			
		} else if(qName.equalsIgnoreCase("ImgInfo")){
			if(guideElement.hasDetailInfo())
				FileUtils.saveInternalStorage(guideElement.getDetailFileName(), BitmapUtils.decodeBitmapFromBase64(tempVal));			
		}
	}

}
