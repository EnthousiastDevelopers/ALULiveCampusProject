package com.tolotranet.livecampus;


import android.app.Activity;
import android.app.ProgressDialog;
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
    ProgressDialog mProgress;
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


                //Intent i = new Intent(Sign_Up_confirm.this, AppSelect.class);
//                i.putExtra("email", emailstr);
//                startActivity(i);
                Log.d("email is:", emailstr);
                 mProgress = new ProgressDialog(this);
                mProgress.setMessage("Loading your data ...");
                mProgress.show();
                AppSelect.origin = "signup"; //because the next activity cannot hold an extra, origin is used to redirect after the next activity
                helper.AllUserDataBaseToObject(); // because we jump through this step, now we need to initialize the user_object, or else it will be empty
                Sis_startApplicationAsyncTaskOwner myTask = new Sis_startApplicationAsyncTaskOwner();
                myTask.execute(this);

            }
        }
    }
    @Override
    protected void onDestroy() { //because there is a progress bar that opens before going onto the detailedlistviewowener
        super.onDestroy();
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

}
