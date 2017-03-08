package com.tolotranet.livecampus.Nfc;

/**
 * Created by Tolotra Samuel on 02/03/2017.
 */


import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Nfc.nfc_record.ParsedNdefRecord;
import com.tolotranet.livecampus.Nfc.nfc_record.SmartPoster;
import com.tolotranet.livecampus.Nfc.nfc_record.TextRecord;
import com.tolotranet.livecampus.Nfc.nfc_record.UriRecord;
/*
This has nothing to do with nfc, it is just a UI display parser*/
public class Nfc_NdefMessageParser {

    // Utility class
    private Nfc_NdefMessageParser() {

    }

    /** Parse an NdefMessage */
    public static List<ParsedNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();
        for (final NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record));
            } else {
                elements.add(new ParsedNdefRecord() {
                    @Override
                    public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent, int offset) {
                        TextView text = (TextView) inflater.inflate(R.layout.nfc_tag_text, parent, false);
                        text.setText(new String(record.getPayload()));
                        return text;
                    }

                });
            }
        }
        return elements;
    }
}