package com.tolotranet.livecampus.Food;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.FindReplaceRequest;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.tolotranet.livecampus.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Food_DetailListViewOwner extends Activity
		implements EasyPermissions.PermissionCallbacks {

	ListView lv;
	Food_DetailListViewAdapterOwner myPersonDetailListViewAdapter;
	ArrayList<Food_DetailListItem> DetailList;
	TextView mOutputText;
	GoogleAccountCredential mCredential;
    int Index;
	int userID;
	private Button mCallApiButton;
	ProgressDialog mProgress;
	Boolean hasUpdated = false;
	Map<String, String> map = new HashMap<String, String>();
	Map<String, String> mapupdate = new HashMap<String, String>();

	static final int REQUEST_ACCOUNT_PICKER = 1000;
	static final int REQUEST_AUTHORIZATION = 1001;
	static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
	static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

	private static final String BUTTON_TEXT = "Call Google Sheets API";
	private static final String PREF_ACCOUNT_NAME = "accountName";
	private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_activity_detail_list_view);
		Log.d("hello", "owner edit mode has started");

		Intent i = getIntent();
		Index = i.getIntExtra("index", 0);
		userID = i.getIntExtra("myId", 0);

		DetailList = getPersonalDetails(Index);

		lv = (ListView) findViewById(R.id.person_details_lv);

		myPersonDetailListViewAdapter = new Food_DetailListViewAdapterOwner(this,
				DetailList);
		lv.setAdapter(myPersonDetailListViewAdapter);
		lv.setOnItemClickListener(new PersonDetailListViewClickListener());


		// Initialize credentials and service object.
		mCredential = GoogleAccountCredential.usingOAuth2(
				getApplicationContext(), Arrays.asList(SCOPES))
				.setBackOff(new ExponentialBackOff());


			for (int n = 0; n < lv.getAdapter().getCount(); n++) {
				View viewET = lv.getChildAt(n);
				if (viewET != null) {
					EditText ValueEditText = (EditText) viewET.findViewById(R.id.detail_value);
					if (ValueEditText.length() > 0) {
						ValueEditText.getText().clear();
					}
					Log.d("hello", ":" + ValueEditText.getText().toString());
				}
			}
	}

	private ArrayList<Food_DetailListItem> getPersonalDetails(int Index) {
		ArrayList<Food_DetailListItem> DetailList = new ArrayList<Food_DetailListItem>();





		Food_DetailListItem sr = new Food_DetailListItem();
		String tag = "emailaddress";
		sr.setDetailName(tag);
		if (!(Food_XMLParserClass.q2.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q2.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
		DetailList.add(sr);
		map.put(tag, Food_XMLParserClass.q2.get(Index));


			tag = "firstname";
			sr = new Food_DetailListItem();
			sr.setDetailName(tag);
		if (!(Food_XMLParserClass.q3.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q3.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q3.get(Index));


			tag = "lastname";
			sr = new Food_DetailListItem();
			sr.setDetailName("lastname");
		if (!(Food_XMLParserClass.q4.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q4.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q4.get(Index));

			tag = "fullname";
			sr = new Food_DetailListItem();
			sr.setDetailName("fullname");
		if (!(Food_XMLParserClass.q5.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q5.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q5.get(Index));


			tag = "birthday";
			sr = new Food_DetailListItem();
			sr.setDetailName("birthday");
		if (!(Food_XMLParserClass.q6.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q6.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q6.get(Index));


			tag ="nationality";
			sr = new Food_DetailListItem();
			sr.setDetailName("nationality");
		if (!(Food_XMLParserClass.q7.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q7.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q7.get(Index));


			tag ="passportnumber";
			sr = new Food_DetailListItem();
			sr.setDetailName("passportnumber");
		if (!(Food_XMLParserClass.q8.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q8.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q8.get(Index));



			tag ="phonenumber1";
			sr = new Food_DetailListItem();
			sr.setDetailName("phonenumber1");
		if (!(Food_XMLParserClass.q9.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q9.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q9.get(Index));

			tag ="phonenumber2";
			sr = new Food_DetailListItem();
			sr.setDetailName("phonenumber2");
		if (!(Food_XMLParserClass.q10.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q10.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q10.get(Index));

			tag ="phonenumber3";
			sr = new Food_DetailListItem();
			sr.setDetailName("phonenumber3");
		if (!(Food_XMLParserClass.q11.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q11.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q11.get(Index));



			tag ="apartment";
			sr = new Food_DetailListItem();
			sr.setDetailName("apartment");
		if (!(Food_XMLParserClass.q12.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q12.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q12.get(Index));

			tag ="residence";
			sr = new Food_DetailListItem();
			sr.setDetailName("residence");
		if (!(Food_XMLParserClass.q13.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q13.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q13.get(Index));


			tag ="room";
			sr = new Food_DetailListItem();
			sr.setDetailName("room");
		if (!(Food_XMLParserClass.q14.get(Index).equals(""))) {
			sr.setDetailValue(Food_XMLParserClass.q14.get(Index));
		}else{
			sr.setDetailValue("please updatme me! :-( ");
		}
			DetailList.add(sr);
			map.put(tag, Food_XMLParserClass.q14.get(Index));


		//test
		String x = DetailList.get(2).DetailName;
		Log.d("helloget2", x);
		return DetailList;
	}



	public class PersonDetailListViewClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			// TODO Auto-generated method stub
			String TempDetailName = (String) ((Food_DetailListItem) arg0
					.getItemAtPosition(arg2)).getDetailName();
			if (TempDetailName.equals("Mobile")
					|| TempDetailName.equals("Residence")
					|| TempDetailName.equals("Office")) {
				String DetailValue = (String) ((Food_DetailListItem) arg0
						.getItemAtPosition(arg2)).getDetailValue();
				if (!DetailValue.equals("")) {
					Intent callintent = new Intent(Intent.ACTION_DIAL,
							Uri.parse("tel:"
									+ DetailValue.replaceAll("[A-Za-z()\\s]+",
									"").trim()));
					startActivity(callintent);
				}
			}

			if (TempDetailName.equals("Email")) {
				String ToEmailId = (String) ((Food_DetailListItem) arg0
						.getItemAtPosition(arg2)).getDetailValue();
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
				emailIntent.setData(Uri.parse("mailto:"));
				Log.d("hello", ToEmailId);
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[]{ToEmailId.trim()});
				// emailIntent.setType("message/rfc822");
				startActivity(Intent.createChooser(emailIntent,
						"Send mail using..."));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person_detail_list_view, menu);
		return true;
	}

	@Override
	public void onBackPressed() {

		//Sis_UpdateMe updateMe = new Sis_UpdateMe();
		//updateMe.execute(this);


	Integer x =	lv.getAdapter().getCount();
		Log.d("helloworld", x.toString());

		for (int i = 0; i < lv.getAdapter().getCount(); i++) {
			View viewTelefone = lv.getChildAt(i);
			if (viewTelefone != null) {
				TextView NameEditText = (TextView) viewTelefone.findViewById(R.id.detail_name);
				EditText ValueEditText = (EditText) viewTelefone.findViewById(R.id.detail_value);
				Log.d("hellotest", NameEditText.getText().toString() + ":" + ValueEditText.getText().toString());

				String xdata = map.get(NameEditText.getText().toString());
				Log.d("helloxdata", xdata);

				if (!xdata.equals(ValueEditText.getText().toString())){
					Log.d("hellocomparaison", "different need update");

					//mapudate is the findreplace couple to send to the sheet updater. xdata is the find, ValueEdittext is the replace
					if(ValueEditText.getText().toString().equals("")) {
						mapupdate.put(xdata, "Update your "+NameEditText.getText().toString());

						//exception 1 apartement to avoid interference with room number
					}else if ( (NameEditText.getText().toString().equals("apartment"))){
						if(ValueEditText.getText().toString().startsWith("Apartment")) {
							mapupdate.put(xdata, ValueEditText.getText().toString());
						}else{
							mapupdate.put(xdata, "Apartment "+ValueEditText.getText().toString());
						}
						//**end of exception
					}else{
						mapupdate.put(xdata, ValueEditText.getText().toString());
					}
					hasUpdated = true;
				}else {
					Log.d("hellocomparaison", "similar no need to update");
				}
			}
		}
//just test
		String xy = DetailList.get(3).DetailName;
		Log.d("helloget2just test", xy);

		if(hasUpdated == true) {
			getResultsFromApi();
		}else {
			Food_DetailListViewOwner.super.onBackPressed();
		}
	}


	/**
	 * Attempt to call the API, after verifying that all the preconditions are
	 * satisfied. The preconditions are: Google Play Services installed, an
	 * account was selected and the device currently has online access. If any
	 * of the preconditions are not satisfied, the app will prompt the user as
	 * appropriate.
	 */
	private void getResultsFromApi() {
		if (!isGooglePlayServicesAvailable()) {
			Log.d("hello", "google play service not available");
			acquireGooglePlayServices();
		} else if (mCredential.getSelectedAccountName() == null) {
			Log.d("hello", "no account chosen");
			chooseAccount();
		} else if (!isDeviceOnline()) {
			Log.d("hello", "no network conncetion available");
		} else {
			new MakeRequestTask(mCredential).execute();
			//then update the XML of the phone for offline view and go back
			Food_GetDataAsyncTask getDataTask = new Food_GetDataAsyncTask(Food_DetailListViewOwner.this, "normal");
			//getDataTask.execute(this);

		}
	}

	/**
	 * Attempts to set the account used with the API credentials. If an account
	 * name was previously saved it will use that one; otherwise an account
	 * picker dialog will be shown to the user. Note that the setting the
	 * account to use with the credentials object requires the app to have the
	 * GET_ACCOUNTS permission, which is requested here if it is not already
	 * present. The AfterPermissionGranted annotation indicates that this
	 * function will be rerun automatically whenever the GET_ACCOUNTS permission
	 * is granted.
	 */
	@AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
	private void chooseAccount() {
		if (EasyPermissions.hasPermissions(
				this, Manifest.permission.GET_ACCOUNTS)) {
			String accountName = getPreferences(Context.MODE_PRIVATE)
					.getString(PREF_ACCOUNT_NAME, null);
			if (accountName != null) {
				mCredential.setSelectedAccountName(accountName);
				getResultsFromApi();
			} else {
				// Start a dialog from which the user can choose an account
				startActivityForResult(
						mCredential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
		} else {
			// Request the GET_ACCOUNTS permission via a user dialog
			EasyPermissions.requestPermissions(
					this,
					"This app needs to access your Google account (via Contacts).",
					REQUEST_PERMISSION_GET_ACCOUNTS,
					Manifest.permission.GET_ACCOUNTS);
		}
	}

	/**
	 * Called when an activity launched here (specifically, AccountPicker
	 * and authorization) exits, giving you the requestCode you started it with,
	 * the resultCode it returned, and any additional data from it.
	 *
	 * @param requestCode code indicating which activity result is incoming.
	 * @param resultCode  code indicating the result of the incoming
	 *                    activity result.
	 * @param data        Intent (containing result data) returned by incoming
	 *                    activity result.
	 */
	@Override
	protected void onActivityResult(
			int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQUEST_GOOGLE_PLAY_SERVICES:
				if (resultCode != RESULT_OK) {
					Log.d("hello","This app requires Google Play Services. Please install " +
							"Google Play Services on your device and relaunch this app." );
				} else {
					getResultsFromApi();
				}
				break;
			case REQUEST_ACCOUNT_PICKER:
				if (resultCode == RESULT_OK && data != null &&
						data.getExtras() != null) {
					String accountName =
							data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						SharedPreferences settings =
								getPreferences(Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString(PREF_ACCOUNT_NAME, accountName);
						editor.apply();
						mCredential.setSelectedAccountName(accountName);
						getResultsFromApi();
					}
				}
				break;
			case REQUEST_AUTHORIZATION:
				if (resultCode == RESULT_OK) {
					getResultsFromApi();
				}
				break;
		}
	}

	/**
	 * Respond to requests for permissions at runtime for API 23 and above.
	 *
	 * @param requestCode  The request code passed in
	 *                     requestPermissions(android.app.Activity, String, int, String[])
	 * @param permissions  The requested permissions. Never null.
	 * @param grantResults The grant results for the corresponding permissions
	 *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(
				requestCode, permissions, grantResults, this);
	}

	/**
	 * Callback for when a permission is granted using the EasyPermissions
	 * library.
	 *
	 * @param requestCode The request code associated with the requested
	 *                    permission
	 * @param list        The requested permission list. Never null.
	 */
	@Override
	public void onPermissionsGranted(int requestCode, List<String> list) {
		// Do nothing.
	}

	/**
	 * Callback for when a permission is denied using the EasyPermissions
	 * library.
	 *
	 * @param requestCode The request code associated with the requested
	 *                    permission
	 * @param list        The requested permission list. Never null.
	 */
	@Override
	public void onPermissionsDenied(int requestCode, List<String> list) {
		// Do nothing.
	}

	/**
	 * Checks whether the device currently has a network connection.
	 *
	 * @return true if the device has a network connection, false otherwise.
	 */
	private boolean isDeviceOnline() {
		ConnectivityManager connMgr =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	/**
	 * Check that Google Play services APK is installed and up to date.
	 *
	 * @return true if Google Play Services is available and up to
	 * date on this device; false otherwise.
	 */
	private boolean isGooglePlayServicesAvailable() {
		GoogleApiAvailability apiAvailability =
				GoogleApiAvailability.getInstance();
		final int connectionStatusCode =
				apiAvailability.isGooglePlayServicesAvailable(this);
		return connectionStatusCode == ConnectionResult.SUCCESS;
	}

	/**
	 * Attempt to resolve a missing, out-of-date, invalid or disabled Google
	 * Play Services installation via a user dialog, if possible.
	 */
	private void acquireGooglePlayServices() {
		GoogleApiAvailability apiAvailability =
				GoogleApiAvailability.getInstance();
		final int connectionStatusCode =
				apiAvailability.isGooglePlayServicesAvailable(this);
		if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
			showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
		}
	}


	/**
	 * Display an error dialog showing that Google Play Services is missing
	 * or out of date.
	 *
	 * @param connectionStatusCode code describing the presence (or lack of)
	 *                             Google Play Services on this device.
	 */
	void showGooglePlayServicesAvailabilityErrorDialog(
			final int connectionStatusCode) {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		Dialog dialog = apiAvailability.getErrorDialog(
				Food_DetailListViewOwner.this,
				connectionStatusCode,
				REQUEST_GOOGLE_PLAY_SERVICES);
		dialog.show();
	}

	/**
	 * An asynchronous task that handles the Google Sheets API call.
	 * Placing the API calls in their own task ensures the UI stays responsive.
	 */
	private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
		private com.google.api.services.sheets.v4.Sheets mService = null;
		private Exception mLastError = null;

		public MakeRequestTask(GoogleAccountCredential credential) {
			HttpTransport transport = AndroidHttp.newCompatibleTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			mService = new com.google.api.services.sheets.v4.Sheets.Builder(
					transport, jsonFactory, credential)
					.setApplicationName("Google Sheets API Android Quickstart")
					.build();
		}

		/**
		 * Background task to call Google Sheets API.
		 *
		 * @param params no parameters needed for this task.
		 */
		@Override
		protected List<String> doInBackground(Void... params) {
			try {
				return getDataFromApi();
			} catch (Exception e) {
				mLastError = e;
				cancel(true);
				return null;
			}
		}

		/**
		 * Fetch a list of names and majors of students in a sample spreadsheet:
		 * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
		 *
		 * @return List of names and majors
		 * @throws IOException
		 */
		private List<String> getDataFromApi() throws IOException {
			String spreadsheetId = "1En7qJ5aG2U9fEMSoh-wW1FXoqzNWQfMB4v6-vcN5pVg";
			String range = "student data!A2:V12";
			List<String> results = new ArrayList<String>();
			List<Request> requests = new ArrayList<>();

			ValueRange response = this.mService.spreadsheets().values()
					.get(spreadsheetId, range)
					.execute();


			List<List<Object>> values = response.getValues();
			if (values != null) {
				results.add("Fullname, Nationality");
				for (List row : values) {
					results.add(row.get(0) + ", " + row.get(4));
				}
			}

			//updaterows();
			//getting memory location
			Integer row = null;
			if( userID < 2000000 && userID > 999999) {
				//student reference IDs are between 1,000,000 and 2,000,000
				row = userID - 1000000;
			}


			List<CellData> valuesL = new ArrayList<>();
			for (Map.Entry<String, String> entry : mapupdate.entrySet()) {
				//check HasMapLabelValueCoupleOld iteration
				Log.d("hello HasMapLabelValueCoupleOld iteration", entry.getKey() + "/" + entry.getValue());


				requests.add(new Request().setFindReplace(new FindReplaceRequest().setFind(entry.getKey())
						.setMatchEntireCell(true)
						.setMatchCase(true)
						.setReplacement(entry.getValue())
						.setRange(new GridRange()
								.setSheetId(0)
								.setStartRowIndex(row)
								.setEndRowIndex(row + 1))));
			}



			String cellReq = (new FindReplaceRequest().setFind("samuel")).getFind();
			Log.d("findings",cellReq);

	//This is a test (green color)
			valuesL.add(new CellData()
					.setUserEnteredValue(new ExtendedValue()
							.setNumberValue(Double.valueOf(3)))
					.setUserEnteredFormat(new CellFormat()
							.setBackgroundColor(new Color()
									.setGreen(Float.valueOf(1)))));
			requests.add(new Request()
					.setUpdateCells(new UpdateCellsRequest()
							.setStart(new GridCoordinate()
									.setSheetId(0)
									.setRowIndex(3)
									.setColumnIndex(2))
							.setRows(Arrays.asList(
									new RowData().setValues(valuesL)))
							.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));


			BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
					.setRequests(requests);

			try {
				mService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest)
						.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return results;
		}
		private void updaterows() {

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

		@Override
		protected void onCancelled() {
			//mProgress.hide();
			if (mLastError != null) {
				if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
					showGooglePlayServicesAvailabilityErrorDialog(
							((GooglePlayServicesAvailabilityIOException) mLastError)
									.getConnectionStatusCode());
				} else if (mLastError instanceof UserRecoverableAuthIOException) {
					startActivityForResult(
							((UserRecoverableAuthIOException) mLastError).getIntent(),
							Food_DetailListViewOwner.REQUEST_AUTHORIZATION);
				} else {
					Log.d("hello", "The following error occurred:\n"
							+ mLastError.getMessage());

				}
			} else {
				Log.d("hello", "Request cancelled.");
			}
		}
	}
}