package com.sjb.environment.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjb.environment.R;
import java.util.ArrayList;

public class DataAdapter extends BaseAdapter {
	private Context context;
	ViewHolder viewHolder = null;
	ArrayList<String> allKeys = new ArrayList<>();
	ArrayList<JsonObject> values = new ArrayList<>();

	public DataAdapter(Context context,ArrayList<JsonObject> values, ArrayList<String> allKeys) {
		// TODO Auto-generated constructor stub
		this.allKeys = allKeys;
		this.context = context;
		this.values = values;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return values.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return values.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.data_list_item, null);

			viewHolder.data_list_root = (LinearLayout)convertView.findViewById(R.id.data_list_root);
			viewHolder.data_list_root.removeAllViews();

			for (int i = 0; i < allKeys.size(); i++) {
                View child = LayoutInflater.from(context).inflate(R.layout.data_list_item_child, null);
                viewHolder.time = (TextView)child.findViewById(R.id.time);
                viewHolder.time.setText(values.get(position).get(allKeys.get(i)).getAsString());
                viewHolder.data_list_root.addView(child);
            }

			viewHolder.data_list_root.setBackgroundColor(context.getResources().getColor(R.color.login_bg));
			convertView.setTag(viewHolder);
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}

		return convertView;
	}


	private class ViewHolder {
		LinearLayout data_list_root;
		TextView time;
	}
}
