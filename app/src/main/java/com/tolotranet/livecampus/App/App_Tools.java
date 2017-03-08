package com.tolotranet.livecampus.App;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tolotra Samuel on 04/02/2017.
 */




public final class App_Tools extends Service {



    //Have to keep permissions for writing and reading external storage
    public static void StoreData( String filename, String input) throws IOException {
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive/backup");
        if (!Dir.exists()) {
            Dir.mkdir();
        }
        File file = new File(Dir, filename);
        FileOutputStream fileoutputstream = new FileOutputStream(file);
        fileoutputstream.write(input.getBytes());
        fileoutputstream.close();
        Log.d("hello", "writing complete "+input+" in directory "+filename);
    }


    public int getTimeStamp(){
        Calendar c = Calendar.getInstance();
        int timestamp = (int) c.getTimeInMillis();
        return timestamp;
    }
    public static String ReadData( String filename) throws IOException {
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");

        if (!Dir.exists()) {
            return "";
        }

        File newfile = new File(Dir, filename);
        FileInputStream fileinputstream = new FileInputStream(newfile);
        InputStreamReader inputreader = new InputStreamReader(fileinputstream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        StringBuffer stringbuffer = new StringBuffer();
        String ReadMessage;
        while (((ReadMessage = buffreader.readLine()) != null)) {
            stringbuffer.append(ReadMessage + "\n");
        }
        inputreader.close();

        return stringbuffer.toString();
    }



    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public String GiveMeTheNextId() {
         SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

   public String randomStringLength( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
    public String randomString(){
        StringBuilder sb = new StringBuilder( 10 );
        for( int i = 0; i < 10; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
    public static int randomInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public static boolean CompareDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yy h'h'mm");
        SimpleDateFormat dfDate2 = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");
        dfDate = formatter;
        dfDate2 = formatter;

        Date strDate = null;
        try {
            strDate = dfDate.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean b = false;
        if (System.currentTimeMillis() > strDate.getTime()) {
            //Log.d("hello","future");
        }

        try {
            if (dfDate.parse(startDate).before(dfDate2.parse(endDate))) {
                //Log.d("hello", "time is datab: "+(startDate)+"is before current time"+endDate );
                b = true;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate2.parse(endDate))) {
                b = false;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return b;
    }
    public static boolean CheckIfFirstBeforeLastDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yy kk'h'mm");
        SimpleDateFormat dfDate2 = dfDate;

        Date strDate = null;
        try {
            strDate = dfDate.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean b = false;
        if (System.currentTimeMillis() > strDate.getTime()) {
            //Log.d("hello","future");
        }

        try {
            if (dfDate.parse(startDate).before(dfDate2.parse(endDate))) {
                //Log.d("hello", "time is datab: "+(startDate)+"is before current time"+endDate );
                b = false;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate2.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = true; // If start date is after the end date.
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    public static long DifferenceDates(String startDate, String endDate, TimeUnit timeUnit) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yy h'h'mm");
        SimpleDateFormat dfDate2 = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");
        dfDate = formatter;
        dfDate2 = formatter;

        Date strDate = null;
        long diffInMillies = 0;
        try {
             diffInMillies  = dfDate.parse(startDate).getTime() - (dfDate2.parse(endDate)).getTime();
          return   timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
       return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }
    private boolean isDeviceOnline(Context c) {
        ConnectivityManager connMgr =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    public static String ConvertSecondToHHMMString(int secondtTime)
    {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondtTime*1000L));
        return time;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}