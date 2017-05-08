package com.sjb.environment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sjb.environment.R;
import com.sjb.environment.bean.MessageBean;
import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
	private ArrayList<MessageBean> jsonArray;
	private Context context;
	ViewHolder viewHolder = null;
	public MessageAdapter(Context context,ArrayList<MessageBean> jsonArray) {
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

                convertView = LayoutInflater.from(context).inflate(R.layout.message_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView)convertView.findViewById(R.id.title);
                viewHolder.content = (TextView)convertView.findViewById(R.id.content);
                viewHolder.time = (TextView)convertView.findViewById(R.id.time);
                viewHolder.image = (ImageView)convertView.findViewById(R.id.image_url);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


			MessageBean mMessageBean = jsonArray.get(position);

			String title = mMessageBean.getTitle();
			String content = mMessageBean.getContent();
			String time = mMessageBean.getTime();
			String type = mMessageBean.getType();

			viewHolder.title.setText(title);
			viewHolder.content.setText(content);
			viewHolder.time.setText(time);
			switch (type){
                case "1":
                    Glide.with(context).load(R.drawable.icon_tz).into(viewHolder.image);
                    break;
                case "2":
                    Glide.with(context).load(R.drawable.icon_tx).into(viewHolder.image);
                    break;
                case "3":
                    Glide.with(context).load(R.drawable.icon_wx).into(viewHolder.image);
                    break;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}


	private class ViewHolder {
		TextView title,content,time;
		ImageView image;
	}
}
