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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;

public class Maint_DetailListView extends Activity {
	ArrayList<Com_ItemObject> CommentsItemArray;
	Thread send_comment_thread;
	ListView answer_lv;
	String comment_objectrandomid;
	String comment_fullnameauthor;
	String comment_emailauthor;
	String comment_parentid;
	String comment_category;
	String comment_description;
	String comment_col8;
	String comment_col9;
	String comment_col7;
	private Com_MyCustomBaseAdapter myComAdapter;
	EditText answerET;
	private String answertxt;

	ListView lv;
	Maint_DetailListViewAdapter myPersonDetailListViewAdapter;
	ArrayList<Maint_DetailListItem> DetailList;

	Button answerBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maint_activity_detail_list_view);
		Log.d("hello", "view mode has started");
		Intent i = getIntent();
		final int Index = i.getIntExtra("index", 0);
		DetailList = getPersonalDetails(Index);

		lv = (ListView) findViewById(R.id.person_details_lv);
		myPersonDetailListViewAdapter = new Maint_DetailListViewAdapter(this,
				DetailList);
		lv.setAdapter(myPersonDetailListViewAdapter);
		lv.setOnItemClickListener(new PersonDetailListViewClickListener());

		lv = (ListView) findViewById(R.id.person_details_lv);
		answer_lv = (ListView) findViewById(R.id.answer_lv);
		answerBtn = (Button) findViewById(R.id.answerBtn);
		answerET = (EditText) findViewById(R.id.answerET);

		Com_MainList comments = new Com_MainList();

		CommentsItemArray = comments.MakeArrayList(Maint_XMLParserClass.q10.get(Index)); //Because we need to use the object of the item opened from the clicked index to create an arraylist of its answer and comments form com_mainlist makearraylist
		myComAdapter = new Com_MyCustomBaseAdapter(getApplicationContext(), CommentsItemArray);
		final App_Tools tools = new App_Tools(); // because we need to generate random object id for new added comments

		answerBtn.setOnClickListener(new View.OnClickListener() { //button to send the answer
			@Override
			public void onClick(View v) {
				answertxt = answerET.getText().toString();
				if (answertxt.length()>15) { //because we dont want the user to send empty or short comments
					comment_objectrandomid = tools.randomString();
					comment_fullnameauthor = Sign_User_Object.Name;
					comment_emailauthor = Sign_User_Object.Email;
					comment_parentid = Maint_XMLParserClass.q10.get(Index); //because q12 is xml parser class is the object id
					comment_category = "Answer";
					comment_description = answertxt;
					comment_col8 = "";
					comment_col9 = "";
					comment_col7 = "";
					send_comment_thread.start(); //because after the user click on the button send, the vote_thread will fill the google form
					Com_GetDataAsyncTask commentTaskBackGround = new Com_GetDataAsyncTask(); // because we cannot make it static, getData() is already inside it and cannot be called it is static
					commentTaskBackGround.synchronize(); // because we want to synchronizeAsyncTask the background, even if we dont use it immediately
					Com_ItemObject newCom = new Com_ItemObject(); //because we want to create a newcomment object to add to the list of comm
					newCom.setBottomText(answertxt);
					newCom.setName(comment_emailauthor);

					CommentsItemArray.add(newCom);
					myComAdapter.notifyDataSetChanged();                //because when the button is clicked, we need to refresh activity in order to display the new anser

					answerET.setText(""); //because we want to remove the text after click
				}else {
					Toast.makeText(getApplicationContext(), "Your answer doesn't meet the minium requirement: Too Short", Toast.LENGTH_SHORT).show();

				}
			}
		});
		answer_lv.setAdapter(myComAdapter);
		send_comment_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					//String url for database Mauritius
					String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSeCfU1g8cKE5m0XZeurkFrRWBz8z8mzyVg7B1SxLWk41z2_1g/formResponse";
					HttpRequest mReq = new HttpRequest();
					String data = "entry.241491841=" + URLEncoder.encode(comment_objectrandomid) + "&" +
							"entry.700251506=" + URLEncoder.encode(comment_fullnameauthor) + "&" +
							"entry.583468840=" + URLEncoder.encode(comment_emailauthor) + "&" +
							"entry.1883075296=" + URLEncoder.encode(comment_parentid) + "&" +
							"entry.544598487=" + URLEncoder.encode(comment_category) + "&" +
							"entry.1951449669=" + URLEncoder.encode(comment_description) + "&" +
							"entry.733850032=" + URLEncoder.encode(comment_col8) + "&" +
							"entry.133876350=" + URLEncoder.encode(comment_col9) + "&" +
							"entry.691271547=" + URLEncoder.encode(comment_col7);
					String response = mReq.sendPost(fullUrl, data);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	private ArrayList<Maint_DetailListItem> getPersonalDetails(int Index) {
		ArrayList<Maint_DetailListItem> DetailList = new ArrayList<Maint_DetailListItem>();
		String nullTag = "Update your";

		Maint_DetailListItem sr = new Maint_DetailListItem();
		sr.setDetailName("Question");
		sr.setDetailValue(Maint_XMLParserClass.q2.get(Index));
		DetailList.add(sr);

		if (!(Maint_XMLParserClass.q5.get(Index).equals("")|| Maint_XMLParserClass.q5.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("Description");
			sr.setDetailValue(Maint_XMLParserClass.q5.get(Index));
			DetailList.add(sr);
		}

		if (!(Maint_XMLParserClass.q3.get(Index).equals("")|| Maint_XMLParserClass.q3.get(Index).startsWith(nullTag) )) {
		sr = new Maint_DetailListItem();
		sr.setDetailName("Asked by");
		sr.setDetailValue(Maint_XMLParserClass.q3.get(Index));
		DetailList.add(sr);
		}


		if (!(Maint_XMLParserClass.q6.get(Index).equals("")|| Maint_XMLParserClass.q6.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("Answer");
			sr.setDetailValue(Maint_XMLParserClass.q6.get(Index));
			DetailList.add(sr);
		}


		if ((Maint_XMLParserClass.q6.get(Index).equals("")|| Maint_XMLParserClass.q6.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("Answer");
			sr.setDetailValue("No answer yet");
			DetailList.add(sr);
		}


		if (!(Maint_XMLParserClass.q4.get(Index).equals("")|| Maint_XMLParserClass.q4.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("Organiser");
			sr.setDetailValue(Maint_XMLParserClass.q4.get(Index));
			DetailList.add(sr);
		}



		if (!(Maint_XMLParserClass.q7.get(Index).equals("")|| Maint_XMLParserClass.q7.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("Vote");
			sr.setDetailValue(Maint_XMLParserClass.q7.get(Index));
			DetailList.add(sr);
		}
		if (!(Maint_XMLParserClass.q8.get(Index).equals("")|| Maint_XMLParserClass.q8.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("passportnumber");
			sr.setDetailValue(Maint_XMLParserClass.q8.get(Index));
			DetailList.add(sr);
		}

		if (!(Maint_XMLParserClass.q9.get(Index).equals("")|| Maint_XMLParserClass.q9.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("phonenumber1");
			sr.setDetailValue(Maint_XMLParserClass.q9.get(Index));
			DetailList.add(sr);
		}
		if (!(Maint_XMLParserClass.q10.get(Index).equals("")|| Maint_XMLParserClass.q10.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("phonenumber2");
			sr.setDetailValue(Maint_XMLParserClass.q10.get(Index));
			DetailList.add(sr);
		}

		if (!(Maint_XMLParserClass.q11.get(Index).equals("")|| Maint_XMLParserClass.q11.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("phonenumber3");
			sr.setDetailValue(Maint_XMLParserClass.q11.get(Index));
			DetailList.add(sr);
		}

		if (!(Maint_XMLParserClass.q12.get(Index).equals("")|| Maint_XMLParserClass.q12.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("apartment");
			sr.setDetailValue(Maint_XMLParserClass.q12.get(Index));
			DetailList.add(sr);
		}
		if (!(Maint_XMLParserClass.q13.get(Index).equals("")|| Maint_XMLParserClass.q13.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("residence");
			sr.setDetailValue(Maint_XMLParserClass.q13.get(Index));
			DetailList.add(sr);
		}
		if (!(Maint_XMLParserClass.q14.get(Index).equals("")|| Maint_XMLParserClass.q14.get(Index).startsWith(nullTag) )) {
			sr = new Maint_DetailListItem();
			sr.setDetailName("room");
			sr.setDetailValue(Maint_XMLParserClass.q14.get(Index));
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
			String TempDetailName = (String) ((Maint_DetailListItem) arg0
					.getItemAtPosition(arg2)).getDetailName();
			if (TempDetailName.equals("Mobile")
					|| TempDetailName.equals("Residence")
					|| TempDetailName.equals("Office")) {
				String DetailValue = (String) ((Maint_DetailListItem) arg0
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
				String ToEmailId = (String) ((Maint_DetailListItem) arg0
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
