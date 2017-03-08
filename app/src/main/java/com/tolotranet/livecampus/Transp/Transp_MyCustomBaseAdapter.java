package com.tolotranet.livecampus.Transp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tolotranet.livecampus.R;

import java.util.ArrayList;

public class Transp_MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<Transp_ItemObject> MyArrayObjects = new ArrayList<Transp_ItemObject>();
	private static ArrayList<Transp_ItemObject> FilteredObjects = new ArrayList<Transp_ItemObject>();
	private Context context;
	private LayoutInflater mInflater;
	private ItemFilter myFilter = new ItemFilter();
	
	public Transp_MyCustomBaseAdapter(Context c, ArrayList<Transp_ItemObject> MyList) {
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
			convertView = mInflater.inflate(R.layout.transp_list_item, null);
			holder = new ViewHolder();
			holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
			holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
			holder.MiddleTV = (TextView) convertView.findViewById(R.id.Middle_name_tv);
			holder.RightTV = (TextView) convertView.findViewById(R.id.Right_name_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String Name = FilteredObjects.get(position).getName();
		String BottomText = FilteredObjects.get(position).getBottomText();
		String MiddleText = FilteredObjects.get(position).getDayText();
		String CohortText= FilteredObjects.get(position).getCohort();

		holder.NameTV.setText(Name);
		holder.RightTV.setText(BottomText);
		holder.BottomTV.setText(CohortText);
		holder.MiddleTV.setText(MiddleText);

		return convertView;
	}

	static class ViewHolder{
		TextView NameTV;
		TextView MiddleTV;
		TextView BottomTV;
		TextView RightTV;
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
				final ArrayList<Transp_ItemObject> TempList = new ArrayList<Transp_ItemObject>();
				for(Transp_ItemObject ItemObject : MyArrayObjects){
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
			FilteredObjects = (ArrayList<Transp_ItemObject>) arg1.values;
			notifyDataSetChanged();
		}
		
	}
	
}
