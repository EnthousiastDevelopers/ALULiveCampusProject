package com.tolotranet.livecampus;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class Com_DetailListView extends Activity {

    ListView lv;
    Com_DetailListViewAdapter myPersonDetailListViewAdapter;
    ArrayList<Com_DetailListItem> DetailList;
    private int voteState = 0;
    private int voteStateOrigin = 0;
    ImageView downvote;
    ImageView upvote;

    TextView VoteCount, VoteCountLabel;

    String col1_s = "";//author
    String col2_s = "";//recipient
    String col3_s = "";//action
    String col4_s = "";//object
    String col5_s = "";//score
    String col6_s = "";  //currency
    String col7_s = "";  //sub-action
    public Thread thread = null;
    private ListView answer_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_activity_detail_list_view);
        Log.d("hello", "view mode has started");
        Intent i = getIntent();
        int Index = i.getIntExtra("index", 0);
        DetailList = getPersonalDetails(Index);

        lv = (ListView) findViewById(R.id.person_details_lv);
        answer_lv = (ListView) findViewById(R.id.answer_lv);
        myPersonDetailListViewAdapter = new Com_DetailListViewAdapter(this, DetailList);

        lv.setAdapter(myPersonDetailListViewAdapter);
        lv.setOnItemClickListener(new PersonDetailListViewClickListener());

    //    answer_lv.setAdapter(myPersonDetailListViewAdapterComment);

        upvote = (ImageView) findViewById(R.id.upvote);
        downvote = (ImageView) findViewById(R.id.downvote);
        VoteCount = (TextView) findViewById(R.id.VoteCount);
        VoteCountLabel = (TextView) findViewById(R.id.VoteCountLabel);
        VoteCount.setText(Com_XMLParserClass.q7.get(Index));// because we need to set the number of vote bigger and dynamic
        VoteCount.addTextChangedListener(new TextWatcher() { //because votecount textview changes everytime the arrows are clicked
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Integer.parseInt(VoteCount.getText().toString()) > 1) {
                    VoteCountLabel.setText("Votes");
                } else {
                    VoteCountLabel.setText("Vote");
                }
            }
        });
        upvote.setOnClickListener(new OnVotePressed());
        downvote.setOnClickListener(new OnVotePressed());

        col1_s = Sign_User_Object.Email; //because the point is from
        col3_s = "Voted";
        col2_s = Com_XMLParserClass.q13.get(Index); //because q13 is the author of this post that receive the votes
        col4_s = Com_XMLParserClass.q12.get(Index); //because q12 is the object unique id
        col5_s = "0"; // because we initialise it, it will be changed in when the user click the arrows
        col6_s = "Normal";

        if ((Com_XMLParserClass.q14.get(Index).equals(""))) { //because q14 should contains the list of the voters from google sheet script, "" means no one voted it before, so there is no need to find the user's voteState since it doesnt exist
            voteState = 0; //because the ui needs a variable to be set up
            Log.d("hello", "no list of voters");
        } else {
            try { //because is the json parsing might be incorrect

                JSONArray arrayJson = new JSONArray(Com_XMLParserClass.q14.get(Index)); //because the json received start with [ ena with ], its an array of objects with propriety

                outerloop:
                for (int n = 0; n < arrayJson.length(); n++) { //because the length is the number of unique user who voted this
                    JSONObject theobj = arrayJson.getJSONObject(n); //because it
                    if (theobj.getString("author").equals(Sign_User_Object.Email)) {
                        int scoreGiven = theobj.getInt("score");
                        voteState = scoreGiven; //because voteState is the switch we need to define wether the user has voted up, voted down or unvoted
                        voteStateOrigin = scoreGiven; //because voteState is the switch we need to define wether the user has voted up, voted down or unvoted
                        Log.d("hello", String.valueOf(scoreGiven) + Com_XMLParserClass.q14.get(Index));
                        break outerloop; //because we found the user and its score given
                    }
                    Log.d("hello", "your email not found in json");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (voteState == 0) {
            setColor("none");
        } else if (voteState >= 1) {
            setColor("up");
        } else if (voteState <= -1) {
            setColor("down");
        }

         thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpRequest mReq = new HttpRequest();
                    //Add score +1
                    String scoreUrl = "https://docs.google.com/forms/d/e/1FAIpQLScTo4QaRmnksgipbXf4OwUMBt2eofT2pah52KQLtq2K8TAK0w/formResponse";
                    String data_score =
                            "entry.683575632=" + URLEncoder.encode(col1_s) + "&" + //author
                                    "entry.2052192297=" + URLEncoder.encode(col2_s) + "&" + //recipient
                                    "entry.270042749=" + URLEncoder.encode(col3_s) + "&" + //action
                                    "entry.136521820=" + URLEncoder.encode(col4_s) + "&" + //object
                                    "entry.293560667=" + URLEncoder.encode(col5_s) + "&" + //score
                                    "entry.1655111766=" + URLEncoder.encode(col7_s) + "&" + //sub-action
                                    "entry.568849419=" + URLEncoder.encode(col6_s);  //currency
                    String response_score = mReq.sendPost(scoreUrl, data_score);
                    Log.d("response", response_score);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private ArrayList<Com_DetailListItem> getPersonalDetails(int Index) {
        ArrayList<Com_DetailListItem> DetailList = new ArrayList<Com_DetailListItem>();
        String nullTag = "Update your";

        Com_DetailListItem sr = new Com_DetailListItem();
        sr.setDetailName("Question");
        sr.setDetailValue(Com_XMLParserClass.q2.get(Index));
        DetailList.add(sr);

        if (!(Com_XMLParserClass.q5.get(Index).equals("") || Com_XMLParserClass.q5.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("Description");
            sr.setDetailValue(Com_XMLParserClass.q5.get(Index));
            DetailList.add(sr);
        }

        if (!(Com_XMLParserClass.q3.get(Index).equals("") || Com_XMLParserClass.q3.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("Asked by");
            sr.setDetailValue(Com_XMLParserClass.q3.get(Index));
            DetailList.add(sr);
        }


        if (!(Com_XMLParserClass.q6.get(Index).equals("") || Com_XMLParserClass.q6.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("Answer");
            sr.setDetailValue(Com_XMLParserClass.q6.get(Index));
            DetailList.add(sr);
        }


        if ((Com_XMLParserClass.q6.get(Index).equals("") || Com_XMLParserClass.q6.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("Answer");
            sr.setDetailValue("No answer yet");
            DetailList.add(sr);
        }


        if (!(Com_XMLParserClass.q4.get(Index).equals("") || Com_XMLParserClass.q4.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("Organiser");
            sr.setDetailValue(Com_XMLParserClass.q4.get(Index));
            DetailList.add(sr);
        }


        if (!(Com_XMLParserClass.q7.get(Index).equals("") || Com_XMLParserClass.q7.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("Vote");
            sr.setDetailValue(Com_XMLParserClass.q7.get(Index));
            DetailList.add(sr);
        }
        if (!(Com_XMLParserClass.q8.get(Index).equals("") || Com_XMLParserClass.q8.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("passportnumber");
            sr.setDetailValue(Com_XMLParserClass.q8.get(Index));
            DetailList.add(sr);
        }

        if (!(Com_XMLParserClass.q9.get(Index).equals("") || Com_XMLParserClass.q9.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("phonenumber1");
            sr.setDetailValue(Com_XMLParserClass.q9.get(Index));
            DetailList.add(sr);
        }
        if (!(Com_XMLParserClass.q10.get(Index).equals("") || Com_XMLParserClass.q10.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("phonenumber2");
            sr.setDetailValue(Com_XMLParserClass.q10.get(Index));
            DetailList.add(sr);
        }

        if (!(Com_XMLParserClass.q11.get(Index).equals("") || Com_XMLParserClass.q11.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("phonenumber3");
            sr.setDetailValue(Com_XMLParserClass.q11.get(Index));
            DetailList.add(sr);
        }

        if (!(Com_XMLParserClass.q12.get(Index).equals("") || Com_XMLParserClass.q12.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("apartment");
            sr.setDetailValue(Com_XMLParserClass.q12.get(Index));
            DetailList.add(sr);
        }
        if (!(Com_XMLParserClass.q13.get(Index).equals("") || Com_XMLParserClass.q13.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("residence");
            sr.setDetailValue(Com_XMLParserClass.q13.get(Index));
            DetailList.add(sr);
        }
        if (!(Com_XMLParserClass.q14.get(Index).equals("") || Com_XMLParserClass.q14.get(Index).startsWith(nullTag))) {
            sr = new Com_DetailListItem();
            sr.setDetailName("room");
            sr.setDetailValue(Com_XMLParserClass.q14.get(Index));
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
            String TempDetailName = (String) ((Com_DetailListItem) arg0
                    .getItemAtPosition(arg2)).getDetailName();
            if (TempDetailName.equals("Mobile")
                    || TempDetailName.equals("Residence")
                    || TempDetailName.equals("Office")) {
                String DetailValue = (String) ((Com_DetailListItem) arg0
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
                String ToEmailId = (String) ((Com_DetailListItem) arg0
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

    private void setColor(String state) {

        switch (state) {
            case "up":
                upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                downvote.clearColorFilter();
                Log.d("hello", "color up");
                return;
            case "none":
                downvote.clearColorFilter();
                upvote.clearColorFilter();
                Log.d("hello", "color none");
                return;
            case "down":
                downvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                upvote.clearColorFilter();
                Log.d("hello", "color down");
                return;
        }

    }

    private class OnVotePressed implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            thread.interrupt();
            if (v == upvote) {
                if (voteState == 1) {
                    voteState = 0;
                    setColor("none");
                    thread.start();
                } else {
                    voteState = 1;
                    setColor("none"); // because it might be -1 or 0 and down is still alive
                    setColor("up");
                }

            }
            if (v == downvote) {
                if (voteState == -1) {
                    voteState = 0;
                    setColor("none");
                } else {
                    voteState = -1;
                    setColor("none"); //because it might be +1 or 0 and up is still alive
                    setColor("down");
                }

            }
            if(voteStateOrigin== 0) {
                col7_s = String.valueOf(voteState);

            }else if(voteStateOrigin == 1){
                col7_s = String.valueOf(voteState-1);
            }else if (voteStateOrigin == -1){
                col7_s = String.valueOf(voteState+1);
            }
            Log.d("hello", col7_s);
            col5_s = String.valueOf(Integer.parseInt( col7_s)*15);
            voteStateOrigin = voteState;
            thread.start();
        }
    }
}
