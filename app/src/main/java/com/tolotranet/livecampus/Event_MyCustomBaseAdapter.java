package com.tolotranet.livecampus;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class Event_MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<Event_ItemObject> MyArrayObjects = new ArrayList<Event_ItemObject>();
	private static ArrayList<Event_ItemObject> FilteredObjects = new ArrayList<Event_ItemObject>();
	private Context context;
	private LayoutInflater mInflater;
	private ItemFilter myFilter = new ItemFilter();
	
	public Event_MyCustomBaseAdapter(Context c, ArrayList<Event_ItemObject> MyList) {
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

	public Filter getFilter(){
		return myFilter;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.event_list_item, null);
			holder = new ViewHolder();
			holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
			holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
			holder.MiniTV = (TextView) convertView.findViewById(R.id.Mini_bottom_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String Name = FilteredObjects.get(position).getName();
		String BottomText = FilteredObjects.get(position).getBottomText();
		String MiniText = FilteredObjects.get(position).getMiniText();

		holder.NameTV.setText(Name);
		holder.BottomTV.setText(BottomText);
		holder.MiniTV.setText(MiniText);

		return convertView;
	}

	static class ViewHolder{
		TextView NameTV;
		TextView BottomTV;
		TextView MiniTV;
	}
	
	private class ItemFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence arg0) {
			// TODO Auto-generated method stub
			FilterResults filterResults = new FilterResults();
			if(arg0 == null || arg0.length() == 0){
				filterResults.values = MyArrayObjects;
				filterResults.count = MyArrayObjects.size();
			}else{
				String filterString =arg0.toString().toLowerCase();
				final ArrayList<Event_ItemObject> TempList = new ArrayList<Event_ItemObject>();
				for(Event_ItemObject Sis_ItemObject : MyArrayObjects){
					//Filters both from Name and Bottom Text
					if((Sis_ItemObject.getName() +" "+ Sis_ItemObject.getBottomText()).toLowerCase().contains(filterString)){
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
