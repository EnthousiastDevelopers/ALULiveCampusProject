package com.tolotranet.livecampus;


import android.util.Log;

import java.net.URLEncoder;

/**
 * Created by Tolotra Samuel on 11/02/2017.
 */

public class HttpRequestApp {
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

}
