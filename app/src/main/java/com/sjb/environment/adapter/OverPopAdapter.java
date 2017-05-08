package com.sjb.environment.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.sjb.environment.R;
import java.util.LinkedHashMap;
import java.util.List;

public class OverPopAdapter extends BaseAdapter {
	private Context context;
	ViewHolder viewHolder = null;
	List<?> monitor;
	private String name = "";
	private String isOver = "";
	private String isOnLine = "";

	public OverPopAdapter(Context context,List<?> monitor) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.monitor = monitor;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return monitor.size();
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
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.overlist_item, null);
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtName);

			viewHolder.over_item_image = (ImageView)convertView.findViewById(R.id.over_item_image);
			viewHolder.over_item_image_c = (ImageView)convertView.findViewById(R.id.detail_c_img);

			if(((LinkedHashMap)monitor.get(position)).get("name")!=null && !((LinkedHashMap)monitor.get(position)).get("name").equals("")) {
				name = ((LinkedHashMap) monitor.get(position)).get("name").toString();
			}
			if(((LinkedHashMap)monitor.get(position)).get("isover")!=null && !((LinkedHashMap)monitor.get(position)).get("isover").equals("")) {
				isOver = ((LinkedHashMap)monitor.get(position)).get("isover").toString();
			}
			if(((LinkedHashMap)monitor.get(position)).get("isonline")!=null && !((LinkedHashMap)monitor.get(position)).get("isonline").equals("")) {
				isOnLine = ((LinkedHashMap)monitor.get(position)).get("isonline").toString();
			}

			if(isOver.equals("0")){
				viewHolder.over_item_image_c.setVisibility(View.GONE);
				switch (isOnLine){
					case "1":
						Glide.with(context)
								.load(R.drawable.icon_on)
								.fitCenter()
								.into(viewHolder.over_item_image);
						break;
					case "0":
						Glide.with(context)
								.load(R.drawable.icon_off)
								.fitCenter()
								.into(viewHolder.over_item_image);
						break;
				}
			}else{
				viewHolder.over_item_image_c.setVisibility(View.VISIBLE);
			}

			viewHolder.txtName.setText(name);
			convertView.setTag(viewHolder);
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}

		return convertView;
	}

	private class ViewHolder {
		TextView txtName;
		ImageView over_item_image,over_item_image_c;
	}
}
