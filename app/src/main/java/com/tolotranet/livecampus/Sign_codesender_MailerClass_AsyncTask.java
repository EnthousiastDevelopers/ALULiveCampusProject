package com.tolotranet.livecampus;


import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class Sign_codesender_MailerClass_AsyncTask extends AsyncTask {

    private static String USER_NAME = "alucampuslive";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "campuslive2016"; // GMail password
   /* public static String RECIPIENT = "tolotrasam@gmail.com";*/
   public static String RECIPIENT = "tolotrasam@gmail.com";
    public static String BODY = "no body";
    public static String SUBJECT = "no subject";
    Sign_check_mail x = new Sign_check_mail();
    int code;
    protected Object doInBackground(Object[] params) {
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { RECIPIENT }; // list of recipient email addresses
        String subject = SUBJECT;
        String body = BODY;

        sendFromGMail(from, pass, to, subject, body);
        return null;

    }
    public static void main(String[] args) {

    }

    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body, "utf-8", "html");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }

}