package com.tolotranet.livecampus.Nfc;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tolotra Samuel on 19/03/2017.
 */


public class Nfc_registerCard_Sis extends AsyncTask<Void, Void, List<String>> {
    //dont forget to override on cancel where you call this, check nfc_food_main to see how

    private Exception mLastError = null;
    public com.google.api.services.sheets.v4.Sheets mService = null;
    private String spreadSheetAction;
    private String NewValue;
    private int MyRow, ColumnID;
    private boolean hasFinishedNewIdRegistration;


    public Nfc_registerCard_Sis(GoogleAccountCredential mCredential, String spreadSheetAction, int myRow, int columnID, String newValue) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("Google Sheets API Android Quickstart")
                .build();

        this.MyRow = myRow;
        this.ColumnID = columnID;
        this.NewValue = newValue;
        this.spreadSheetAction = spreadSheetAction;
    }

    /**
     * Background task to call Google Sheets API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            return GetEditDataFromApi(); //because ALL request set are finished, create the request
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    private List<String> GetEditDataFromApi() throws IOException {
        Log.d("hello", "starting get edit data from API");
        String spreadsheetId = "1En7qJ5aG2U9fEMSoh-wW1FXoqzNWQfMB4v6-vcN5pVg";
        List<Request> requests = new ArrayList<>();
        List<String> results = new ArrayList<String>();


        if (spreadSheetAction.equals("test")) {
            String range = "student data!A2:V12";

            ValueRange response = mService.spreadsheets().values().get(spreadsheetId, range).execute();

            List<List<Object>> values = response.getValues();
            if (values != null) {
                results.add("Name, Major");
                for (List row : values) {
                    Log.d("hello", row.get(0) + ", " + row.get(4));
                }
            }
        }

        if (spreadSheetAction.equals("updateCell")) {


            Log.d("hello", "setting up Cells to update");
            List<CellData> NewValueCellData = new ArrayList<>();
            NewValueCellData.add(new CellData()
                    .setUserEnteredValue(new ExtendedValue()
                            .setStringValue(NewValue))
                    .setUserEnteredFormat(new CellFormat()
                            .setBackgroundColor(new Color()
                                    .setBlue(Float.valueOf(1)))));


            requests.add(new Request()
                    .setUpdateCells(new UpdateCellsRequest()
                            .setStart(new GridCoordinate()
                                    .setSheetId(0)
                                    .setRowIndex(MyRow - 1) //because the row index start with 0
                                    .setColumnIndex(ColumnID - 1)) //because the column index start with 0

                            .setRows(Arrays.asList(new RowData().setValues((NewValueCellData))))
                            .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
        }
        Log.d("hello", "column: " + String.valueOf(ColumnID) + ", row: " + String.valueOf(MyRow));


        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);

        //  String range = "student data!A2:V12";
        // ValueRange response = mService.spreadsheets().values().get(spreadsheetId, range).execute();

        mService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();

        hasFinishedNewIdRegistration = true;
        return results;
    }

    @Override
    protected void onPreExecute() {
        Log.d("hello", ". onPreExecute");
        //mProgress.show();
    }

    @Override
    protected void onPostExecute(List<String> output) {
        //mProgress.hide();
        if (output == null || output.size() == 0) {
            Log.d("hello", "No result returned");
        } else {
            output.add(0, "Data retrieved using the Google Sheets API:");
            Log.d("hello", TextUtils.join("\n", output));
        }
    }


}
