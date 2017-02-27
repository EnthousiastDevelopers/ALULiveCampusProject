package com.tolotranet.livecampus;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Bubble_DetailListView extends Activity {

	ListView lv;
	Bubble_DetailListViewAdapter myPersonDetailListViewAdapter;
	ArrayList<Bubble_DetailListItem> DetailList;
	Button AddToCartBtn, BuyNowBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq_activity_detail_list_view);
		Log.d("hello", "view mode has started");
		Intent i = getIntent();
		int Index = i.getIntExtra("index", 0);
		DetailList = getPersonalDetails(Index);
		AddToCartBtn = (Button) findViewById(R.id.add_to_cart_btn);
		BuyNowBtn = (Button) findViewById(R.id.buy_now_btn);

		AddToCartBtn.setOnClickListener(new ShopContactListViewClickListener() );
		BuyNowBtn.setOnClickListener(new ShopContactListViewClickListener() );
		lv = (ListView) findViewById(R.id.person_details_lv);
		myPersonDetailListViewAdapter = new Bubble_DetailListViewAdapter(this,
				DetailList);
		lv.setAdapter(myPersonDetailListViewAdapter);
		lv.setOnItemClickListener(new PersonDetailListViewClickListener());
	}
	private class ShopContactListViewClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "You have not enough points, contribute to the App or Add money to your CampusLive-Wallet" , Toast.LENGTH_LONG).show();
		}
	}
	private ArrayList<Bubble_DetailListItem> getPersonalDetails(int Index) {
		ArrayList<Bubble_DetailListItem> DetailList = new ArrayList<Bubble_DetailListItem>();
		String nullTag = "Update your";

		Bubble_DetailListItem sr = new Bubble_DetailListItem();
		sr.setDetailName("Question");
		sr.setDetailValue(Bubble_XMLParserClass.q2.get(Index));
		DetailList.add(sr);

		if (!(Bubble_XMLParserClass.q5.get(Index).equals("")|| Bubble_XMLParserClass.q5.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("Description");
			sr.setDetailValue(Bubble_XMLParserClass.q5.get(Index));
			DetailList.add(sr);
		}

		if (!(Bubble_XMLParserClass.q3.get(Index).equals("")|| Bubble_XMLParserClass.q3.get(Index).startsWith(nullTag) )) {
		sr = new Bubble_DetailListItem();
		sr.setDetailName("Asked by");
		sr.setDetailValue(Bubble_XMLParserClass.q3.get(Index));
		DetailList.add(sr);
		}


		if (!(Bubble_XMLParserClass.q6.get(Index).equals("")|| Bubble_XMLParserClass.q6.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("Answer");
			sr.setDetailValue(Bubble_XMLParserClass.q6.get(Index));
			DetailList.add(sr);
		}


		if ((Bubble_XMLParserClass.q6.get(Index).equals("")|| Bubble_XMLParserClass.q6.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("Answer");
			sr.setDetailValue("No answer yet");
			DetailList.add(sr);
		}


		if (!(Bubble_XMLParserClass.q4.get(Index).equals("")|| Bubble_XMLParserClass.q4.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("Organiser");
			sr.setDetailValue(Bubble_XMLParserClass.q4.get(Index));
			DetailList.add(sr);
		}



		if (!(Bubble_XMLParserClass.q7.get(Index).equals("")|| Bubble_XMLParserClass.q7.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("Vote");
			sr.setDetailValue(Bubble_XMLParserClass.q7.get(Index));
			DetailList.add(sr);
		}
		if (!(Bubble_XMLParserClass.q8.get(Index).equals("")|| Bubble_XMLParserClass.q8.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("passportnumber");
			sr.setDetailValue(Bubble_XMLParserClass.q8.get(Index));
			DetailList.add(sr);
		}

		if (!(Bubble_XMLParserClass.q9.get(Index).equals("")|| Bubble_XMLParserClass.q9.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("phonenumber1");
			sr.setDetailValue(Bubble_XMLParserClass.q9.get(Index));
			DetailList.add(sr);
		}
		if (!(Bubble_XMLParserClass.q10.get(Index).equals("")|| Bubble_XMLParserClass.q10.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("phonenumber2");
			sr.setDetailValue(Bubble_XMLParserClass.q10.get(Index));
			DetailList.add(sr);
		}

		if (!(Bubble_XMLParserClass.q11.get(Index).equals("")|| Bubble_XMLParserClass.q11.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("phonenumber3");
			sr.setDetailValue(Bubble_XMLParserClass.q11.get(Index));
			DetailList.add(sr);
		}

		if (!(Bubble_XMLParserClass.q12.get(Index).equals("")|| Bubble_XMLParserClass.q12.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("apartment");
			sr.setDetailValue(Bubble_XMLParserClass.q12.get(Index));
			DetailList.add(sr);
		}
		if (!(Bubble_XMLParserClass.q13.get(Index).equals("")|| Bubble_XMLParserClass.q13.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("residence");
			sr.setDetailValue(Bubble_XMLParserClass.q13.get(Index));
			DetailList.add(sr);
		}
		if (!(Bubble_XMLParserClass.q14.get(Index).equals("")|| Bubble_XMLParserClass.q14.get(Index).startsWith(nullTag) )) {
			sr = new Bubble_DetailListItem();
			sr.setDetailName("room");
			sr.setDetailValue(Bubble_XMLParserClass.q14.get(Index));
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
			String TempDetailName = (String) ((Bubble_DetailListItem) arg0
					.getItemAtPosition(arg2)).getDetailName();
			if (TempDetailName.equals("Mobile")
					|| TempDetailName.equals("Residence")
					|| TempDetailName.equals("Office")) {
				String DetailValue = (String) ((Bubble_DetailListItem) arg0
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
				String ToEmailId = (String) ((Bubble_DetailListItem) arg0
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
