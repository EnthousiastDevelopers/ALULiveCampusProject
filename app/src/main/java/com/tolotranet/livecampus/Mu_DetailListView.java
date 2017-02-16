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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class Mu_DetailListView extends Activity {

    ListView lv;
    Mu_DetailListViewAdapter myPersonDetailListViewAdapter;
    ArrayList<Mu_DetailListItem> DetailList;
    private int voteState = 0;
    private int voteStateOrigin = 0;
    ImageView downvote;
    ImageView upvote;
    Button answerBtn;

    TextView VoteCount, VoteCountLabel;
    ArrayList<Com_ItemObject> CommentsItemArray;
    String col1_s = "";//author
    String col2_s = "";//recipient
    String col3_s = "";//action
    String col4_s = "";//object
    String col5_s = "";//score
    String col6_s = "";  //currency
    String col7_s = "";  //sub-action
    Thread vote_thread;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mu_activity_detail_list_view);
        Log.d("hello", "view mode has started");
        Intent i = getIntent();
        final int Index = i.getIntExtra("index", 0);
        DetailList = getPersonalDetails(Index);
        CommentsItemArray = Com_MainList.MakeArrayList(Mu_XMLParserClass.q12.get(Index)); //Because we need to use the object of the item opened from the clicked index to create an arraylist of its answer and comments form com_mainlist makearraylist

        lv = (ListView) findViewById(R.id.person_details_lv);
        answer_lv = (ListView) findViewById(R.id.answer_lv);
        answerBtn = (Button) findViewById(R.id.answerBtn);
        answerET = (EditText) findViewById(R.id.answerET);

        myPersonDetailListViewAdapter = new Mu_DetailListViewAdapter(this, DetailList);
        myComAdapter = new Com_MyCustomBaseAdapter(getApplicationContext(), CommentsItemArray);
        final App_Tools tools = new App_Tools(); // because we need to generate random object id for new added comments
        lv.setAdapter(myPersonDetailListViewAdapter);
        lv.setOnItemClickListener(new PersonDetailListViewClickListener());
        answerBtn.setOnClickListener(new View.OnClickListener() { //button to send the answer
            @Override
            public void onClick(View v) {
                answertxt = answerET.getText().toString();
                if (answertxt.length()>15) { //because we dont want the user to send empty or short comments
                    comment_objectrandomid = tools.randomString();
                    comment_fullnameauthor = Sign_User_Object.Name;
                    comment_emailauthor = Sign_User_Object.Email;
                    comment_parentid = Mu_XMLParserClass.q12.get(Index); //because q12 is xml parser class is the object id
                    comment_category = "Answer";
                    comment_description = answertxt;
                    comment_col8 = "";
                    comment_col9 = "";
                    comment_col7 = "";
                    send_comment_thread.start(); //because after the user click on the button send, the vote_thread will fill the google form
                    Com_GetDataAsyncTask commentTaskBackGround = new Com_GetDataAsyncTask(); // because we cannot make it static, getData() is already inside it and cannot be called it is static
                    commentTaskBackGround.synchronize(); // because we want to synchronize the background, even if we dont use it immediately
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

        upvote = (ImageView) findViewById(R.id.upvote);
        downvote = (ImageView) findViewById(R.id.downvote);
        VoteCount = (TextView) findViewById(R.id.VoteCount);
        VoteCountLabel = (TextView) findViewById(R.id.VoteCountLabel);
        VoteCount.setText(Mu_XMLParserClass.q7.get(Index));// because we need to set the number of vote bigger and dynamic
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
        col2_s = Mu_XMLParserClass.q13.get(Index); //because q13 is the author of this post that receive the votes
        col4_s = Mu_XMLParserClass.q12.get(Index); //because q12 is the object unique id
        col5_s = "0"; // because we initialise it, it will be changed in when the user click the arrows
        col6_s = "Normal";

        if ((Mu_XMLParserClass.q14.get(Index).equals(""))) { //because q14 should contains the list of the voters from google sheet script, "" means no one voted it before, so there is no need to find the user's voteState since it doesnt exist
            voteState = 0; //because the ui needs a variable to be set up
            Log.d("hello", "no list of voters");
        } else {
            try { //because is the json parsing might be incorrect

                JSONArray arrayJson = new JSONArray(Mu_XMLParserClass.q14.get(Index)); //because the json received start with [ ena with ], its an array of objects with propriety

                outerloop:
                for (int n = 0; n < arrayJson.length(); n++) { //because the length is the number of unique user who voted this
                    JSONObject theobj = arrayJson.getJSONObject(n); //because it
                    if (theobj.getString("author").equals(Sign_User_Object.Email)) {
                        int scoreGiven = theobj.getInt("score");
                        voteState = scoreGiven; //because voteState is the switch we need to define wether the user has voted up, voted down or unvoted
                        voteStateOrigin = scoreGiven; //because voteState is the switch we need to define wether the user has voted up, voted down or unvoted
                        Log.d("hello", String.valueOf(scoreGiven) + Mu_XMLParserClass.q14.get(Index));
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

        vote_thread = new Thread(new Runnable() {
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


    private ArrayList<Mu_DetailListItem> getPersonalDetails(int Index) {
        ArrayList<Mu_DetailListItem> DetailList = new ArrayList<Mu_DetailListItem>();
        String nullTag = "Update your";

        Mu_DetailListItem sr = new Mu_DetailListItem();
        sr.setDetailName("Question");
        sr.setDetailValue(Mu_XMLParserClass.q2.get(Index));
        DetailList.add(sr);

        if (!(Mu_XMLParserClass.q5.get(Index).equals("") || Mu_XMLParserClass.q5.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("Description");
            sr.setDetailValue(Mu_XMLParserClass.q5.get(Index));
            DetailList.add(sr);
        }

        if (!(Mu_XMLParserClass.q3.get(Index).equals("") || Mu_XMLParserClass.q3.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("Asked by");
            sr.setDetailValue(Mu_XMLParserClass.q3.get(Index));
            DetailList.add(sr);
        }


        if (!(Mu_XMLParserClass.q6.get(Index).equals("") || Mu_XMLParserClass.q6.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("Answer");
            sr.setDetailValue(Mu_XMLParserClass.q6.get(Index));
            DetailList.add(sr);
        }


        if ((Mu_XMLParserClass.q6.get(Index).equals("") || Mu_XMLParserClass.q6.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("Answer");
            sr.setDetailValue("No answer yet");
            DetailList.add(sr);
        }


        if (!(Mu_XMLParserClass.q4.get(Index).equals("") || Mu_XMLParserClass.q4.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("Organiser");
            sr.setDetailValue(Mu_XMLParserClass.q4.get(Index));
            DetailList.add(sr);
        }


        if (!(Mu_XMLParserClass.q7.get(Index).equals("") || Mu_XMLParserClass.q7.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("Vote");
            sr.setDetailValue(Mu_XMLParserClass.q7.get(Index));
            DetailList.add(sr);
        }
        if (!(Mu_XMLParserClass.q8.get(Index).equals("") || Mu_XMLParserClass.q8.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("passportnumber");
            sr.setDetailValue(Mu_XMLParserClass.q8.get(Index));
            DetailList.add(sr);
        }

        if (!(Mu_XMLParserClass.q9.get(Index).equals("") || Mu_XMLParserClass.q9.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("phonenumber1");
            sr.setDetailValue(Mu_XMLParserClass.q9.get(Index));
            DetailList.add(sr);
        }
        if (!(Mu_XMLParserClass.q10.get(Index).equals("") || Mu_XMLParserClass.q10.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("phonenumber2");
            sr.setDetailValue(Mu_XMLParserClass.q10.get(Index));
            DetailList.add(sr);
        }

        if (!(Mu_XMLParserClass.q11.get(Index).equals("") || Mu_XMLParserClass.q11.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("phonenumber3");
            sr.setDetailValue(Mu_XMLParserClass.q11.get(Index));
            DetailList.add(sr);
        }

        if (!(Mu_XMLParserClass.q12.get(Index).equals("") || Mu_XMLParserClass.q12.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("apartment");
            sr.setDetailValue(Mu_XMLParserClass.q12.get(Index));
            DetailList.add(sr);
        }
        if (!(Mu_XMLParserClass.q13.get(Index).equals("") || Mu_XMLParserClass.q13.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("residence");
            sr.setDetailValue(Mu_XMLParserClass.q13.get(Index));
            DetailList.add(sr);
        }
        if (!(Mu_XMLParserClass.q14.get(Index).equals("") || Mu_XMLParserClass.q14.get(Index).startsWith(nullTag))) {
            sr = new Mu_DetailListItem();
            sr.setDetailName("room");
            sr.setDetailValue(Mu_XMLParserClass.q14.get(Index));
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
            String TempDetailName = (String) ((Mu_DetailListItem) arg0
                    .getItemAtPosition(arg2)).getDetailName();
            if (TempDetailName.equals("Mobile")
                    || TempDetailName.equals("Residence")
                    || TempDetailName.equals("Office")) {
                String DetailValue = (String) ((Mu_DetailListItem) arg0
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
                String ToEmailId = (String) ((Mu_DetailListItem) arg0
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
            vote_thread.interrupt();
            if (v == upvote) {
                if (voteState == 1) {
                    voteState = 0;
                    setColor("none");
                    vote_thread.start();
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
            if (voteStateOrigin == 0) {
                col7_s = String.valueOf(voteState);

            } else if (voteStateOrigin == 1) {
                col7_s = String.valueOf(voteState - 1);
            } else if (voteStateOrigin == -1) {
                col7_s = String.valueOf(voteState + 1);
            }
            Log.d("hello", col7_s);
            col5_s = String.valueOf(Integer.parseInt(col7_s) * 15);
            voteStateOrigin = voteState;
            vote_thread.start();
        }
    }
}
