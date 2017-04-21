package com.tolotranet.livecampus.Maint;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tolotranet.livecampus.R;

import java.util.ArrayList;

public class Maint_MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<Maint_ItemObject> MyArrayObjects = new ArrayList<Maint_ItemObject>();
    private static ArrayList<Maint_ItemObject> FilteredObjects = new ArrayList<Maint_ItemObject>();
    private Context context;
    private LayoutInflater mInflater;
    private ItemFilter myFilter = new ItemFilter();

    public Maint_MyCustomBaseAdapter(Context c, ArrayList<Maint_ItemObject> MyList) {
        this.context = c;
        MyArrayObjects = MyList;
        FilteredObjects = MyList;

        mInflater = LayoutInflater.from(context);
        // TODO Auto-generated constructor stub

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

    public Filter getFilter() {
        return myFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.maint_list_item, null);
            holder = new ViewHolder();
            holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
            holder.RightTV = (TextView) convertView.findViewById(R.id.Right_tv);
            holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
            holder.RankTV = (TextView) convertView.findViewById(R.id.Rank_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String Name = FilteredObjects.get(position).getName();
        String BottomText = FilteredObjects.get(position).getBottomText();
        String State = FilteredObjects.get(position).getState();
        String Score = String.valueOf(FilteredObjects.get(position).getCommentsCount());

        holder.NameTV.setText(Name);
        holder.RightTV.setText(Score);
        holder.RankTV.setText(State);
        holder.BottomTV.setText(BottomText);
        setStateColorView(State, holder.RankTV);

        return convertView;
    }

    private void setStateColorView(String State, TextView stateTV) {
        int bg = 0;
        switch (State.toLowerCase()) {
            case "pending":
                bg = R.color.orange;
                break;
            case "closed":
                bg = R.color.red;
                break;
            case "seen":
                bg = R.color.green;
                break;
            default:
                bg = R.color.grey;
                break;
        }
        stateTV.setBackgroundColor(context.getResources().getColor(bg));
        stateTV.setTextColor(context.getResources().getColor(R.color.white));
    }

    static class ViewHolder {
        TextView NameTV;
        TextView RightTV;
        TextView BottomTV;
        TextView RankTV;
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
                final ArrayList<Maint_ItemObject> TempList = new ArrayList<Maint_ItemObject>();
                for (Maint_ItemObject Sis_ItemObject : MyArrayObjects) {
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
            FilteredObjects = (ArrayList<Maint_ItemObject>) arg1.values;
            notifyDataSetChanged();
        }

    }

}
