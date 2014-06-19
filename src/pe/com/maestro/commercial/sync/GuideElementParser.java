package pe.com.maestro.commercial.sync;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

import pe.com.maestro.commercial.models.GuideElement;
import rp3.db.sqlite.DataBase;

public class GuideElementParser {
	public static List<GuideElement> parse(InputStream is, DataBase db) {
		List<GuideElement> data = null;
		try {
			// create a XMLReader from SAXParser
			XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
					.getXMLReader();
			// create a SAXXMLHandler
			GuideElementSaxHandler saxHandler = new GuideElementSaxHandler(db);
			// store handler in XMLReader
			xmlReader.setContentHandler(saxHandler);
			// the process starts
			xmlReader.parse(new InputSource(is));
			// get the `Employee list`
			data = saxHandler.getGuidesElement();

		} catch (Exception ex) {
			Log.d("XML", "SAXXMLParser: parse() failed" + ex.getMessage());
		}

		// return Employee list
		return data;
	}
}
