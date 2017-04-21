package com.tolotranet.livecampus.Sis;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tolotranet.livecampus.R;

import java.util.ArrayList;

public class Sis_Interact_MySwipeDeck_BaseAdapter extends BaseAdapter {
    private static ArrayList<Sis_ItemObject> MyArrayObjects = new ArrayList<Sis_ItemObject>();
    private static ArrayList<Sis_ItemObject> FilteredObjects = new ArrayList<Sis_ItemObject>();
    private Context context;
    private LayoutInflater mInflater;
    private ItemFilter myFilter = new ItemFilter();

    public Sis_Interact_MySwipeDeck_BaseAdapter(Context c, ArrayList<Sis_ItemObject> MyList) {
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
    public Sis_ItemObject getItem(int arg0) {
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
            convertView = mInflater.inflate(R.layout.sis_interact_cardview_item, parent, false);
            holder = new ViewHolder();
            holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
            holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
            holder.imageView = (ImageView) convertView.findViewById(R.id.profile_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String Name = FilteredObjects.get(position).getName();
        String BottomText = FilteredObjects.get(position).getBottomText();

        holder.NameTV.setText(Name);
        holder.BottomTV.setText(BottomText);

        Picasso.with(context).load(R.drawable.sis_profil_neutral_large).fit().centerCrop().into(holder.imageView);


        return convertView;
    }

    static class ViewHolder {
        TextView NameTV;
        TextView BottomTV;
        ImageView imageView;
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
                final ArrayList<Sis_ItemObject> TempList = new ArrayList<Sis_ItemObject>();
                for (Sis_ItemObject Sis_ItemObject : MyArrayObjects) {
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
            FilteredObjects = (ArrayList<Sis_ItemObject>) arg1.values;
            notifyDataSetChanged();
        }

    }

}
