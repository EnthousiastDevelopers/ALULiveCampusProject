package com.tolotranet.livecampus.Sis;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tolotranet.livecampus.R;

import java.util.ArrayList;

public class Sis_DetailListView extends Activity {

	TextView fn;
	TextView cy;
	ListView lv;
	Sis_DetailListViewAdapter myPersonDetailListViewAdapter;
	ArrayList<Sis_DetailListItem> DetailList;
	ProgressDialog mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sis_activity_detail_list_view);
		Log.d("hello", "view mode has started");
		Intent i = getIntent();
		int Index = i.getIntExtra("index", 0);

		//first section (profile pic, fullname, country)
		fn = (TextView) findViewById(R.id.fullName);
		cy = (TextView) findViewById(R.id.country);
			fn.setText(Sis_XMLParserClass.q5.get(Index));
			cy.setText(Sis_XMLParserClass.q7.get(Index));

		//second section (the rest available)
		DetailList = getPersonalDetails(Index);

		lv = (ListView) findViewById(R.id.person_details_lv);
		myPersonDetailListViewAdapter = new Sis_DetailListViewAdapter (this,
				DetailList);
		lv.setAdapter(myPersonDetailListViewAdapter);
		lv.setOnItemClickListener(new PersonDetailListViewClickListener());
	}

	private ArrayList<Sis_DetailListItem> getPersonalDetails(int Index) {
		ArrayList<Sis_DetailListItem> DetailList = new ArrayList<Sis_DetailListItem>();
		String nullTag = "Update your";

		Sis_DetailListItem sr = new Sis_DetailListItem();
		sr.setDetailName("Email Address");
		sr.setDetailValue(Sis_XMLParserClass.q2.get(Index));
		DetailList.add(sr);

		if (!(Sis_XMLParserClass.q3.get(Index).equals("")|| Sis_XMLParserClass.q3.get(Index).startsWith(nullTag) )) {
		sr = new Sis_DetailListItem();
		sr.setDetailName("Firstname");
		sr.setDetailValue(Sis_XMLParserClass.q3.get(Index));
		DetailList.add(sr);
		}

		if (!(Sis_XMLParserClass.q4.get(Index).equals("")|| Sis_XMLParserClass.q4.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Lastname");
			sr.setDetailValue(Sis_XMLParserClass.q4.get(Index));
			DetailList.add(sr);
		}

//		just ignore the fullname which will be created using first and lastname
//		if (!(Sis_XMLParserClass.q5.get(Index).equals("")|| Sis_XMLParserClass.q5.get(Index).startsWith(nullTag) )) {
//			sr = new Sis_DetailListItem();
//			sr.setDetailName("fullname");
//			sr.setDetailValue(Sis_XMLParserClass.q5.get(Index));
//			DetailList.add(sr);
//		}

		if (!(Sis_XMLParserClass.q6.get(Index).equals("")|| Sis_XMLParserClass.q6.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Birthday");
			sr.setDetailValue(Sis_XMLParserClass.q6.get(Index));
			DetailList.add(sr);
		}

		if (!(Sis_XMLParserClass.q7.get(Index).equals("")|| Sis_XMLParserClass.q7.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Nationality");
			sr.setDetailValue(Sis_XMLParserClass.q7.get(Index));
			DetailList.add(sr);
		}

		if (!(Sis_XMLParserClass.q15.get(Index).equals("")|| Sis_XMLParserClass.q15.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Major");
			sr.setDetailValue(Sis_XMLParserClass.q15.get(Index));
			DetailList.add(sr);
		}
//		if (!(Sis_XMLParserClass.q8.get(Index).equals("")|| Sis_XMLParserClass.q8.get(Index).startsWith(nullTag) )) {
//			sr = new Sis_DetailListItem();
//			sr.setDetailName("NFC ID");
//			sr.setDetailValue(Sis_XMLParserClass.q8.get(Index));
//			DetailList.add(sr);
//		}

		if (!(Sis_XMLParserClass.q9.get(Index).equals("")|| Sis_XMLParserClass.q9.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Phone number");
			sr.setDetailValue(Sis_XMLParserClass.q9.get(Index));
			DetailList.add(sr);
		}
		if (!(Sis_XMLParserClass.q10.get(Index).equals("")|| Sis_XMLParserClass.q10.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Phone number");
			sr.setDetailValue(Sis_XMLParserClass.q10.get(Index));
			DetailList.add(sr);
		}

		if (!(Sis_XMLParserClass.q11.get(Index).equals("")|| Sis_XMLParserClass.q11.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Phone number");
			sr.setDetailValue(Sis_XMLParserClass.q11.get(Index));
			DetailList.add(sr);
		}

		if (!(Sis_XMLParserClass.q12.get(Index).equals("")|| Sis_XMLParserClass.q12.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Apartment");
			sr.setDetailValue(Sis_XMLParserClass.q12.get(Index));
			DetailList.add(sr);
		}
		if (!(Sis_XMLParserClass.q13.get(Index).equals("")|| Sis_XMLParserClass.q13.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Residence");
			sr.setDetailValue(Sis_XMLParserClass.q13.get(Index));
			DetailList.add(sr);
		}
		if (!(Sis_XMLParserClass.q14.get(Index).equals("")|| Sis_XMLParserClass.q14.get(Index).startsWith(nullTag) )) {
			sr = new Sis_DetailListItem();
			sr.setDetailName("Room");
			sr.setDetailValue(Sis_XMLParserClass.q14.get(Index));
			DetailList.add(sr);
		}
		return DetailList;
	}

	public class PersonDetailListViewClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			// TODO Auto-generated method stub
			String TempDetailName = (String) ((Sis_DetailListItem) arg0
					.getItemAtPosition(arg2)).getDetailName();
			if (TempDetailName.equals("Phone number")
					|| TempDetailName.equals("ResidenceX")
					|| TempDetailName.equals("OfficeX")) {
				String DetailValue = (String) ((Sis_DetailListItem) arg0
						.getItemAtPosition(arg2)).getDetailValue();
				if (!DetailValue.equals("")) {
					Intent callintent = new Intent(Intent.ACTION_DIAL,
							Uri.parse("tel:"
									+ DetailValue.replaceAll("[A-Za-z()\\s]+",
											"").trim()));
					startActivity(callintent);
				}
			}

			if (TempDetailName.equals("Email Address")) {
				String ToEmailId = (String) ((Sis_DetailListItem) arg0
						.getItemAtPosition(arg2)).getDetailValue();
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
				emailIntent.setData(Uri.parse("mailto:"));
				Log.d("hello", ToEmailId);
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { ToEmailId.trim() });
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

}
