package com.tolotranet.livecampus;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Booking_MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<Booking_ItemObject> MyArrayObjects = new ArrayList<Booking_ItemObject>();
	private static ArrayList<Booking_ItemObject> FilteredObjects = new ArrayList<Booking_ItemObject>();
	private Context context;
	private LayoutInflater mInflater;
	private ItemFilter myFilter = new ItemFilter();

	public Booking_MyCustomBaseAdapter(Context c, ArrayList<Booking_ItemObject> MyList) {
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
			convertView = mInflater.inflate(R.layout.booking_list_item_single, null);
			holder = new ViewHolder();
			holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
			holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
			holder.ImgV = (ImageView) convertView.findViewById(R.id.PersonImageView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String Name = FilteredObjects.get(position).getName();
		String BottomText = FilteredObjects.get(position).getBottomText();
		int ImgId = FilteredObjects.get(position).getImgId();


		holder.NameTV.setText(Name);
		holder.BottomTV.setText(BottomText);
		holder.ImgV.setImageResource(ImgId);

		return convertView;
	}

	static class ViewHolder{
		TextView NameTV;
		TextView BottomTV;
		ImageView ImgV;
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
				final ArrayList<Booking_ItemObject> TempList = new ArrayList<Booking_ItemObject>();
				for(Booking_ItemObject ItemObject : MyArrayObjects){
					//Filters both from Name and Bottom Text
					if((ItemObject.getName() +" "+ItemObject.getBottomText()).toLowerCase().contains(filterString)){
						TempList.add(ItemObject);
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
			FilteredObjects = (ArrayList<Booking_ItemObject>) arg1.values;
			notifyDataSetChanged();
		}
		
	}
	
}
