package com.tolotranet.livecampus;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tolotra Samuel on 16/08/2016.
 */
public class Sign_DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "contacts.db";
    private static final String TABLE_NAME = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LASTNAME = "lastname";
    private static final String COLUMN_FIRSTNAME ="firstname" ;
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_RESIDENCE = "residence";
    private static final String COLUMN_ROOM_NUMBER = "roomnumber";
    private static final String COLUMN_APARTMENT = "apartment";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_NEXT_GIFT_DATE = "nextgiftdate";
    private static final String COLUMN_PASS_CHECK = "passcheck";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_RANK = "rank";
    private static final String COLUMN_PASS = "pass";
    private static final String TABLE_CREATE = "create table contacts (id integer primary key not null , " +
            "lastname text  null ,firstname text  null ,name text  null, residence text  null,roomnumber text  null ," +
            "apartment text  null, phone text  null,    email text  null , nextgiftdate text null, uname text not null," +
            " pass text not null, passcheck boolean null, score text  null, rank text  null);";

    SQLiteDatabase db;

    public Sign_DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }
    public  void insertContact(Sign_User_Object c)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_PASS_CHECK, c.getPassCheck());
        values.put(COLUMN_NAME, c.getName());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_NEXT_GIFT_DATE, c.getNextGiftDate());
        values.put(COLUMN_UNAME, c.getUname());
        values.put(COLUMN_PASS, c.getPass());

        db.insert(TABLE_NAME, null, values);
        db.close();
    };


    public  void cleanTable() {
        db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public  void updateUser(Sign_User_Object c)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_PASS_CHECK, c.getPassCheck());
        values.put(COLUMN_NAME, c.getName());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_NEXT_GIFT_DATE, c.getNextGiftDate());
        values.put(COLUMN_UNAME, c.getUname());
        values.put(COLUMN_PASS, c.getPass());

        db.update(TABLE_NAME,values, null, null);
        db.close();
    };
    public  void AllUserObjectToDataBase()
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();



        values.put(COLUMN_ID, Sign_User_Object.Id);
        values.put(COLUMN_PASS_CHECK,     Sign_User_Object.PassCheck  );
        values.put(COLUMN_PASS,          Sign_User_Object.Pass );
        values.put(COLUMN_NAME,  Sign_User_Object.Name );
        values.put(COLUMN_EMAIL, Sign_User_Object.Email);
        values.put(COLUMN_UNAME,  Sign_User_Object.Uname );
        values.put(COLUMN_RESIDENCE, Sign_User_Object.Residence );
        values.put(COLUMN_ROOM_NUMBER, Sign_User_Object.RoomNumber);
        values.put(COLUMN_APARTMENT,Sign_User_Object.Apartment);
        values.put(COLUMN_LASTNAME,Sign_User_Object.Lastname);
        values.put(COLUMN_FIRSTNAME,Sign_User_Object.Firstname);
        values.put(COLUMN_PHONE, Sign_User_Object.Phone );
        values.put(COLUMN_RANK, Sign_User_Object.Rank);
        values.put(COLUMN_SCORE, Sign_User_Object.Score );

        db.update(TABLE_NAME,values, null, null);
        db.close();
    };

    public  void updateNextGiftDate(String nextdate)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_NEXT_GIFT_DATE, nextdate);

        db.update(TABLE_NAME,values, null, null);
        db.close();
    };


    public  void switch_sign_check()
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean x = check_pass_requirement();

       boolean newpass = true;
        if(x){
            Log.d("hello swicth", "to no");
            newpass = false;
        }
        if(!x){
            Log.d("hello swicth", "to yes");
            newpass = true;
        }

        values.put(COLUMN_PASS_CHECK, newpass);
        db.update(TABLE_NAME,values, null, null);
        db.close();
    };

    public  void Update_user_ID(int id)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, id);
        db.update(TABLE_NAME,values, null, null);
        db.close();
    };

    public boolean check_log_exist(){
        db = this.getReadableDatabase();
        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        if(count == 1) {
            Log.d("passcheck", "log exists so true because count == 1");
            return true;
        }else{
            Log.d("passcheck", "database virgin, or log superior to one, so mistake so false");
            return  false;
        }
    };

    public  String getUserEmail(){
        Log.d("hello", "0");
        db = this.getReadableDatabase();
        String query = "select email,pass from contacts";
        Cursor cursor = db.rawQuery(query, null);
        String emailDB = "username@example.com";
        Log.d("hello", "1");
        if(cursor.moveToFirst()) {
            Log.d("hello", "2");
            do {
                emailDB = cursor.getString(0);
                Log.d("hello", "3");
                return emailDB;
            }
            while (cursor.moveToNext());
        }
        Log.d("hello", "4");
        return emailDB;
    };

    public  String getNextGiftDate(){

        db = this.getReadableDatabase();
        String query = "select nextgiftdate,email from contacts";
        Cursor cursor = db.rawQuery(query, null);
        String result = "0";
        if(cursor.moveToFirst()) {
            do {
                result = cursor.getString(0);
                return result;
            }
            while (cursor.moveToNext());
        }
        return result;
    };

    public  int getUserId(){
        Log.d("hello", "0");
        db = this.getReadableDatabase();
        String query = "select id from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int userid = 999998;
        Log.d("hello", "1");
        if(cursor.moveToFirst()) {
            Log.d("hello", "2");
            do {
                userid = cursor.getInt(0);
                Log.d("hello", "3");
                return userid;
            }
            while (cursor.moveToNext());
        }
        Log.d("hello", "4");
        return userid;
    };


    public boolean check_pass_requirement() {
        Log.d("passcheck requirement", "1");
        db = this.getReadableDatabase();
        String query = "select passcheck from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        Log.d("passcheck requirement", "11");
        int a;
        if(cursor.moveToFirst()) {
            do {
                a= cursor.getInt(0);
                Log.d("passcheck requirement", "111");
                if(a == 1){
                    Log.d("passcheck requirement", "passcheck equals true, go to sign in, password required");
                    return true;
                }
                if(a == 0){
                    Log.d("passcheck requirement", "passcheck equals false, go to appselect");
                    return false;
                }
            }
            while (cursor.moveToFirst());
        }
        Log.d("passcheck requirement", "no passcheck found, so true, go to sign up");
        return true;
    }

    public String  searchPass(String uname) {
        db = this.getReadableDatabase();
        String query = "select email, pass from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        String a, b;
        b = "not found";
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                b = cursor.getString(1);

                if (a.equals(uname)) {
                    b = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        return b;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    public void AllUserDataBaseToObject() {
        db = this.getReadableDatabase();
        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int userid = 999998;
        Log.d("hello", "1");
        if(cursor.moveToFirst()) {
            do {

                Sign_User_Object.Id = cursor.getInt(0);
                Sign_User_Object.Lastname = cursor.getString(1);
                Sign_User_Object.Firstname = cursor.getString(2);
                Sign_User_Object.Name = cursor.getString(3);
                Sign_User_Object.Residence = cursor.getString(4);
                Sign_User_Object.RoomNumber = cursor.getString(5);
                Sign_User_Object.Apartment = cursor.getString(6);
                Sign_User_Object.Phone = cursor.getString(7);
                Sign_User_Object.Email = cursor.getString(8);
                Sign_User_Object.NextGiftDate = cursor.getString(9);
                Sign_User_Object.Uname = cursor.getString(10);
                Sign_User_Object.Pass = cursor.getString(11);
                Sign_User_Object.PassCheck = cursor.getString(12);
                Sign_User_Object.Score = cursor.getString(13);
                Sign_User_Object.Rank = cursor.getString(14);

            }
            while (cursor.moveToNext());
        }
    }
}
