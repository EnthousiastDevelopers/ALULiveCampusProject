package com.tolotranet.livecampus.Maint;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.tolotranet.livecampus.R;

import java.util.ArrayList;

public class Maint_DetailListViewAdapterOwner extends BaseAdapter {

	private static ArrayList<Maint_DetailListItem> DetailList;
	private LayoutInflater mInflater;

	public Maint_DetailListViewAdapterOwner(Context context,
											ArrayList<Maint_DetailListItem> results) {
		mInflater = LayoutInflater.from(context);
		DetailList = results;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return DetailList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return DetailList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.maint_detail_list_item_owner, null);
			holder = new ViewHolder();
			holder.DetailName = (TextView) convertView
					.findViewById(R.id.detail_name);
			holder.DetailValue = (EditText) convertView
					.findViewById(R.id.detail_value);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.DetailName.setText(DetailList.get(position).getDetailName());
		holder.DetailValue
				.setText(DetailList.get(position).getDetailValue());

		return convertView; 
	}


	static class ViewHolder {
		TextView DetailName;
		EditText DetailValue;
	}

}
