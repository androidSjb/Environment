package com.sjb.environment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.internal.LinkedTreeMap;
import com.sjb.environment.R;
import com.sjb.environment.activity.RealTimeDetailActivity;
import com.sjb.environment.bean.RealTimeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueneng-cc on 16/5/7.
 */
public class WranAdapter extends BaseAdapter {
    private Context context;
    ArrayList<RealTimeBean> realTimeBeans;

    public WranAdapter(Context context, ArrayList<RealTimeBean> realTimeBeans) {
        this.context = context;
        this.realTimeBeans = realTimeBeans;
    }
    @Override
    public int getCount() {
        return realTimeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            ViewHolder viewHolder;

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_list_item, null);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.realtime_type = (ImageView) convertView.findViewById(R.id.realtime_type);


            viewHolder.expandableItemRoot = (LinearLayout)convertView.findViewById(R.id.expandable);

            viewHolder.expandableItemRoot.removeAllViews();
            String type = "";
            final List<?> monitor = realTimeBeans.get(position).getMonitor();
            for (int i = 0; i < monitor.size(); i++) {
                final View root = LayoutInflater.from(context).inflate(R.layout.expandable_item, null);
                TextView contentDetail = (TextView)root.findViewById(R.id.fan_number);
                ImageView detail_img = (ImageView)root.findViewById(R.id.detail_img);

                final String name = ((LinkedTreeMap)monitor.get(i)).get("name").toString();
                final String id = ((LinkedTreeMap)monitor.get(i)).get("id").toString();
                final String monitortype  = ((LinkedTreeMap)monitor.get(i)).get("monitortype").toString();
                if(monitortype.equals("0")){
                    //气
                    type += "0";
                }else{
                    //水
                    type += "1";
                }
                Glide.with(context)
                        .load(R.drawable.icon_outb)
                        .fitCenter()
                        .into(detail_img);
                contentDetail.setText(name);

                root.setTag(i);
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RealTimeDetailActivity.class);
                        intent.putExtra("title",name);
                        intent.putExtra("psInfoId",id);
                        intent.putExtra("monitortype",monitortype);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(intent);
                    }
                });
                viewHolder.expandableItemRoot.addView(root);
            }
//            viewHolder.realtime_type.setTag(type);
            viewHolder.name.setText(realTimeBeans.get(position).getName());

            if(type.contains("0")&&type.contains("1")){
                Glide.with(context)
                        .load(R.drawable.out_all)
                        .fitCenter()
                        .into(viewHolder.realtime_type);
            }else{
                if(type.contains("0")){
                    Glide.with(context)
                            .load(R.drawable.out_air)
                            .fitCenter()
                            .into(viewHolder.realtime_type);
                }else if(type.contains("1")){
                    Glide.with(context)
                            .load(R.drawable.out_woter)
                            .fitCenter()
                            .into(viewHolder.realtime_type);
                }
            }
            convertView.setTag(viewHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {

        ImageView realtime_type;
        TextView name;
        LinearLayout expandableItemRoot;

    }
}
