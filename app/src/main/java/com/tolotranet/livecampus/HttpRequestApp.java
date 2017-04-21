package com.tolotranet.livecampus;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.tolotranet.livecampus.Sis.Interact.Interaction_ItemObject;
import com.tolotranet.livecampus.Sis.Interact.Interaction_SendObject;

import java.net.URLEncoder;

/**
 * Created by Tolotra Samuel on 11/02/2017.
 */

public class HttpRequestApp extends AsyncTask<Activity, Void, Void> {
    String actionId;
    Activity Activity;
    private Interaction_SendObject interactionObject;

    public HttpRequestApp(String actionid) {
        this.actionId = actionid;
    }

    public void setInteractionObject(Interaction_SendObject interactionObj) {
        this.interactionObject = interactionObj;
    }

    @Override
    protected Void doInBackground(Activity... params) {
        this.Activity = params[0];
        switch (actionId) {
            case "addinteraction":
                add_Interaction(interactionObject);
        }
        return null;
    }

    private void add_Interaction(Interaction_SendObject interactionObject) {
        try {

            HttpRequest mReq = new HttpRequest();
            //Add score +1
            String scoreUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfmqfDvsxGM6PePVbdINlIHYe2OPd9GCwnSJa2UOuN2ksxQmw/formResponse";
            String data_score =
                    "entry.683575632=" + URLEncoder.encode(interactionObject.getSender()) + "&" + //action
                            "entry.2052192297=" + URLEncoder.encode(interactionObject.getRecipient()) + "&" + //object
                            "entry.136521820=" + URLEncoder.encode(String.valueOf(interactionObject.getId())) + "&" + //author
                            "entry.270042749=" + URLEncoder.encode(interactionObject.getName()) + "&" + //recipient
                            "entry.1655111766=" + URLEncoder.encode(interactionObject.getCategory()) + "&" + //score
                            "entry.568849419=" + URLEncoder.encode(interactionObject.getPrivacy());  //currency
            String response_score = mReq.sendPost(scoreUrl, data_score);
            Log.d("response", response_score);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addVote_Score(final String author,
                                     final String recipient,
                                     final String action,
                                     final String object,
                                     final String score,
                                     final String sub_action,
                                     final String currency
    ) {
        Thread scoreThread = new Thread(new Runnable() { //because we will add score and add vote are using the same google form, it will be used so many times, votes will be in subaction
            @Override
            public void run() {
                try {

                    HttpRequest mReq = new HttpRequest();
                    //Add score +1
                    String scoreUrl = "https://docs.google.com/forms/d/e/1FAIpQLScTo4QaRmnksgipbXf4OwUMBt2eofT2pah52KQLtq2K8TAK0w/formResponse";
                    String data_score =
                            "entry.683575632=" + URLEncoder.encode(author) + "&" + //author
                                    "entry.2052192297=" + URLEncoder.encode(recipient) + "&" + //recipient
                                    "entry.270042749=" + URLEncoder.encode(action) + "&" + //action
                                    "entry.136521820=" + URLEncoder.encode(object) + "&" + //object
                                    "entry.293560667=" + URLEncoder.encode(score) + "&" + //score
                                    "entry.1655111766=" + URLEncoder.encode(sub_action) + "&" + //sub-action because vote and score are using the same table
                                    "entry.568849419=" + URLEncoder.encode(currency);  //currency
                    String response_score = mReq.sendPost(scoreUrl, data_score);
                    Log.d("response", response_score);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        scoreThread.start();
    }

    ;

    public static void add_App_UsageTrack(final String author,
                                          final String appid,
                                          final String action,
                                          final String value,
                                          final String label
    ) {
        Thread usageThread = new Thread(new Runnable() { //because we will add score and add vote are using the same google form, it will be used so many times, votes will be in subaction
            @Override
            public void run() {
                try {

                    HttpRequest mReq = new HttpRequest();
                    //Add score +1
                    String scoreUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfuHisntBdxs9yJj8i7h8ORJ-CPAGB_Xy2ywLOIrSs-WrnRLg/formResponse";
                    String data_score =
                            "entry.651969059=" + URLEncoder.encode(author) + "&" + //author
                                    "entry.1234903408=" + URLEncoder.encode(appid) + "&" + //recipient
                                    "entry.1733490618=" + URLEncoder.encode(action) + "&" + //action
                                    "entry.349657667=" + URLEncoder.encode(value) + "&" +  //currency
                                    "entry.1681037110=" + URLEncoder.encode(label);  //currency
                    String response_score = mReq.sendPost(scoreUrl, data_score);
                    Log.d("response app usage", response_score);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        usageThread.start();
    }

    ;

    public void sendComment(final String comment_objectrandomid,
                            final String comment_fullnameauthor,
                            final String comment_emailauthor,
                            final String comment_parentid,
                            final String comment_category,
                            final String comment_description,
                            final String comment_col8,
                            final String comment_col9,
                            final String comment_col7
    ) {

        Thread send_comment_thread = new Thread(new Runnable() {

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
        send_comment_thread.start();
    }

    public static void add_Course_Attendance(final String cardID, final String myEmail, final String myFirstName, final String myLastName, final String course) {

        Thread send_attendance_record = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    //String url for database Mauritius

                    String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSez9DyP2JxxLLVFNfbAuLIDXOhS5T-75wlAF9JsSohiGQVy5Q/formResponse";
                    HttpRequest mReq = new HttpRequest();
                    String data = "entry.1979182813=" + URLEncoder.encode(cardID) + "&" +
                            "entry.516943837=" + URLEncoder.encode(myEmail) + "&" +
                            "entry.2118286285=" + URLEncoder.encode(myFirstName) + "&" +
                            "entry.1614196389=" + URLEncoder.encode(myLastName) + "&" +
                            "entry.977151147=" + URLEncoder.encode(course);
                    String response = mReq.sendPost(fullUrl, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        send_attendance_record.start();
    }


    public static void add_Food_Attendance(final String cardID
                                           , final String myEmail,
                                           final String myFirstName,
                                           final String myLastName,
                                           final String residence,
                                           final String optionLabel,
                                           final String option,
                                           final String category
                                           ) {
        Thread send_attendance_record = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //String url for database Mauritius

                    String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSdMok55r8FBk5NCm3pdx40zkpqty1H7L8Tzr5LsQBD6-LYFTA/formResponse";
                    HttpRequest mReq = new HttpRequest();
                    String data =
                            "entry.1979182813=" + URLEncoder.encode(cardID) + "&" +
                            "entry.516943837=" + URLEncoder.encode(myEmail) + "&" +
                            "entry.2118286285=" + URLEncoder.encode(myFirstName) + "&" +
                            "entry.1614196389=" + URLEncoder.encode(myLastName) + "&" +
                            "entry.1579080695=" + URLEncoder.encode(residence) + "&" +
                            "entry.687001300=" + URLEncoder.encode(option) + "&" +
                            "entry.977151147=" + URLEncoder.encode(optionLabel) + "&" +
                            "entry.466597607=" + URLEncoder.encode(category);
                    String response = mReq.sendPost(fullUrl, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        send_attendance_record.start();
    }
}
