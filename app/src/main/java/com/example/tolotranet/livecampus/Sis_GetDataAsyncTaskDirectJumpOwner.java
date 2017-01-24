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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Sis_GetDataAsyncTaskDirectJumpOwner extends AsyncTask<Activity, Void, Void> {

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
            String xmlString = Sis_XMLCreator.CreateSpreadSheetToXML();
            Sis_FileOperations.StoreData(xmlString);
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
        Intent i = new Intent(myActivity.getApplicationContext(), Sis_DetailListViewOwner.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        int index, userId;
        AppSelect AppHelper = new AppSelect();

        userId = AppHelper.getUserID();
        index = AppHelper.getUserID() - 1000001;

        i.putExtra("origin", "appselect");
        i.putExtra("index", index);
        i.putExtra("myId", userId);

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
                "https://spreadsheets.google.com/feeds/worksheets/1En7qJ5aG2U9fEMSoh-wW1FXoqzNWQfMB4v6-vcN5pVg/public/full");

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
        ArrayList<String> q15 = new ArrayList<String>();
        ArrayList<String> Tag_Array = new ArrayList<String>();

        ListEntry TempList = listFeed.getEntries().get(0);
        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(myActivity);
        String Uemail = helper.getUserEmail();

        for (String tag : TempList.getCustomElements().getTags()) {
            Tag_Array.add(tag);
        }

        for (ListEntry row : listFeed.getEntries()) {
            for (String tag : row.getCustomElements().getTags()) {
                if (tag.equals("userid")) {
                    q1.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("emailaddress")) {
                    q2.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                    Log.d("helloidbefore", Uemail);
                    Log.d("helloidbefore", row.getCustomElements().getValue(tag));

                    if(ProperValue(row.getCustomElements().getValue(tag)).equals("")){
                        Log.d("hello", "empty tag container emailaddress");
                    }
                    if (row.getCustomElements().getValue(tag).equals(Uemail)) {
                        String IdString;
                        IdString = row.getCustomElements().getValue("userid");
                        Log.d("helloid", IdString);
                        helper.Update_user_ID(Integer.parseInt(IdString));
                        Log.d("helloid2", IdString);
                    }
                }
                if (tag.equals("firstname")) {
                    q3.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("lastname")) {
                    q4.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("fullname")) {
                    q5.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("birthday")) {
                    q6.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("nationality")) {
                    q7.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("passportnumber")) {
                    q8.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("phonenumber1")) {
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
                if (tag.equals("residence")) {
                    q13.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("room")) {
                    q14.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
                if (tag.equals("major")) {
                    q15.add(ProperValue(row.getCustomElements()
                            .getValue(tag)));
                }
            }

        }

        Sis_XMLParserClass.q1 = q1;
        Sis_XMLParserClass.q2 = q2;
        Sis_XMLParserClass.q3 = q3;
        Sis_XMLParserClass.q4 = q4;
        Sis_XMLParserClass.q5 = q5;
        Sis_XMLParserClass.q6 = q6;
        Sis_XMLParserClass.q7 = q7;
        Sis_XMLParserClass.q8 = q8;
        Sis_XMLParserClass.q9 = q9;
        Sis_XMLParserClass.q10 = q10;
        Sis_XMLParserClass.q11 = q11;
        Sis_XMLParserClass.q12 = q12;
        Sis_XMLParserClass.q13 = q13;
        Sis_XMLParserClass.q14 = q14;
        Sis_XMLParserClass.q15 = q15;
        Sis_XMLParserClass.Tag_Array = Tag_Array;

    }

    public String ProperValue(String input) {
        if (input == null) {
            return new String("");
        } else {
            return input;
        }

    }

}
