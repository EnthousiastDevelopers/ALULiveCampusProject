package com.tolotranet.livecampus.Voice;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Voice_DialogActivity extends Activity {
    //this activity is an hack to allow the voice to be called within the service
    @Override
    public void onResume() {
        super.onResume();
    Log.d("hello", "onResume dialogacitivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hello", "onCreate dialogactivity");
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: When is the next Bus");
        try {
            startActivityForResult(i, 100);

        } catch (ActivityNotFoundException a) {
            Toast.makeText(Voice_DialogActivity.this, "Sorry, your device does not support speech language", Toast.LENGTH_SHORT);

        }
    }

    //this activity result give response of the startactivityforresult of voice recognition

    @Override
    public void onActivityResult(int request_code, int request_result, Intent i) {
        super.onActivityResult(request_code, request_result, i);
        switch (request_code) {
            case 100:
                if (request_result == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(Voice_DialogActivity.this, result.get(0), Toast.LENGTH_SHORT).show();
                    Voice_Speech_Action voiceSpeech_action = new Voice_Speech_Action();
                    voiceSpeech_action.Speech_Actions(Voice_DialogActivity.this, result.get(0).toLowerCase());
                }
                break;
        } //end case request_code 100
    }

}