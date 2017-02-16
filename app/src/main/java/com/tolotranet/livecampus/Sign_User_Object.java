package com.tolotranet.livecampus;


/**
 * Created by Tolotra Samuel on 16/08/2016.
 */
public class Sign_User_Object {

    public static int Id;
    public static String Lastname;
    public static String Firstname;
    public static String Name;
    public static String Residence;
    public static String RoomNumber;
    public static String Apartment;
    public static String Phone;
    public static String Email;
    public static String NextGiftDate;
    public static String Uname;
    public static String Pass;
    public static String PassCheck;
    public static String Rank;
    public static String Score;


    String name, email, uname, pass, nextgiftdate;
    boolean passCheck;

    public void setName (String name) {
        this.name = name;
    }
    public void setNextGiftDate (String name) {
        this.nextgiftdate = name;
    }
    public String getName() {
        return this.name;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }
    public String getNextGiftDate() {
        return this.nextgiftdate;
    }
    public void setUname (String uname) {
        this.uname = uname;
    }
    public String getUname() {
        return this.uname;
    }
    public void setPass (String pass) {
        this.pass = pass;
    }
    public String getPass() {
        return this.pass;
    }

    public void setPassCheck (boolean passCheck) {
        this.passCheck = passCheck;
    }
    public boolean getPassCheck() {
        return this.passCheck;
    }


}
