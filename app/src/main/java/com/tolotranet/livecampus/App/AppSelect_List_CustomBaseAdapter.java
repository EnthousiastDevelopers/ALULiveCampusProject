package com.tolotranet.livecampus.App;


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

public class AppSelect_List_CustomBaseAdapter extends BaseAdapter {
	private  ArrayList<Apps_ItemObject> MyArrayObjects = new ArrayList<Apps_ItemObject>();
	private  ArrayList<Apps_ItemObject> FilteredObjects = new ArrayList<Apps_ItemObject>();
	private Context context;
	private LayoutInflater mInflater;
	private ItemFilter myFilter = new ItemFilter();
	private String type= null;

	public AppSelect_List_CustomBaseAdapter(Context c, ArrayList<Apps_ItemObject> MyList, String type) {
		this.context = c;
		this.type = type;
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
			convertView = mInflater.inflate(R.layout.appselect_list_item_single_upper, null);
			if(type.equals("child")){
				convertView = mInflater.inflate(R.layout.appselect_list_item_single, null);
			}
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
	//	holder.BreakFastTV.setText(BottomText);
		holder.ImgV.setImageResource(ImgId);



		//setting theme
		holder.NameTV.setTextColor(context.getResources().getColor(R.color.blue_alu));
		holder.BottomTV.setTextColor(context.getResources().getColor(R.color.blue_alu));
		holder.ImgV.setColorFilter(context.getResources().getColor(R.color.red_alu));

		return convertView;
	}

	 class ViewHolder{
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
				final ArrayList<Apps_ItemObject> TempList = new ArrayList<Apps_ItemObject>();
				for(Apps_ItemObject ItemObject : MyArrayObjects){
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
			FilteredObjects = (ArrayList<Apps_ItemObject>) arg1.values;
			notifyDataSetChanged();
		}
		
	}
	
}
