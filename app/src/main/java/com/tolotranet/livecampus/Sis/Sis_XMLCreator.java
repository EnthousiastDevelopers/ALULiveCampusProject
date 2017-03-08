package com.tolotranet.livecampus.Sis;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

public class Sis_XMLCreator {

	public static String CreateSpreadSheetToXML() throws ParserConfigurationException, TransformerException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Element");
		doc.appendChild(rootElement);

		for (int i = 0; i < Sis_XMLParserClass.q1.size(); i++) {
			// PersonElement elements
			Element ItemElement = doc.createElement("item");
			rootElement.appendChild(ItemElement);

			Element q1 = doc.createElement("q1");
			q1.appendChild(doc.createTextNode(Sis_XMLParserClass.q1.get(i)));
			ItemElement.appendChild(q1);

			Element q2 = doc.createElement("q2");
			q2.appendChild(doc.createTextNode(Sis_XMLParserClass.q2.get(i)));
			ItemElement.appendChild(q2);


			Element q3 = doc.createElement("q3");
			q3.appendChild(doc.createTextNode(Sis_XMLParserClass.q3.get(i)));
			ItemElement.appendChild(q3);

			Element q4 = doc.createElement("q4");
			q4.appendChild(doc.createTextNode(Sis_XMLParserClass.q4.get(i)));
			ItemElement.appendChild(q4);

			Element q5 = doc.createElement("q5");
			q5.appendChild(doc.createTextNode(Sis_XMLParserClass.q5.get(i)));
			ItemElement.appendChild(q5);

			Element q6 = doc.createElement("q6");
			q6.appendChild(doc.createTextNode(Sis_XMLParserClass.q6.get(i)));
			ItemElement.appendChild(q6);

			Element q7 = doc.createElement("q7");
			q7.appendChild(doc.createTextNode(Sis_XMLParserClass.q7.get(i)));
			ItemElement.appendChild(q7);

			Element q8 = doc.createElement("q8");
			q8.appendChild(doc.createTextNode(Sis_XMLParserClass.q8.get(i)));
			ItemElement.appendChild(q8);

			Element q9 = doc.createElement("q9");
			q9.appendChild(doc.createTextNode(Sis_XMLParserClass.q9.get(i)));
			ItemElement.appendChild(q9);

			Element q10 = doc.createElement("q10");
			q10.appendChild(doc.createTextNode(Sis_XMLParserClass.q10.get(i)));
			ItemElement.appendChild(q10);

			Element q11 = doc.createElement("q11");
			q11.appendChild(doc.createTextNode(Sis_XMLParserClass.q11.get(i)));
			ItemElement.appendChild(q11);


			Element q12 = doc.createElement("q12");
			q12.appendChild(doc.createTextNode(Sis_XMLParserClass.q12.get(i)));
			ItemElement.appendChild(q12);

			Element q13 = doc.createElement("q13");
			q13.appendChild(doc.createTextNode(Sis_XMLParserClass.q13.get(i)));
			ItemElement.appendChild(q13);

			Element q14 = doc.createElement("q14");
			q14.appendChild(doc.createTextNode(Sis_XMLParserClass.q14.get(i)));
			ItemElement.appendChild(q14);

			Element q15 = doc.createElement("q15");
			q15.appendChild(doc.createTextNode(Sis_XMLParserClass.q15.get(i)));
			ItemElement.appendChild(q15);

			Element q16 = doc.createElement("q16");
			q16.appendChild(doc.createTextNode(Sis_XMLParserClass.q16.get(i)));
			ItemElement.appendChild(q16);

			Element q17 = doc.createElement("q17");
			q17.appendChild(doc.createTextNode(Sis_XMLParserClass.q17.get(i)));
			ItemElement.appendChild(q17);

			Element q18 = doc.createElement("q18");
			q18.appendChild(doc.createTextNode(Sis_XMLParserClass.q18.get(i)));
			ItemElement.appendChild(q18);
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