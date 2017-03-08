package com.tolotranet.livecampus.Com;


import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class Com_XMLParserClass {

	public static ArrayList<String> q1;
	public static ArrayList<String> q2;
	public static ArrayList<String> q3;
	public static ArrayList<String> q4;
	public static ArrayList<String> q5;
	public static ArrayList<String> q6;
	public static ArrayList<String> q7;
	public static ArrayList<String> q8;
	public static ArrayList<String> q9;
	public static ArrayList<String> q10;
	public static ArrayList<String> q11;
	public static ArrayList<String> q12;
	public static ArrayList<String> q13;
	public static ArrayList<String> q14;
	public static ArrayList<String> Tag_Array;

	public Com_XMLParserClass() throws XmlPullParserException,
			IOException {
		// TODO Auto-generated constructor stub
		q1 = new ArrayList<String>();
		q2 = new ArrayList<String>();
		q3 = new ArrayList<String>();
		q4 = new ArrayList<String>();
		q5 = new ArrayList<String>();
		q6 = new ArrayList<String>();
		q7 = new ArrayList<String>();
		q8 = new ArrayList<String>();
		q9 = new ArrayList<String>();
		q10 = new ArrayList<String>();
		q11 = new ArrayList<String>();
		q12 = new ArrayList<String>();
		q13 = new ArrayList<String>();
		q14 = new ArrayList<String>();
//		c = context;
//		Resources res = c.getResources();
//		XmlResourceParser xrp = res.getXml(R.xml.contacts);
//
//		xrp.next(); // May throw error so add exceptions to the file
//		int eventType = xrp.getEventType();

		String xmlString = Com_FileOperations.ReadData();
		XmlPullParser xrp = Xml.newPullParser();
		xrp.setInput(new StringReader(xmlString));
		int eventType = xrp.getEventType();
		Log.d("hello", "reading complete. XML parser class tolotra kely");
		//Change the tag to small letters
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xrp.getName().equals(("q1"))) {
					q1.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q2"))) {
					q2.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q3"))) {
					q3.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q4"))) {
					q4.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q5"))) {
					q5.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q6"))) {
					q6.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q7"))) {
					q7.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q8"))) {
					q8.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q9"))) {
					q9.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q10"))) {
					q10.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q11"))) {
					q11.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q12"))) {
					q12.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q13"))) {
					q13.add(xrp.nextText());
				}
				if (xrp.getName().equals(("q14"))) {
					q14.add(xrp.nextText());
				}
			}
			eventType = xrp.next();
		}
	}
}
