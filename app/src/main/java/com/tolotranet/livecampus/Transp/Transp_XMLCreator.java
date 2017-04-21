package com.tolotranet.livecampus.Transp;


import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Transp_XMLCreator {

	public static String CreateSpreadSheetToXML() throws ParserConfigurationException, TransformerException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Contacts");
		doc.appendChild(rootElement);
//Log.d("hello", "this is my array"+ Arrays.toString(Transp_XMLParserClass.RouteArray.toArray()) );

		for (int i = 0; i < Transp_XMLParserClass.TimeStamp.size(); i++) {
			// PersonElement elements
			Element PersonElement = doc.createElement("Person");
			rootElement.appendChild(PersonElement);

			Element s_no = doc.createElement(Transp_XMLParserClass.Tag_Array.get(0));
			s_no.appendChild(doc.createTextNode(""+i));
			PersonElement.appendChild(s_no);

			Element Route = doc.createElement("route");
			Route.appendChild(doc.createTextNode(Transp_XMLParserClass.RouteArray.get(i)));
			PersonElement.appendChild(Route);


			Element TIME = doc.createElement("time");
			TIME.appendChild(doc.createTextNode(Transp_XMLParserClass.TimeArray.get(i)));
			PersonElement.appendChild(TIME);

			Element Shuttle = doc.createElement("shuttle");
			Shuttle.appendChild(doc.createTextNode(Transp_XMLParserClass.ShuttleArray.get(i)));
			PersonElement.appendChild(Shuttle);

			Element O1 = doc.createElement("o1");
			O1.appendChild(doc.createTextNode(Transp_XMLParserClass.Off1_Array.get(i)));
			PersonElement.appendChild(O1);

			Element COHORT = doc.createElement("cohort");
			COHORT.appendChild(doc.createTextNode(Transp_XMLParserClass.Cohort_Array.get(i)));
			PersonElement.appendChild(COHORT);

			Element category = doc.createElement("category");
			category.appendChild(doc.createTextNode(Transp_XMLParserClass.CategoryArray.get(i)));
			PersonElement.appendChild(category);

			Element NUMBERSTUDENT = doc.createElement("numberofstudents");
			NUMBERSTUDENT.appendChild(doc.createTextNode(Transp_XMLParserClass.NumberStud_Array.get(i)));
			PersonElement.appendChild(NUMBERSTUDENT);

			Element R2 = doc.createElement("r2");
			R2.appendChild(doc.createTextNode(Transp_XMLParserClass.TimeStamp.get(i)));
			PersonElement.appendChild(R2);

			Element M1 = doc.createElement("m1");
			M1.appendChild(doc.createTextNode(Transp_XMLParserClass.Mob1_Array.get(i)));
			PersonElement.appendChild(M1);

			Element M2 = doc.createElement("m2");
			M2.appendChild(doc.createTextNode(Transp_XMLParserClass.Mob2_Array.get(i)));
			PersonElement.appendChild(M2);


			Element E1 = doc.createElement("e1");
			E1.appendChild(doc.createTextNode(Transp_XMLParserClass.Email1_Array.get(i)));
			PersonElement.appendChild(E1);

			Element E2 = doc.createElement("e2");
			E2.appendChild(doc.createTextNode(Transp_XMLParserClass.Email2_Array.get(i)));
			PersonElement.appendChild(E2);

			Element DAY = doc.createElement("day");
			DAY.appendChild(doc.createTextNode(Transp_XMLParserClass.Day_Array.get(i)));
			PersonElement.appendChild(DAY);

			Element DATE = doc.createElement("date");
			DATE.appendChild(doc.createTextNode(Transp_XMLParserClass.DateArray.get(i)));
			PersonElement.appendChild(DATE);
		}

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "2");

		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString();

		return output;
	}

}