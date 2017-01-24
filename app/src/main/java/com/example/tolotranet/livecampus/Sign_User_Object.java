package com.example.tolotranet.livecampus;

/**
 * Created by Tolotra Samuel on 16/08/2016.
 */
public class Sign_User_Object {

    String name, email, uname, pass;
    boolean passCheck;

    public void setName (String name) {
        this.name = name;
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
