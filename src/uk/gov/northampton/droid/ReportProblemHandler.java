package uk.gov.northampton.droid;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReportProblemHandler extends DefaultHandler{

		private StringBuffer buffer = new StringBuffer();

		private ArrayList<ReportProblem> problemList;
		private ReportProblem problem;

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			buffer.setLength(0);

			if (localName.equals("problems")) {
				problemList = new ArrayList<ReportProblem>();
			}
			else if (localName.equals("problem")) {
				problem = new ReportProblem();
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)throws SAXException {

			if (localName.equals("problem")) {
				problemList.add(problem);
			}
			else if (localName.equals("problem-description")) {
				problem.setpDesc(buffer.toString());
			}
			else if (localName.equals("problem-number")) {
				int pNum = Integer.parseInt(buffer.toString());
				problem.setpNum(pNum);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			buffer.append(ch, start, length);
		}

		public ArrayList<ReportProblem> retrieveProblemReasons() {
			return problemList;
		}

}
