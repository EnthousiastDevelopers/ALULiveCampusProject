package com.tolotranet.livecampus.Sis.Interact;


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

public class Interact_List_CustomBaseAdapter extends BaseAdapter {
	private  ArrayList<Interaction_ItemObject> MyArrayObjects = new ArrayList<Interaction_ItemObject>();
	private  ArrayList<Interaction_ItemObject> FilteredObjects = new ArrayList<Interaction_ItemObject>();
	private Context context;
	private LayoutInflater mInflater;
	private ItemFilter myFilter = new ItemFilter();
	private String type= null;
	private Boolean[] arrayTextValue;


	public Interact_List_CustomBaseAdapter(Context c, ArrayList<Interaction_ItemObject> MyList, String type) {
		this.context = c;
		this.type = type;
		MyArrayObjects = MyList;
		FilteredObjects = MyList;
		mInflater = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
		int n = MyList.size();
		arrayTextValue = new Boolean[n];

		for (int i = 0; i < n; i++) { //copy values of DetailistItem in two new ArrayString
			arrayTextValue[i] = false;
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

	public Filter getFilter(){
		return myFilter;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		
		if(convertView == null){

			convertView = mInflater.inflate(R.layout.interact_list_item, null);
			holder = new ViewHolder();
			holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
			holder.BottomTV = (TextView) convertView.findViewById(R.id.Middle_name_tv);
			holder.ImgV = (ImageView) convertView.findViewById(R.id.PersonImageView);


			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		
		String Name = FilteredObjects.get(position).getName();
		String BottomText = FilteredObjects.get(position).getCategory();
		int ImgId = FilteredObjects.get(position).getImgId();


		holder.NameTV.setText(Name);
	//	holder.BreakFastTV.setText(BottomText);
		holder.ImgV.setImageResource(ImgId);



		//setting theme
		holder.NameTV.setTextColor(context.getResources().getColor(R.color.black));
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
				final ArrayList<Interaction_ItemObject> TempList = new ArrayList<Interaction_ItemObject>();
				for(Interaction_ItemObject ItemObject : MyArrayObjects){
					//Filters both from Name and Bottom Text
					if((ItemObject.getName() +" "+ItemObject.getCategory()).toLowerCase().contains(filterString)){
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
			FilteredObjects = (ArrayList<Interaction_ItemObject>) arg1.values;
			notifyDataSetChanged();
		}
		
	}
	
}
