package com.sjb.environment.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.sjb.environment.R;

import java.util.ArrayList;

public class RealTimeDetailListAdapter extends BaseAdapter {
	private Context context;
	ViewHolder viewHolder = null;
	ArrayList<String> keys = new ArrayList<>();
	ArrayList<String> values = new ArrayList<>();

	public RealTimeDetailListAdapter(Context context, ArrayList<String> values, ArrayList<String> keys) {
		// TODO Auto-generated constructor stub
		this.keys = keys;
		this.context = context;
		this.values = values;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return keys.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return keys.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		try {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.real_time_detail_item, null);

			viewHolder.keys = (TextView)convertView.findViewById(R.id.key);
			viewHolder.values = (TextView)convertView.findViewById(R.id.value);

			viewHolder.keys.setText(keys.get(position));
			viewHolder.values.setText(values.get(position));

			convertView.setTag(viewHolder);
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}

		return convertView;
	}


	private class ViewHolder {
		TextView keys,values;
	}
}
