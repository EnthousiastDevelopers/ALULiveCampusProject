package com.tolotranet.livecampus.Food;


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

public class Food_MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<Food_ItemObject> MyArrayObjects = new ArrayList<Food_ItemObject>();
	private static ArrayList<Food_ItemObject> FilteredObjects = new ArrayList<Food_ItemObject>();
	private Context context;
	private LayoutInflater mInflater;
	private ItemFilter myFilter = new ItemFilter();

	
	public Food_MyCustomBaseAdapter(Context c, ArrayList<Food_ItemObject> MyList) {
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
			convertView = mInflater.inflate(R.layout.food_list_item, null);
			holder = new ViewHolder();
			holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
			holder.BreakFastTV = (TextView) convertView.findViewById(R.id.BreakfastTV);
			holder.SnackTV = (TextView) convertView.findViewById(R.id.SnackTV);
			holder.LunchTV = (TextView) convertView.findViewById(R.id.LunchTV);
			holder.SnackEveningTV = (TextView) convertView.findViewById(R.id.EveningSnackTV);
			holder.Dinner = (TextView) convertView.findViewById(R.id.DinnerTV);

			holder.VotesTV = (TextView) convertView.findViewById(R.id.Votes_TV);
			holder.CommmentTV = (TextView) convertView.findViewById(R.id.Comments_tv);

			holder.ImageViewIco = (ImageView) convertView.findViewById(R.id.PersonImageView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String Name = FilteredObjects.get(position).getName();
		String BottomText1 = FilteredObjects.get(position).getBottomText1();
		String BottomText2 = FilteredObjects.get(position).getBottomText2();
		String BottomText3 = FilteredObjects.get(position).getBottomText3();
		String BottomText4 = FilteredObjects.get(position).getBottomText4();
		String BottomText5 = FilteredObjects.get(position).getBottomText5();




		int RightText = FilteredObjects.get(position).getVotes();
		int RightCommentCount= FilteredObjects.get(position).getComments();
		int ImgViewID = FilteredObjects.get(position).getImgID();

		holder.NameTV.setText(Name);
		holder.BreakFastTV.setText(BottomText1);
		holder.SnackTV.setText(BottomText2);
		holder.LunchTV.setText(BottomText3);
		holder.SnackEveningTV.setText(BottomText4);
		holder.Dinner.setText(BottomText5);


		holder.VotesTV.setText(String.valueOf(RightText));
		holder.CommmentTV.setText(String.valueOf(RightCommentCount));
		//holder.ImageViewIco.setImageResource(ImgViewID);

		return convertView;
	}

	static class ViewHolder{
		TextView NameTV;
		TextView BreakFastTV;
		TextView SnackTV;
		TextView LunchTV;
		TextView SnackEveningTV;
		TextView Dinner;

		TextView VotesTV;
		TextView CommmentTV;
		ImageView ImageViewIco;
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
				final ArrayList<Food_ItemObject> TempList = new ArrayList<Food_ItemObject>();
				for(Food_ItemObject Sis_ItemObject : MyArrayObjects){
					//Filters both from Name and Bottom Text
					if((Sis_ItemObject.getName() +" "+Sis_ItemObject.getRightText() +" "+ Sis_ItemObject.getBottomText1()).toLowerCase().contains(filterString)){
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
			FilteredObjects = (ArrayList<Food_ItemObject>) arg1.values;
			notifyDataSetChanged();
		}
		
	}
	
}
