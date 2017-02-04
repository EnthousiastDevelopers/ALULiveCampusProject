package com.example.tolotranet.livecampus;

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
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ServiceException;

public class Transp_GetDataAsyncTask extends AsyncTask<Activity, Void, Void> {

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
			String xmlString = Transp_XMLCreator.CreateSpreadSheetToXML();
			Transp_FileOperations.StoreData(xmlString);
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
		Intent i = new Intent(myActivity.getApplicationContext(),Transp_TransApp.class);
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
				"https://spreadsheets.google.com/feeds/worksheets/1ZNbR2eECA3PX9RSfFw7R0H50JpALJC50CQLSvvS-ySw/public/full");

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

		ArrayList<String> CategoryArray = new ArrayList<String>();
		ArrayList<String> ShuttleArray = new ArrayList<String>();
		ArrayList<String> Email1_Array = new ArrayList<String>();
		ArrayList<String> Email2_Array = new ArrayList<String>();
		ArrayList<String> RouteArray = new ArrayList<String>();
		ArrayList<String> DateArray= new ArrayList<String>();
		ArrayList<String> TimeArray = new ArrayList<String>();
		ArrayList<String> S_No_Array = new ArrayList<String>();
		ArrayList<String> Off1_Array = new ArrayList<String>();
		ArrayList<String> Cohort_Array = new ArrayList<String>();
		ArrayList<String> NumberStud_Array = new ArrayList<String>();
		ArrayList<String> Res2_Array = new ArrayList<String>();
		ArrayList<String> Mob1_Array = new ArrayList<String>();
		ArrayList<String> Mob2_Array = new ArrayList<String>();
		ArrayList<String> Tag_Array = new ArrayList<String>();
		ArrayList<String> Day_Array = new ArrayList<String>();


		Calendar calendar1 = Calendar.getInstance();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/m/yyyy h'h'mm");
		String currentDate = formatter1.format(calendar1.getTime());

		String datadb;

		ListEntry TempList = listFeed.getEntries().get(0);
		for(String tag:TempList.getCustomElements().getTags()){
			Tag_Array.add(tag);
		}

		for (ListEntry row : listFeed.getEntries()) {
			for (String tag : row.getCustomElements().getTags()) {

				datadb = row.getCustomElements().getValue("timestamp");
				if(currentDate.compareTo(datadb)<=0) {
					//Log.d("datefreshcompared", "this timestamp was on: " + datadb + " and the current app_event is:" + currentDate);


					if (tag.equals("s.no.")) {
						S_No_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("timestamp")) {
						Res2_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));

						//Log.d("datecomapare","this timestamp was on" + datadb+" and the current app_event is:" + currentDate);
//					if (CheckDates(currentDate,datadb )){
//						Log.d("datefresh","this timestamp was on: " + datadb+" and the current app_event is:" + currentDate);
//					}
//						Log.d("sequence", "");
					}

					if (tag.equals("route")) {
						RouteArray.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("date")) {
						DateArray.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("time")) {
						TimeArray.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("category")) {
						CategoryArray.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("shuttle")) {
						ShuttleArray.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("o1")) {
						Off1_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("cohort")) {
						Cohort_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}

					if (tag.equals("numberofstudents")) {
						NumberStud_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}

					if (tag.equals("m1")) {
						Mob1_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("m2")) {
						Mob2_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("e1")) {
						Email1_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("e2")) {
						Email2_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
					if (tag.equals("day")) {
						Day_Array.add(ProperValue(row.getCustomElements()
								.getValue(tag)));
					}
				}
			}

		}

		Transp_XMLParserClass.CategoryArray = CategoryArray;
		Transp_XMLParserClass.ShuttleArray = ShuttleArray;
		Transp_XMLParserClass.Email1_Array = Email1_Array;
		Transp_XMLParserClass.Email2_Array = Email2_Array;
		Transp_XMLParserClass.RouteArray = RouteArray;
		Transp_XMLParserClass.DateArray = DateArray;
		Transp_XMLParserClass.Off1_Array = Off1_Array;
		Transp_XMLParserClass.Cohort_Array = Cohort_Array;
		Transp_XMLParserClass.TimeArray = TimeArray;
		Transp_XMLParserClass.NumberStud_Array = NumberStud_Array;
		Transp_XMLParserClass.Res2_Array = Res2_Array;
		Transp_XMLParserClass.Mob1_Array = Mob1_Array;
		Transp_XMLParserClass.Mob2_Array = Mob2_Array;
		Transp_XMLParserClass.Tag_Array = Tag_Array;
		Transp_XMLParserClass.Day_Array = Day_Array;

	}

	public String ProperValue(String input) {
		if (input == null) {
			return new String("");
		} else {
			return input;
		}

	}




}
