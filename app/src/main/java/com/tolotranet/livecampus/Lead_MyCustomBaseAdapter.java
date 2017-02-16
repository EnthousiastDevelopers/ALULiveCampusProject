package com.tolotranet.livecampus;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class Lead_MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<Lead_ItemObject> MyArrayObjects = new ArrayList<Lead_ItemObject>();
	private static ArrayList<Lead_ItemObject> FilteredObjects = new ArrayList<Lead_ItemObject>();
	private Context context;
	private LayoutInflater mInflater;
	private ItemFilter myFilter = new ItemFilter();
	
	public Lead_MyCustomBaseAdapter(Context c, ArrayList<Lead_ItemObject> MyList) {
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
			convertView = mInflater.inflate(R.layout.lead_list_item, null);
			holder = new ViewHolder();
			holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
			holder.RightTV = (TextView) convertView.findViewById(R.id.Right_tv);
			holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
			holder.RankTV = (TextView) convertView.findViewById(R.id.Rank_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String Name = FilteredObjects.get(position).getName();
		String BottomText = FilteredObjects.get(position).getBottomText();
		String Rank = String.valueOf(FilteredObjects.get(position).getIndex());
		String Score = String.valueOf(FilteredObjects.get(position).getScore());

		holder.NameTV.setText(Name);
		holder.RightTV.setText(Score);
		holder.RankTV.setText(Rank);
		holder.BottomTV.setText(BottomText);

		return convertView;
	}

	static class ViewHolder{
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
			if(arg0 == null || arg0.length() == 0){
				filterResults.values = MyArrayObjects;
				filterResults.count = MyArrayObjects.size();
			}else{
				String filterString =arg0.toString().toLowerCase();
				final ArrayList<Lead_ItemObject> TempList = new ArrayList<Lead_ItemObject>();
				for(Lead_ItemObject Sis_ItemObject : MyArrayObjects){
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
			FilteredObjects = (ArrayList<Lead_ItemObject>) arg1.values;
			notifyDataSetChanged();
		}
		
	}
	
}
