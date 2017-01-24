package com.example.tolotranet.livecampus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Tolotra Samuel on 16/08/2016.
 */
public class Sign_Up_confirm extends Activity{
    Sign_DatabaseHelper helper = new Sign_DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_confirm);

    }
    public void onSignUpClick (View v) {

        boolean passCheckBool = false;
        String emailstr = null;
        emailstr = getIntent().getStringExtra("email");

        if(v.getId() == R.id.Bsignupbutton) {
           // EditText name = (EditText)findViewById(R.id.TFname);
           // EditText email = (EditText)findViewById(R.id.TFemail);
//            EditText uname = (EditText)findViewById(R.id.TFuname);
            EditText pass1 = (EditText)findViewById(R.id.TFpass1);
            EditText pass2 = (EditText)findViewById(R.id.TFpass2);
            ToggleButton check_pass = (ToggleButton)findViewById(R.id.toggleButtonCheckPass) ;
            String x = check_pass.getText().toString();
            if(x.equals("ON")){
               passCheckBool = true;
            }
            if(x.equals("OFF")){
                passCheckBool = false;
            }
//            String namestr = name.getText().toString();
           // emailstr = emailTV.getText().toString();
           // String unamestr = uname.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();
            if (!pass1str.equals(pass2str)) {
             //popup message
                Toast pass = Toast.makeText(Sign_Up_confirm.this , "Passwords don't match!" , Toast.LENGTH_SHORT );
                pass.show();
            }else {
                //insert the details in database
                Sign_User_Object c = new Sign_User_Object();
//                c.setName(namestr);
                c.setUname(emailstr);
//
                c.setEmail(emailstr);
                c.setPass(pass1str);
                c.setPassCheck(passCheckBool);

                boolean log = helper.check_log_exist();
                if(!log) {
                    Log.d("passcheck", "insert new row (might be 0 or more than one already");
                    helper.insertContact(c);
                }if(log){
                    Log.d("passcheck", "insert new row 1 row already");
                    helper.updateUser(c);
                }

                Intent i = new Intent(Sign_Up_confirm.this, AppSelect.class);

                i.putExtra("email", emailstr);
                Log.d("email is:", emailstr);
                startActivity(i);
            }
        }
    }
}
