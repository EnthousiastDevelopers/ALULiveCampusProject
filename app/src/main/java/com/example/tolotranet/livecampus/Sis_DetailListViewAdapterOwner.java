package com.example.tolotranet.livecampus;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class Sis_DetailListViewAdapterOwner extends BaseAdapter {


	private String[] arrText;
	private String[] arrTemp;

	private static ArrayList<Sis_DetailListItem> DetailList;

	private LayoutInflater mInflater;

	public Sis_DetailListViewAdapterOwner(Context context,
										  ArrayList<Sis_DetailListItem> results) {
		mInflater = LayoutInflater.from(context);
		DetailList = results;
	Log.d("hello", DetailList.toString());

		int n = DetailList.size();
	    arrText = new String[n];
		arrTemp = new String[n];

		for (int i = 0; i < n; i++) {
			arrText[i] = DetailList.get(i).getDetailName();
			arrTemp[i] = DetailList.get(i).getDetailValue();
		}
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(arrText != null && arrText.length != 0){
			return arrText.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrText[position];
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
			holder.editText1 = (EditText) convertView.findViewById(R.id.detail_value);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.ref = position;

		holder.textView1.setText(arrText[position]);
		holder.editText1.setText(arrTemp[position]);
		holder.editText1.addTextChangedListener(new TextWatcher() {

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
				arrTemp[holder.ref] = arg0.toString();
			}
		});

		return convertView;
	}

	private class ViewHolder {
		TextView textView1;
		EditText editText1;
		int ref;
	}


}

