package uk.gov.northampton.droid;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PropertyHandler extends DefaultHandler{

		private StringBuffer buffer = new StringBuffer();

		private ArrayList<Property> propertyList;
		private Property p;

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			buffer.setLength(0);

			if (localName.equals("properties")) {
				propertyList = new ArrayList<Property>();
			}
			else if (localName.equals("property")) {
				p = new Property();
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName)throws SAXException {

			if (localName.equals("property")) {
				propertyList.add(p);
			}
			else if (localName.equals("address")) {
				p.setAddress(buffer.toString());
			}
			else if (localName.equals("day")) {
				p.setBinCollectionDay(buffer.toString());
			}
			else if (localName.equals("date")) {
				p.setBinCollectionDate(buffer.toString());
			}
			else if (localName.equals("type")) {
				p.setBinCollectionType(buffer.toString());
			}	
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			buffer.append(ch, start, length);
		}

		public ArrayList<Property> retrievePropertyList() {
			return propertyList;
		}

}
