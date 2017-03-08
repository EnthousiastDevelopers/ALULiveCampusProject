package com.tolotranet.livecampus.Voice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import com.tolotranet.livecampus.App.App_Contribute;
import com.tolotranet.livecampus.Booking.Booking_App;
import com.tolotranet.livecampus.Bubble.Bubble_SpreadSheetActivity;
import com.tolotranet.livecampus.Event.Event_SpreadSheetActivity;
import com.tolotranet.livecampus.Food.Food_MainList;
import com.tolotranet.livecampus.Food.Food_SpreadSheetActivity;
import com.tolotranet.livecampus.Gift.Gift_App;
import com.tolotranet.livecampus.Lead.Lead_SpreadSheetActivity;
import com.tolotranet.livecampus.Live.Live_App;
import com.tolotranet.livecampus.Maint.Maint_Add;
import com.tolotranet.livecampus.Maint.Maint_SpreadSheetActivity;
import com.tolotranet.livecampus.Mu.Mu_Add_Food;
import com.tolotranet.livecampus.Mu.Mu_App;
import com.tolotranet.livecampus.Nfc.Nfc_MainActivity;
import com.tolotranet.livecampus.Settings.Settings_mainActivity;
import com.tolotranet.livecampus.Sis.Sis_SpreadSheetActivity;
import com.tolotranet.livecampus.Sis.Sis_startApplicationAsyncTaskOwner;
import com.tolotranet.livecampus.Transp.Transp_SpreadSheetActivity;
import com.tolotranet.livecampus.Transp.Transp_TransApp;

/**
 * Created by Tolotra Samuel on 03/03/2017.
 */

public class Voice_Speech_Action extends Activity {
    private Activity context;
    private String text;
    private ProgressDialog mProgress;
    private String speechAction;

    public void Speech_Actions(Activity c, String text) {
        context = c;
        this.text = text;
        switch (text) {

            case "when is the next bus":
                startActivityPrompt("Transport Schedule");
                break;
            case "when is the bus leaving":
                startActivityPrompt("Transport Schedule");
                break;
            case "what's for lunch":
                speechAction = "lunch";
                startActivityPrompt("Get the menu of the week and give feedback");
                break;
            case "what's for dinner":
                speechAction = "dinner";
                startActivityPrompt("Get the menu of the week and give feedback");
                break;

            case "what's for breakfast":
                speechAction = "breakfast";
                startActivityPrompt("Get the menu of the week and give feedback");
                break;

            case "what's the snack today":
                speechAction = "snack";
                startActivityPrompt("Get the menu of the week and give feedback");
                break;

            case "i'm hungry":
                speechAction = "all";
                startActivityPrompt("Get the menu of the week and give feedback");
                break;
            case "what's happening now":
                startActivityPrompt("Events");
                break;
            case "start online sign up sheet":
                startActivityPrompt("Attendance");
                break;
            case "start sign up sheet":
                startActivityPrompt("Attendance");
                break;
            case "show me people":
                startActivityPrompt("Student Profiles");
                break;
            case "settings":
                startActivityPrompt("Settings");
                break;
            case "book a meeting":
                startActivityPrompt("Book a meeting");
                break;
            case "show me the academic calendar":
                startActivityPrompt("Booking");
                break;

            case "when is my next deadline":
                startActivityPrompt("Booking");
                break;
            case "what should i do today":
                startActivityPrompt("Booking");
                break;
            case "what should i do now":
                startActivityPrompt("Booking");
                break;


            default:
                Toast.makeText(context, "Sorry, no action associated to that, try with: when is the next bus", Toast.LENGTH_LONG).show();
                break;
        }


    }


    void startActivityPrompt(String Menu) {

        //open editor profile if the person clicked is the current user
        if (Menu == "Book a meeting") {
            Intent i = new Intent(context, Booking_App.class);
            context.startActivity(i);
        }
        if (Menu == "Food App") {
            Intent i = new Intent(context, App_Contribute.class);
            context.startActivity(i);
        }
        if (Menu == "Transport Schedule") {
            Intent i = new Intent(context.getApplicationContext(), Transp_SpreadSheetActivity.class);
            if (context != null) {
                Transp_TransApp.Origin = "Speech";
                Transp_TransApp.SpeechAction = "New Bus";
                context.startActivity(i);
            }
        }
        if (Menu == "Student Life Stream") {
            Intent i = new Intent(context, Live_App.class);
            context.startActivity(i);
        }
        if (Menu == "Zendesk Form") {
            Intent i = new Intent(context, Maint_Add.class);
            context.startActivity(i);

        }
        if (Menu == "Student Profiles") {
            Intent i = new Intent(context, Sis_SpreadSheetActivity.class);
            context.startActivity(i);
            // i.putExtra("myId", userID);
        }
        if (Menu == "Events") {
            Intent i = new Intent(context, Event_SpreadSheetActivity.class);
            context.startActivity(i);
        }
        if (Menu == "Settings") {
            Intent i = new Intent(context, Settings_mainActivity.class);
            context.startActivity(i);
        }
        if (Menu == "Gift") {
            Intent i = new Intent(context, Gift_App.class);
            context.startActivity(i);
        }
        if (Menu == "Me") {
            mProgress = new ProgressDialog(context);
            mProgress.setMessage("Loading data ...");
            mProgress.show();
            Sis_startApplicationAsyncTaskOwner myTask = new Sis_startApplicationAsyncTaskOwner();
            myTask.execute((Runnable) context);
        }
        if (Menu == "BubbleMarket") {
            Intent i = new Intent(context, Bubble_SpreadSheetActivity.class);
            context.startActivity(i);
        }
        if (Menu == "Attendance") {
            Intent i = new Intent(context, Nfc_MainActivity.class);
            context.startActivity(i);

        }
        if (Menu == "Leaderboard") {
            Intent i = new Intent(context, Lead_SpreadSheetActivity.class);
            context.startActivity(i);
        }
        if (Menu == "My Housing Queries") {
            Intent i = new Intent(context, Maint_SpreadSheetActivity.class);
            context.startActivity(i);
        }
        if (Menu == "Mauritius") {
            Intent i = new Intent(context, Mu_App.class);
            context.startActivity(i);
        }
//            if (Menu == "Submit ideas to improve the app") {
//                Intent i = new Intent(context, Mu_Add_Idea.class);
//                startActivityPrompt(i);
//            }
        if (Menu == "Get the menu of the week and give feedback") {
            Intent i = new Intent(context, Food_SpreadSheetActivity.class);
            Food_MainList.SpeechAction = speechAction;
            Food_MainList.Origin = "speech";
            Food_MainList.When = "";
            context.startActivity(i);
            //Food_App.mu_category_imgID = ThisImg;
        }

        if (Menu == "Suggest Food") {
            Intent i = new Intent(context, Mu_Add_Food.class);
            Mu_App.mu_category = "Food";
            context.startActivity(i);
        }

    }
}
