package com.tolotranet.livecampus.Nfc;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tolotranet.livecampus.R;

import java.util.ArrayList;

public class Nfc_Record_Custom_BaseAdapter extends BaseAdapter {
	private static ArrayList<Nfc_Record_ItemObject> MyArrayObjects = new ArrayList<Nfc_Record_ItemObject>();
	private static ArrayList<Nfc_Record_ItemObject> FilteredObjects = new ArrayList<Nfc_Record_ItemObject>();
	private Context context;
	private LayoutInflater mInflater;
	private ItemFilter myFilter = new ItemFilter();

	public Nfc_Record_Custom_BaseAdapter(Context c, ArrayList<Nfc_Record_ItemObject> MyList) {
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
			convertView = mInflater.inflate(R.layout.nfc_people_item_single, null);
			holder = new ViewHolder();
			holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
			holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
			holder.BottomTV2 = (TextView) convertView.findViewById(R.id.botton_tv_2);
			holder.ImgV = (ImageView) convertView.findViewById(R.id.PersonImageView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String Name = FilteredObjects.get(position).getName();
		String BottomText = FilteredObjects.get(position).getBottomText();
		String BottomText_2 = FilteredObjects.get(position).getBottomText_2();
		int ImgId = FilteredObjects.get(position).getImgId();


		holder.NameTV.setText(Name);
		holder.BottomTV.setText(BottomText);
		holder.BottomTV2.setText(BottomText_2);
		holder.ImgV.setImageResource(ImgId);

		return convertView;
	}

	static class ViewHolder{
		TextView NameTV;
		TextView BottomTV;
		TextView BottomTV2;
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
				final ArrayList<Nfc_Record_ItemObject> TempList = new ArrayList<Nfc_Record_ItemObject>();
				for(Nfc_Record_ItemObject ItemObject : MyArrayObjects){
					//Filters both from Name and Bottom Text
					if((ItemObject.getName() +" "+ItemObject.getBottomText() +" "+ ItemObject.getBottomText_2()).toLowerCase().contains(filterString)){
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
			FilteredObjects = (ArrayList<Nfc_Record_ItemObject>) arg1.values;
			notifyDataSetChanged();
		}
		
	}
	
}
