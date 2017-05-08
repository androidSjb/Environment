package com.sjb.environment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.sjb.environment.R;
import com.sjb.environment.bean.MessageBean;
import com.sjb.environment.bean.OutBean;

import java.util.ArrayList;

public class OutAdapter extends BaseAdapter {
	private ArrayList<OutBean> jsonArray;
	private Context context;
	ViewHolder viewHolder = null;
	public OutAdapter(Context context,ArrayList<JsonObject> values, ArrayList<String> allKeys) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.jsonArray = jsonArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonArray.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.out_b_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.time = (TextView)convertView.findViewById(R.id.time);
                viewHolder.value = (TextView)convertView.findViewById(R.id.value);
                viewHolder.bs = (TextView)convertView.findViewById(R.id.bs);
                viewHolder.bz = (TextView)convertView.findViewById(R.id.bz);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


			OutBean mOutBean = jsonArray.get(position);


			viewHolder.time.setText(mOutBean.getTime());
			viewHolder.value.setText(mOutBean.getValue());
			viewHolder.bs.setText(mOutBean.getBs());
			viewHolder.bz.setText(mOutBean.getBz());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}


	private class ViewHolder {
		TextView time,value,bs,bz;
	}
}
