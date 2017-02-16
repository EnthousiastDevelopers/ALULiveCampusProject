package com.tolotranet.livecampus;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Transp_ElementDetailListView extends Activity {

	ListView lv;
	Transp_DetailListViewAdapter myPersonDetailListViewAdapter;
	ArrayList<Transp_DetailListItem> DetailList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transp_activity_detail_list_view);

		Intent i = getIntent();
		int Index = i.getIntExtra("index", 0);
		DetailList = getPersonalDetails(Index);

		lv = (ListView) findViewById(R.id.person_details_lv);
		myPersonDetailListViewAdapter = new Transp_DetailListViewAdapter(this,
				DetailList);
		lv.setAdapter(myPersonDetailListViewAdapter);
		lv.setOnItemClickListener(new PersonDetailListViewClickListener());
	}

	private ArrayList<Transp_DetailListItem> getPersonalDetails(int Index) {
		ArrayList<Transp_DetailListItem> DetailList = new ArrayList<Transp_DetailListItem>();

		Transp_DetailListItem sr = new Transp_DetailListItem();

		if (!(Transp_XMLParserClass.RouteArray.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Route");
			sr.setDetailValue(Transp_XMLParserClass.RouteArray.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.DateArray.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Date");
			sr.setDetailValue(Transp_XMLParserClass.DateArray.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.Day_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Day");
			sr.setDetailValue(Transp_XMLParserClass.Day_Array.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.TimeArray.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Time");
			sr.setDetailValue(Transp_XMLParserClass.TimeArray.get(Index));
			DetailList.add(sr);
		}



		if (!(Transp_XMLParserClass.CategoryArray.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Category");
			sr.setDetailValue(Transp_XMLParserClass.CategoryArray.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.Off1_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Office");
			sr.setDetailValue(Transp_XMLParserClass.Off1_Array.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.Cohort_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Cohort");
			sr.setDetailValue(Transp_XMLParserClass.Cohort_Array.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.NumberStud_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Number of student");
			sr.setDetailValue(Transp_XMLParserClass.NumberStud_Array.get(Index));
			DetailList.add(sr);
		}
		if (!(Transp_XMLParserClass.Res2_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Residence");
			sr.setDetailValue(Transp_XMLParserClass.Res2_Array.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.Mob1_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Mobile");
			sr.setDetailValue(Transp_XMLParserClass.Mob1_Array.get(Index));
			DetailList.add(sr);
		}
		if (!(Transp_XMLParserClass.Mob2_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Mobile");
			sr.setDetailValue(Transp_XMLParserClass.Mob2_Array.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.Email1_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Email");
			sr.setDetailValue(Transp_XMLParserClass.Email1_Array.get(Index));
			DetailList.add(sr);
		}


		if (!(Transp_XMLParserClass.Email2_Array.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Email");
			sr.setDetailValue(Transp_XMLParserClass.Email2_Array.get(Index));
			DetailList.add(sr);
		}

		if (!(Transp_XMLParserClass.ShuttleArray.get(Index).equals(""))) {
			sr = new Transp_DetailListItem();
			sr.setDetailName("Shuttle");
			sr.setDetailValue(Transp_XMLParserClass.ShuttleArray.get(Index));
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
			String TempDetailName = (String) ((Transp_DetailListItem) arg0
					.getItemAtPosition(arg2)).getDetailName();
			if (TempDetailName.equals("Mobile")
					|| TempDetailName.equals("Residence")
					|| TempDetailName.equals("Office")) {
				String DetailValue = (String) ((Transp_DetailListItem) arg0
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
				String ToEmailId = (String) ((Transp_DetailListItem) arg0
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
	@Override
	public void onBackPressed() {

		//No user end update available
//		Transp_SisUpdateMe updateMe = new Transp_SisUpdateMe();
//		updateMe.execute(this);
		Transp_ElementDetailListView.super.onBackPressed();
		//Go back directly
//		new AlertDialog.Builder(this)
//				.setTitle("Save Changes?")
//				.setMessage("Are you sure you want to exit?")
//				.setNegativeButton(android.R.string.no, null)
//				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//					public void onClick(DialogInterface arg0, int arg1) {
//						Transp_ElementDetailListView.super.onBackPressed();
//					}
//				}).create().show();
	}

}
