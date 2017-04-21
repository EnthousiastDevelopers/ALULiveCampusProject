package com.tolotranet.livecampus.Nfc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tolotranet.livecampus.R;

/**
 * Created by Tolotra Samuel on 19/03/2017.
 */

public class Nfc_Food_Fragment extends Fragment {
    private View rootView;
    private final String ARG_SECTION_NUMBER = "section_number";
    private int position;
    private TextView foodTV;
    private String lastFood;
    private ImageView editBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.nfc_food_fragment_main, null);
        getArgumentsFromAdapter();

        Log.d("hello", "postition is:" + position);
        instantiateLayers();
        setUpLayoutListeners();

        return rootView;
    }

    private void setUpLayoutListeners() {
        editBtn.setOnClickListener(new EditOnclickListerner());
    }

    private void instantiateLayers() {
        foodTV = (TextView) rootView.findViewById(R.id.courseTV);
        foodTV.setText(lastFood);
        editBtn = (ImageView) rootView.findViewById(R.id.editBtn);

    }

    private void getArgumentsFromAdapter() {
        position = getArguments().getInt(ARG_SECTION_NUMBER); //this position is wrong, is another string marker
        lastFood = getArguments().getString(ARG_SECTION_NUMBER);
    }

    private static boolean m_iAmVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;

        if (m_iAmVisible) {
            Log.d("hello", "this fragment is now visible " + String.valueOf(position));
            if (lastFood == null) {
                //making sure there is no null execption
                lastFood = getArguments().getString(ARG_SECTION_NUMBER);
            }
            ((Nfc_MainActivity_Food) getActivity()).setFoodLabel(this.lastFood);
        } else {
            Log.d("hello", "this fragment is now invisible " + String.valueOf(position));
        }
    }

    private class EditOnclickListerner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showPopupFoodrInput("");
        }
    }

    private void showPopupFoodrInput(final String Cardid) {

        final EditText modifyET = new EditText(getActivity()); //create new edittext programatically
        final String Titlelabel = "Food";//because we need to put the label to put on the dialog

        modifyET.setHint("Enter the food name");
        modifyET.setText(lastFood);
        modifyET.setSelection(modifyET.getText().length()); // set the cursor at the end of the edittext immediately
        modifyET.setTextColor(getResources().getColor(R.color.black));
        new AlertDialog.Builder(getActivity())
                .setTitle(Titlelabel)
                .setView(modifyET).setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                // mProgress.show(); //start upload
                                String newFood = modifyET.getText().toString();
                                if (!newFood.equals(lastFood)) {
                                    foodTV.setText(newFood);
                                    lastFood = newFood;
                                    ((Nfc_MainActivity_Food) getActivity()).setFoodLabel(lastFood);
                                    //back up recordThisTag before changing food
                                }
                                //recursion, we just made sure the card is regitered to one email address
                            }//end onClisk yes

                        }//end on yes clicked           //end of if yes clicked on alert dialog
                ).create().show();   //show the alert dialog
    }
}
