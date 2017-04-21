package com.tolotranet.livecampus.Nfc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tolotranet.livecampus.R;

/**
 * Created by Tolotra Samuel on 19/03/2017.
 */

public class Nfc_bank_fragment extends Fragment {
    private View rootView;
    private final String ARG_SECTION_NUMBER = "section_number";
    private final String ARG_SECTION_TITLE = "section_title";
    private EditText amountET;
    private int position;
    private String sectionTitle;
    private EditText transactionBuddyET;
    private Button transactBtn;
    private MyTextWatcher mytextwatcher;
    private boolean edittedProgramatically = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nfc_bank_fragment, null);

        getArgumentsFromAdapter();
        createAbstractElements();
        //setUpSharedDrawerLayout();
        //includeLayoutContent();
        instantiateLayers();

        setUpLayoutListeners();
        //setUpLayoutAdapters();

        setUpUiAlgorythm();

        //getArgumentsFromAdapter();

        return rootView;
    } //end onCreate

    private void createAbstractElements() {
        mytextwatcher = new MyTextWatcher();
    }

    private void getArgumentsFromAdapter() {
        position = getArguments().getInt(ARG_SECTION_NUMBER);
        sectionTitle = getArguments().getString(ARG_SECTION_TITLE);
    }

    private void setUpUiAlgorythm() {
        amountET.setSelection(amountET.getText().length()); // set the cursor at the end of the edittext immediately
        Log.d("hello position pageAdpt", String.valueOf(position) + sectionTitle);
        switch (position) {
            case 0:
                // amountET.setHint("Request money from...");
                break;
            case 1:
                transactBtn.setText("REQUEST");
                transactionBuddyET.setHint("Request money from...");
                break;
        }

    }

    private void setUpLayoutListeners() {
        //amountET.
        amountET.addTextChangedListener(mytextwatcher);
        transactionBuddyET.setOnClickListener(new MyOnClickListener());
    }



    private void instantiateLayers() {
        amountET = (EditText) rootView.findViewById(R.id.amountET);
        transactionBuddyET = (EditText) rootView.findViewById(R.id.transactionBuddyET);
        transactBtn = (Button) rootView.findViewById(R.id.transactBtn);
    }

    public class MyTextWatcher implements TextWatcher {
        //the goal is to show 0 at mininum and never show a number starting with 0
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s,
                                      int start,
                                      int count,
                                      int after) {
            // TODO Auto-generated method stub
            // Log.d("hello before change", s.toString()+start+count+after);
        }

        @Override
        public void onTextChanged(CharSequence s,
                                  int start,
                                  int count,
                                  int after) {
            // TODO Auto-generated method stub
            if (edittedProgramatically) {
                return;
            }
            String currentTxt = (amountET.getText().toString());
            if (currentTxt.toString().equals("")) {
                edittedProgramatically = true;
                amountET.setText("0");
                amountET.setSelection(1);
                edittedProgramatically = false;
            }
            if (s.toString().startsWith("0")) {
                edittedProgramatically = true;
                amountET.setText(s.toString().substring(1));
                amountET.setSelection(after);
                edittedProgramatically = false;
            }
            Log.d("hello", s.toString() + start + count + after);
        }

    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),Nfc_Bank_BuddyList_Picker.class);
            startActivity(i);

        }
    }
}
