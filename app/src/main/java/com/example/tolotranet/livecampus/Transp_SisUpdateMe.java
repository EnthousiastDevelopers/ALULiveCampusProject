package com.example.tolotranet.livecampus;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Created by Tolotra Samuel on 23/08/2016.
 */
public class Transp_SisUpdateMe extends AsyncTask<Activity, Object, Void> {

    Activity myActivity;
    @Override
    protected Void doInBackground(Activity... arg0) {
        // TODO Auto-generated method stub
        myActivity = arg0[0];

        try {
            updateaction(myActivity);
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
        return  null;
    }


    public static void updateaction(Context myContext)
            throws AuthenticationException, MalformedURLException, IOException, ServiceException,URISyntaxException {

        SpreadsheetService service =
                new SpreadsheetService("MySpreadsheetIntegration-v1");

        service.setProtocolVersion(SpreadsheetService.Versions.V3);

        AssetManager am = myContext.getAssets();
        InputStream inputStream = am.open("key.p12");
        File file = createFileFromInputStream(inputStream);

        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String[] SCOPESArray = {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/1En7qJ5aG2U9fEMSoh-wW1FXoqzNWQfMB4v6-vcN5pVg/private/full", "https://docs.google.com/feeds"};
        final List SCOPES = Arrays.asList(SCOPESArray);
        GoogleCredential credential = null;
        try {
            credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setServiceAccountId("732698423440-ucindhfeddmul5f2dmp42kuv18r3mecq.apps.googleusercontent.com")
                    .setServiceAccountScopes(SCOPES)
                    .setServiceAccountPrivateKeyFromP12File(file)
                    .build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        service.setOAuth2Credentials(credential);
        // TODO: Authorize the service object for a specific user (see other sections)

        // TODO: Authorize the service object for a specific user (see other sections)

        // Define the URL to request.  This should never change.
        URL SPREADSHEET_FEED_URL = new URL(
                "https://spreadsheets.google.com/feeds/worksheets/1En7qJ5aG2U9fEMSoh-wW1FXoqzNWQfMB4v6-vcN5pVg/private/full");


        WorksheetFeed worksheetFeed = service.getFeed(
                SPREADSHEET_FEED_URL, WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        WorksheetEntry worksheet = worksheets.get(0);
        Log.d("hello", "Worksheet name is "
                + worksheet.getTitle().getPlainText());

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = new URI(worksheet.getListFeedUrl().toString())
                .toURL();

        Log.d("hello", "URL is \n " + listFeedUrl.toString());

        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        // TODO: Choose a row more intelligently based on your app's needs.
        ListEntry row = listFeed.getEntries().get(0);

        // Update the row's data.
        row.getCustomElements().setValueLocal("route", "Sarah");
        row.getCustomElements().setValueLocal("day", "Hunt");
        row.getCustomElements().setValueLocal("shuttle", "32");
        row.getCustomElements().setValueLocal("category", "154");

        // Save the row using the API.
        row.update();
    }
    private static File createFileFromInputStream(InputStream inputStream) {

        String path = "";

        File file = new File(Environment.getExternalStorageDirectory(),
                "KeyHolder/KeyFile/");
        if (!file.exists()) {
            if (!file.mkdirs())
                Log.d("KeyHolder", "Folder not created");
            else
                Log.d("KeyHolder", "Folder created");
        } else
            Log.d("KeyHolder", "Folder present");

        path = file.getAbsolutePath();

        try {
            File f = new File(path+"/MyKey");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            // Logging exception
            e.printStackTrace();
        }

        return null;
    }

    }
