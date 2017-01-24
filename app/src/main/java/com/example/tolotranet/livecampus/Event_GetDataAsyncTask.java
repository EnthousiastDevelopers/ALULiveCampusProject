package com.example.tolotranet.livecampus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Event_GetDataAsyncTask extends AsyncTask<Activity, Void, Void> {

	Activity myActivity;
	@Override
	protected Void doInBackground(Activity... arg0) {
		// TODO Auto-generated method stub
		myActivity = arg0[0];
		
		try {
			getData();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String xmlString = Event_XMLCreator.CreateSpreadSheetToXML();
			Event_FileOperations.StoreData(xmlString);
//			Log.d("hello",xmlString);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		Intent i = new Intent(myActivity.getApplicationContext(),Event_MainList.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		myActivity.startActivity(i);
		myActivity.overridePendingTransition(0, 0);
		myActivity.finish();
	}
	
	private void getData() throws AuthenticationException,
			MalformedURLException, IOException, ServiceException,
			URISyntaxException {
		// TODO Auto-generated method stub
		SpreadsheetService service = new SpreadsheetService(
				"MySpreadsheetIntegration-v1");
		service.setProtocolVersion(SpreadsheetService.Versions.V3);

		URL SPREADSHEET_URL = new URL(
				"https://spreadsheets.google.com/feeds/worksheets/1JjthlFPa56vAmHch1vNrDV6JnK3HtEpnTTaKSmClASk/public/full");

		WorksheetFeed feed = service.getFeed(SPREADSHEET_URL,
				WorksheetFeed.class);
		List<WorksheetEntry> worksheets = feed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);
		Log.d("hello", "Worksheet name is "
				+ worksheet.getTitle().getPlainText());

		// URL listFeedUrl = worksheet.getListFeedUrl();
		URL listFeedUrl = new URI(worksheet.getListFeedUrl().toString())
				.toURL();

		Log.d("hello", "URL is \n " + listFeedUrl.toString());

		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

		ArrayList<String> q1 = new ArrayList<String>();
		ArrayList<String> q2 = new ArrayList<String>();
		ArrayList<String> q3 = new ArrayList<String>();
		ArrayList<String> q4 = new ArrayList<String>();
		ArrayList<String> q5 = new ArrayList<String>();
		ArrayList<String> q6 = new ArrayList<String>();
		ArrayList<String> q7 = new ArrayList<String>();
		ArrayList<String> q8 = new ArrayList<String>();
		ArrayList<String> q9 = new ArrayList<String>();
		ArrayList<String> q10 = new ArrayList<String>();
		ArrayList<String> q11 = new ArrayList<String>();
		ArrayList<String> q12 = new ArrayList<String>();
		ArrayList<String> q13 = new ArrayList<String>();
		ArrayList<String> q14 = new ArrayList<String>();
		ArrayList<String> Tag_Array = new ArrayList<String>();



		Calendar calendar1 = Calendar.getInstance();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/m/yyyy h:mm:ss");
		String currentDate = formatter1.format(calendar1.getTime());
		String datadb;

		ListEntry TempList = listFeed.getEntries().get(0);
		for(String tag:TempList.getCustomElements().getTags()){
			Tag_Array.add(tag);
		}
		
		for (ListEntry row : listFeed.getEntries()) {
			for (String tag : row.getCustomElements().getTags()) {

				datadb = row.getCustomElements().getValue("timestamp");
//				if(1==1){
				if(currentDate.compareTo(datadb)<=0) {
					Log.d("datefresh","this timestamp was on: " + datadb+" and the current app_event is:" + currentDate);


					if (tag.equals("userid")) {
						q1.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("event")) {
						q2.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("place")) {
						q3.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("date")) {
						q4.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("description")) {
						q5.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("starttime")) {
						q6.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("endtime")) {
						q7.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("organiser")) {
						q8.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("contactinfo")) {
						q9.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("phonenumber2")) {
						q10.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("phonenumber3")) {
						q11.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("apartment")) {
						q12.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("major")) {
						q13.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("room")) {
						q14.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
				}
			}
			
		}	
		
		Event_XMLParserClass.q1 = q1;
		Event_XMLParserClass.q2 = q2;
		Event_XMLParserClass.q3 = q3;
		Event_XMLParserClass.q4 = q4;
		Event_XMLParserClass.q5 = q5;
		Event_XMLParserClass.q6 = q6;
		Event_XMLParserClass.q7 = q7;
		Event_XMLParserClass.q8 = q8;
		Event_XMLParserClass.q9 = q9;
		Event_XMLParserClass.q10 = q10;
		Event_XMLParserClass.q11 = q11;
		Event_XMLParserClass.q12 = q12;
		Event_XMLParserClass.q13 = q13;
		Event_XMLParserClass.q14 = q14;
		Event_XMLParserClass.Tag_Array = Tag_Array;

	}

	public String ProperValue(String input) {
		if (input == null) {
			return new String("");
		} else {
			return input;
		}

	}

}
