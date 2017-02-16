package com.tolotranet.livecampus;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Sis_DetailListViewAdapterOwner extends BaseAdapter {

	private static ArrayList<Sis_DetailListItem> DetailList;
	private LayoutInflater mInflater;

	private class ViewHolder {
		TextView textView1;
		TextView textView2;
		int ref;
	}

	private String[] arrayTextTitle;
	private String[] arrayTextValue;
	private int[] arrayColumnValue;

	public Sis_DetailListViewAdapterOwner(Context context,
										  ArrayList<Sis_DetailListItem> results) {
		mInflater = LayoutInflater.from(context);
		DetailList = results;
	Log.d("helloDetailistowner", DetailList.toString());

		int n = DetailList.size();
	    arrayTextTitle = new String[n]; //because we need to initialize the array
		arrayTextValue = new String[n];
		arrayColumnValue = new int[n];

		for (int i = 0; i < n; i++) { //copy values of DetailistItem in two new ArrayString
			arrayTextTitle[i] = DetailList.get(i).getDetailName();
			arrayTextValue[i] = DetailList.get(i).getDetailValue();
			arrayColumnValue[i] = DetailList.get(i).getColumnValue();
		}
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(arrayTextTitle != null && arrayTextTitle.length != 0){
			return arrayTextTitle.length;
		}else {
			return DetailList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Sis_DetailListItem c = new Sis_DetailListItem() ; //because the value might change due to user edit, so we need to send the 
		c.setDetailName(arrayTextTitle[position]);
		c.setDetailValue(arrayTextValue[position]);
		c.setColumnValue(arrayColumnValue[position]);
		return c;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		//ViewHolder holder = null;
		final ViewHolder holder;
		if (convertView == null) {

			holder = new ViewHolder();
			LayoutInflater inflater = mInflater;
			convertView = inflater.inflate(R.layout.sis_detail_list_item_owner, null);
			holder.textView1 = (TextView) convertView.findViewById(R.id.detail_name);
			holder.textView2 = (TextView) convertView.findViewById(R.id.detail_value);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.ref = position;

		holder.textView1.setText(arrayTextTitle[position]);
		holder.textView2.setText(arrayTextValue[position]);
		holder.textView2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										  int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				arrayTextValue[holder.ref] = arg0.toString();
			}
		});

		return convertView;
	}



}

