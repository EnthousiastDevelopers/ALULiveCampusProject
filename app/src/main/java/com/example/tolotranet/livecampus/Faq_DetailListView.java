package com.example.tolotranet.livecampus;

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

import java.util.ArrayList;

public class Faq_DetailListView extends Activity {

	ListView lv;
	Faq_DetailListViewAdapter myPersonDetailListViewAdapter;
	ArrayList<Faq_DetailListItem> DetailList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq_activity_detail_list_view);
		Log.d("hello", "view mode has started");
		Intent i = getIntent();
		int Index = i.getIntExtra("index", 0);
		DetailList = getPersonalDetails(Index);

		lv = (ListView) findViewById(R.id.person_details_lv);
		myPersonDetailListViewAdapter = new Faq_DetailListViewAdapter(this,
				DetailList);
		lv.setAdapter(myPersonDetailListViewAdapter);
		lv.setOnItemClickListener(new PersonDetailListViewClickListener());
	}

	private ArrayList<Faq_DetailListItem> getPersonalDetails(int Index) {
		ArrayList<Faq_DetailListItem> DetailList = new ArrayList<Faq_DetailListItem>();
		String nullTag = "Update your";

		Faq_DetailListItem sr = new Faq_DetailListItem();
		sr.setDetailName("Question");
		sr.setDetailValue(Faq_XMLParserClass.q2.get(Index));
		DetailList.add(sr);

		if (!(Faq_XMLParserClass.q5.get(Index).equals("")|| Faq_XMLParserClass.q5.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("Description");
			sr.setDetailValue(Faq_XMLParserClass.q5.get(Index));
			DetailList.add(sr);
		}

		if (!(Faq_XMLParserClass.q3.get(Index).equals("")|| Faq_XMLParserClass.q3.get(Index).startsWith(nullTag) )) {
		sr = new Faq_DetailListItem();
		sr.setDetailName("Asked by");
		sr.setDetailValue(Faq_XMLParserClass.q3.get(Index));
		DetailList.add(sr);
		}


		if (!(Faq_XMLParserClass.q6.get(Index).equals("")|| Faq_XMLParserClass.q6.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("Answer");
			sr.setDetailValue(Faq_XMLParserClass.q6.get(Index));
			DetailList.add(sr);
		}


		if ((Faq_XMLParserClass.q6.get(Index).equals("")|| Faq_XMLParserClass.q6.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("Answer");
			sr.setDetailValue("No answer yet");
			DetailList.add(sr);
		}


		if (!(Faq_XMLParserClass.q4.get(Index).equals("")|| Faq_XMLParserClass.q4.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("Organiser");
			sr.setDetailValue(Faq_XMLParserClass.q4.get(Index));
			DetailList.add(sr);
		}



		if (!(Faq_XMLParserClass.q7.get(Index).equals("")|| Faq_XMLParserClass.q7.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("Vote");
			sr.setDetailValue(Faq_XMLParserClass.q7.get(Index));
			DetailList.add(sr);
		}
		if (!(Faq_XMLParserClass.q8.get(Index).equals("")|| Faq_XMLParserClass.q8.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("passportnumber");
			sr.setDetailValue(Faq_XMLParserClass.q8.get(Index));
			DetailList.add(sr);
		}

		if (!(Faq_XMLParserClass.q9.get(Index).equals("")|| Faq_XMLParserClass.q9.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("phonenumber1");
			sr.setDetailValue(Faq_XMLParserClass.q9.get(Index));
			DetailList.add(sr);
		}
		if (!(Faq_XMLParserClass.q10.get(Index).equals("")|| Faq_XMLParserClass.q10.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("phonenumber2");
			sr.setDetailValue(Faq_XMLParserClass.q10.get(Index));
			DetailList.add(sr);
		}

		if (!(Faq_XMLParserClass.q11.get(Index).equals("")|| Faq_XMLParserClass.q11.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("phonenumber3");
			sr.setDetailValue(Faq_XMLParserClass.q11.get(Index));
			DetailList.add(sr);
		}

		if (!(Faq_XMLParserClass.q12.get(Index).equals("")|| Faq_XMLParserClass.q12.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("apartment");
			sr.setDetailValue(Faq_XMLParserClass.q12.get(Index));
			DetailList.add(sr);
		}
		if (!(Faq_XMLParserClass.q13.get(Index).equals("")|| Faq_XMLParserClass.q13.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("residence");
			sr.setDetailValue(Faq_XMLParserClass.q13.get(Index));
			DetailList.add(sr);
		}
		if (!(Faq_XMLParserClass.q14.get(Index).equals("")|| Faq_XMLParserClass.q14.get(Index).startsWith(nullTag) )) {
			sr = new Faq_DetailListItem();
			sr.setDetailName("room");
			sr.setDetailValue(Faq_XMLParserClass.q14.get(Index));
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
			String TempDetailName = (String) ((Faq_DetailListItem) arg0
					.getItemAtPosition(arg2)).getDetailName();
			if (TempDetailName.equals("Mobile")
					|| TempDetailName.equals("Residence")
					|| TempDetailName.equals("Office")) {
				String DetailValue = (String) ((Faq_DetailListItem) arg0
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
				String ToEmailId = (String) ((Faq_DetailListItem) arg0
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
