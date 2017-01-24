package com.example.tolotranet.livecampus;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class Transp_XMLParserClass {

	public static ArrayList<String> CategoryArray;
	public static ArrayList<String> ShuttleArray ;
	public static ArrayList<String> RouteArray;
	public static ArrayList<String> Off1_Array;
	public static ArrayList<String> Cohort_Array;
	public static ArrayList<String> TimeArray;
	public static ArrayList<String> NumberStud_Array;
	public static ArrayList<String> Res2_Array;
	public static ArrayList<String> Mob1_Array;
	public static ArrayList<String> Mob2_Array;
	public static ArrayList<String> Email1_Array;
	public static ArrayList<String> Email2_Array;
	public static ArrayList<String> Tag_Array;
	public static ArrayList<String> Day_Array;


	public Transp_XMLParserClass() throws XmlPullParserException,
			IOException {
		// TODO Auto-generated constructor stub
		CategoryArray = new ArrayList<String>();
		ShuttleArray = new ArrayList<String>();
		Email1_Array = new ArrayList<String>();
		Email2_Array = new ArrayList<String>();
		RouteArray = new ArrayList<String>();
		Off1_Array = new ArrayList<String>();
		Cohort_Array = new ArrayList<String>();
		TimeArray = new ArrayList<String>();
		NumberStud_Array = new ArrayList<String>();
		Res2_Array = new ArrayList<String>();
		Mob1_Array = new ArrayList<String>();
		Mob2_Array = new ArrayList<String>();
		Day_Array = new ArrayList<String>();
//		c = context;
//		Resources res = c.getResources();
//		XmlResourceParser xrp = res.getXml(R.xml.contacts);
//
//		xrp.next(); // May throw error so add exceptions to the file
//		int eventType = xrp.getEventType();

		String xmlString = Transp_FileOperations.ReadData();
		XmlPullParser xrp = Xml.newPullParser();
		xrp.setInput(new StringReader(xmlString));
		int eventType = xrp.getEventType();
		Log.d("hello", "reading complete.");

		//Change the tag to small letters
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xrp.getName().equals(("route"))) {
					RouteArray.add(xrp.nextText());
				}
				if (xrp.getName().equals(("time"))) {
					TimeArray.add(xrp.nextText());
				}
				if (xrp.getName().equals(("category"))) {
					CategoryArray.add(xrp.nextText());
				}
				if (xrp.getName().equals(("shuttle"))) {
					ShuttleArray.add(xrp.nextText());
				}
				if (xrp.getName().equals(("o1"))) {
					Off1_Array.add(xrp.nextText());
				}
				if (xrp.getName().equals(("cohort"))) {
					Cohort_Array.add(xrp.nextText());
				}

				if (xrp.getName().equals(("numberofstudents"))) {
					NumberStud_Array.add(xrp.nextText());
				}
				if (xrp.getName().equals(("r2"))) {
					Res2_Array.add(xrp.nextText());
				}
				if (xrp.getName().equals(("m1"))) {
					Mob1_Array.add(xrp.nextText());
				}
				if (xrp.getName().equals(("m2"))) {
					Mob2_Array.add(xrp.nextText());
				}
				if (xrp.getName().equals(("e1"))) {
					Email1_Array.add(xrp.nextText());
				}
				if (xrp.getName().equals(("e2"))) {
					Email2_Array.add(xrp.nextText());
				}
				if (xrp.getName().equals(("day"))) {
					Day_Array.add(xrp.nextText());
				}
			}
			eventType = xrp.next();
		}
	}
}
