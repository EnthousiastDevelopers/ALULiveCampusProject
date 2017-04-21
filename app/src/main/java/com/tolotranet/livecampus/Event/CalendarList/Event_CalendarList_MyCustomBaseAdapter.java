package com.tolotranet.livecampus.Event.CalendarList;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import com.tolotranet.livecampus.R;

import java.util.ArrayList;

public class Event_CalendarList_MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<Event_CalendarList_ItemObject> MyArrayObjects = new ArrayList<>();
    private static ArrayList<Event_CalendarList_ItemObject> FilteredObjects = new ArrayList<>();
    private final Boolean[] ArrayStatesBoolean;
    private Context context;
    private LayoutInflater mInflater;
    private ItemFilter myFilter = new ItemFilter();

    public Event_CalendarList_MyCustomBaseAdapter(Context c, ArrayList<Event_CalendarList_ItemObject> MyList) {

        this.context = c;
        MyArrayObjects = MyList;
        FilteredObjects = MyList;
        mInflater = LayoutInflater.from(context);
        int n = MyList.size();
        ArrayStatesBoolean = new Boolean[n];

        for (int i = 0; i < n; i++) { //copy values of DetailistItem in two new ArrayString
            ArrayStatesBoolean[i] = MyList.get(i).getState();
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return FilteredObjects.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return FilteredObjects.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    @Override
    public int getItemViewType(int position) {
        return FilteredObjects.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public Filter getFilter() {
        return myFilter;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
       //Log.d("hello", "position is: " + String.valueOf(position));
        final int type = getItemViewType(position);

       //Log.d("hello","getView " + position + " " + convertView + " type = " + type);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type){
                case 0:
                    convertView = mInflater.inflate(R.layout.event_calendar_list_section, null);
                    holder = new ViewHolder();
                    holder.type = 0;
                    holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
                 break;
                case 1:
                    convertView = mInflater.inflate(R.layout.event_calendar_list_item, null);

                    holder.type = 1;
                    holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
                    holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
                    holder.MiniTV = (TextView) convertView.findViewById(R.id.Mini_bottom_tv);
                    holder.StateCheckBox = (CheckBox) convertView.findViewById(R.id.stateCheckBox);
                    break;
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;
        String Name = FilteredObjects.get(position).getName();

        if (type == 1) { //if is section
            String BottomText = FilteredObjects.get(position).getBottomText();
            String MiniText = FilteredObjects.get(position).getMiniText();
            String BackGround = FilteredObjects.get(position).getBackGroundColor();
            final int index = FilteredObjects.get(position).getIndex();

           //Log.d("hello", " and bg color is: " + BackGround);
           //Log.d("hello", " and mini text  is: " + MiniText);
            holder.NameTV.setText(Name);
            //holder.BottomTV.setText(BottomText);
            holder.MiniTV.setText(MiniText);
            final ViewHolder finalHolder = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.StateCheckBox.performClick();
                }
            });

            holder.StateCheckBox.setChecked(ArrayStatesBoolean[position]);
            holder.StateCheckBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor(BackGround)));
            //holder.StateCheckBox.setSupportButtonTintList(ColorStateList.valueOf(Color.parseColor(BackGround)));


            final ViewHolder finalHolder1 = holder;
            holder.StateCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayStatesBoolean[finalHolder1.ref] = !ArrayStatesBoolean[finalHolder1.ref];
                    if (ArrayStatesBoolean[finalHolder1.ref] == true) {
                        Event_CalendarList_XMLParserClass.q6.set(index, "true");
                    } else {
                        Event_CalendarList_XMLParserClass.q6.set(index, "false");
                    }
                }
            });
        } else { // if is a section
            holder.NameTV.setText(Name);
        }


        return convertView;
    }


    static class ViewHolder {
        TextView NameTV;
        TextView BottomTV;
        TextView MiniTV;
        CheckBox StateCheckBox;
        int type;
        int ref;

    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            // TODO Auto-generated method stub
            FilterResults filterResults = new FilterResults();
            if (arg0 == null || arg0.length() == 0) {
                filterResults.values = MyArrayObjects;
                filterResults.count = MyArrayObjects.size();
            } else {
                String filterString = arg0.toString().toLowerCase();
                final ArrayList<Event_CalendarList_ItemObject> TempList = new ArrayList<Event_CalendarList_ItemObject>();
                for (Event_CalendarList_ItemObject Sis_ItemObject : MyArrayObjects) {
                    //Filters both from Name and Bottom Text
                    if ((Sis_ItemObject.getName() + " " + Sis_ItemObject.getBottomText()).toLowerCase().contains(filterString)) {
                        TempList.add(Sis_ItemObject);
                    }
                }

                filterResults.values = TempList;
                filterResults.count = TempList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            // TODO Auto-generated method stub
            FilteredObjects = (ArrayList<Event_CalendarList_ItemObject>) arg1.values;
            notifyDataSetChanged();
        }

    }

}
