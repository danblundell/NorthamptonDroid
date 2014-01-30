package uk.gov.northampton.droid;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfirmationHandler extends DefaultHandler{

		private StringBuffer buffer = new StringBuffer();
		private Confirmation conf;

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			buffer.setLength(0);

			if (localName.equals("response")) {
				conf = new Confirmation();
				conf.setSuccess(false);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)throws SAXException {

			if (localName.equals("result")) {
				boolean success = false;
				if(buffer.toString().equalsIgnoreCase("success")){
					success = true;
				}
				conf.setSuccess(success);
			}
			else if (localName.equals("callNumber")) {
				conf.setCallNumber(buffer.toString());
			}
			else if (localName.equals("slaDate")) {
				conf.setSla(buffer.toString());
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			buffer.append(ch, start, length);
		}

		public Confirmation retrieveConfirmation() {
			return conf;
		}

}
