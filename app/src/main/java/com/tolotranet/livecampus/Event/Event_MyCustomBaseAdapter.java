package com.tolotranet.livecampus.Event;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.SectionAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Event_MyCustomBaseAdapter extends SectionAdapter {
    private static ArrayList<Event_ItemObject> MyArrayObjects = new ArrayList<Event_ItemObject>();
    private static ArrayList<Event_ItemObject> MyArraySection = new ArrayList<Event_ItemObject>();
    private static ArrayList<Event_ItemObject> FilteredObjects = new ArrayList<Event_ItemObject>();
    private final String todayString;
    private Context context;
    private LayoutInflater mInflater;
    private ItemFilter myFilter = new ItemFilter();
    final SimpleDateFormat formatterDateOnly;

    public Event_MyCustomBaseAdapter(Context c, ArrayList<Event_ItemObject> MyList, ArrayList<Event_ItemObject> MySection) {
        this.context = c;
        MyArrayObjects = MyList;
        FilteredObjects = MyList;
        MyArraySection = MySection;
        Calendar calendar = Calendar.getInstance();
        formatterDateOnly = new SimpleDateFormat("dd/MM/yy");
        todayString = formatterDateOnly.format(calendar.getTime());

        for (Event_ItemObject item : FilteredObjects) {
            // Log.d("hellomarlu", item.getCategory());

        }

        mInflater = LayoutInflater.from(context);
        // TODO Auto-generated constructor stub

    }

    //Total number of sections   (headers)
    @Override
    public int numberOfSections() {
        return MyArraySection.size();
    }

    //Number of rows assigned per section
    // param section is the index of the section ( it starts with 0)
    @Override
    public int numberOfRows(int section) {
        //weird bug, just hide it like that
        if (section == -1) {
            // Log.d("WTF", "why???");
            return 0;
        }
        return MyArraySection.get(section).getChildCount();
    }


    @Override
    public Object getRowItem(int section, int row) {
        return null;
    }

    //boolean if hasSection or just plain listview for the specific section?
    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }

    //number  of the sector header view style (not the items), start with 1
    @Override
    public int getSectionHeaderViewTypeCount() {
        return 2;
    }

    //style id of the sector header view (not the items), start with 0, cannot be more than section section header view type count
    @Override
    public int getSectionHeaderItemViewType(int section) {
        String TimeStamp = MyArraySection.get(section).getTimeStamp();
        if (TimeStamp.equals((todayString))) {
            return 1; // if today equals number in the getName, return 1 meaning color in blue
        }
        return 0; // else color in white
    }


    //get view for section
    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_section_list, null);
            holder = new ViewHolder();
            holder.NameTV = (TextView) convertView.findViewById(R.id.DayTV);
            holder.BottomTV = (TextView) convertView.findViewById(R.id.WeekDayTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String Name = MyArraySection.get(section).getName();
        String BottomText = MyArraySection.get(section).getBottomText();

        holder.NameTV.setText(Name);
        holder.BottomTV.setText(BottomText);
        if (getSectionHeaderItemViewType(section) == 1) {
            holder.BottomTV.setText(BottomText + " \nToday");
            holder.NameTV.setTextColor(context.getResources().getColor(R.color.blue));
            holder.BottomTV.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.NameTV.setTextColor(context.getResources().getColor(R.color.black));
            holder.BottomTV.setTextColor(context.getResources().getColor(R.color.black));
        }
        return convertView;
    }


    //get view for items in row
    @Override
    public View getRowView(int section, int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_list_item, null);
            holder = new ViewHolder();
            holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
            holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
            holder.MiniTV = (TextView) convertView.findViewById(R.id.Mini_bottom_tv);
            holder.textBgLT = (LinearLayout) convertView.findViewById(R.id.textBgLT);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String Name = FilteredObjects.get(position).getName();
        String BottomText = FilteredObjects.get(position).getBottomText();
        String MiniText = FilteredObjects.get(position).getMiniText();
        String bgColor = FilteredObjects.get(position).getBgColor();

        holder.NameTV.setText(Name);
        holder.BottomTV.setText(BottomText);
        holder.MiniTV.setText(MiniText);
        holder.textBgLT.setBackgroundColor(Color.parseColor(bgColor));

        // Log.d("hellomarlu", String.valueOf(position));
        return convertView;
    }

    static class ViewHolder {
        TextView NameTV;
        TextView BottomTV;
        TextView MiniTV;
        LinearLayout textBgLT;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub

        return arg0;
    }

    public Filter getFilter() {
        return myFilter;
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
                final ArrayList<Event_ItemObject> TempList = new ArrayList<Event_ItemObject>();
                for (Event_ItemObject Sis_ItemObject : MyArrayObjects) {
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
            FilteredObjects = (ArrayList<Event_ItemObject>) arg1.values;
            notifyDataSetChanged();
        }

    }

}
