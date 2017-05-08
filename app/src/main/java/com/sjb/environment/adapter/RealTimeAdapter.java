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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.internal.LinkedTreeMap;
import com.sjb.environment.R;
import com.sjb.environment.activity.RealTimeDetailActivity;
import com.sjb.environment.bean.RealTimeBean;
import com.sjb.environment.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueneng-cc on 16/5/7.
 */
public class RealTimeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<RealTimeBean> realTimeBeans;
    private String name = "";
    private String id = "";
    private String isOver = "";
    private String isOnLine = "";
    private String monitortype = "";


    public RealTimeAdapter(Context context,ArrayList<RealTimeBean> realTimeBeans) {
        this.context = context;
        this.realTimeBeans = realTimeBeans;
    }
    @Override
    public int getCount() {
        return realTimeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            ViewHolder viewHolder;

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_list_item, null);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);

            viewHolder.realtime_type = (ImageView) convertView.findViewById(R.id.realtime_type);

            viewHolder.contralType = (ImageView)convertView.findViewById(R.id.contralType);

            viewHolder.expandableItemRoot = (LinearLayout)convertView.findViewById(R.id.expandable);

            viewHolder.expandableItemRoot.removeAllViews();

            final List<?> monitor = realTimeBeans.get(position).getMonitor();

            for (int i = 0; i < monitor.size(); i++) {

                final View root = LayoutInflater.from(context).inflate(R.layout.expandable_item, null);
                TextView contentDetail = (TextView)root.findViewById(R.id.fan_number);
                ImageView detail_img = (ImageView)root.findViewById(R.id.detail_img);


                if(((LinkedTreeMap)monitor.get(i)).get("name")!=null && !((LinkedTreeMap)monitor.get(i)).get("name").equals("")) {
                    name = ((LinkedTreeMap) monitor.get(i)).get("name").toString();
                }
                if(((LinkedTreeMap)monitor.get(i)).get("id")!=null && !((LinkedTreeMap)monitor.get(i)).get("id").equals("")) {
                    id = ((LinkedTreeMap) monitor.get(i)).get("id").toString();
                }
                if(((LinkedTreeMap)monitor.get(i)).get("isover")!=null && !((LinkedTreeMap)monitor.get(i)).get("isover").equals("")) {
                    isOver = ((LinkedTreeMap) monitor.get(i)).get("isover").toString();
                }
                if(((LinkedTreeMap)monitor.get(i)).get("isonline")!=null && !((LinkedTreeMap)monitor.get(i)).get("isonline").equals("")) {
                     isOnLine = ((LinkedTreeMap) monitor.get(i)).get("isonline").toString();
                }
                if(((LinkedTreeMap)monitor.get(i)).get("monitortype")!=null && !((LinkedTreeMap)monitor.get(i)).get("monitortype").equals("")) {
                     monitortype = ((LinkedTreeMap) monitor.get(i)).get("monitortype").toString();
                }
                if(isOver.equals("0")){
                    switch (isOnLine){
                        case "1":
                            Glide.with(context)
                                    .load(R.drawable.icon_online)
                                    .fitCenter()
                                    .into(detail_img);
                            break;
                        case "0":
                            Glide.with(context)
                                    .load(R.drawable.icon_outline)
                                    .fitCenter()
                                    .into(detail_img);
                            break;
                    }
                }else{
                    Glide.with(context)
                            .load(R.drawable.icon_outb)
                            .fitCenter()
                            .into(detail_img);
                }
                contentDetail.setText(name);

                root.setTag(i);
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RealTimeDetailActivity.class);
                        intent.putExtra("title", name);
                        intent.putExtra("psInfoId", id);
                        intent.putExtra("monitortype", monitortype);
                        intent.putExtra("bean",realTimeBeans.get(position));
                        if (realTimeBeans.get(position).getId() != null && !realTimeBeans.get(position).getId().equals("")) {
                            intent.putExtra("monId", realTimeBeans.get(position).getId());
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(intent);
                    }
                });
                viewHolder.expandableItemRoot.addView(root);
            }
            String iconType = realTimeBeans.get(position).getShowtype();
            String[] icons = iconType.split("_");
            String isOn = icons[0];
            String bigTag = icons[1];
            String smallTag = icons[2];

            switch (isOn){
                case "1":
                    switch (bigTag){
                        case "0":
                            Glide.with(context)
                                    .load(R.drawable.out_air)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "1":
                            Glide.with(context)
                                    .load(R.drawable.out_woter)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "2":
                            Glide.with(context)
                                    .load(R.drawable.out_all)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "00":
                            Glide.with(context)
                                    .load(R.drawable.out_rubbish)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "10":
                            Glide.with(context)
                                    .load(R.drawable.out_ws)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "9":
                            Glide.with(context)
                                    .load(R.drawable.normal)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                    }
                    break;
                case "2":
                    switch (bigTag){
                        case "0":
                            Glide.with(context)
                                    .load(R.drawable.out_air_grey)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "1":
                            Glide.with(context)
                                    .load(R.drawable.out_woter_grey)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "2":
                            Glide.with(context)
                                    .load(R.drawable.out_all_grey)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "00":
                            Glide.with(context)
                                    .load(R.drawable.out_rubbish_grey)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "10":
                            Glide.with(context)
                                    .load(R.drawable.out_ws_grey)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "9":
                            Glide.with(context)
                                    .load(R.drawable.normal)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                    }
                    break;
                case "99":
                    switch (bigTag){
                        case "0":
                            Glide.with(context)
                                    .load(R.drawable.out_air_green)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "1":
                            Glide.with(context)
                                    .load(R.drawable.out_woter_green)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "2":
                            Glide.with(context)
                                    .load(R.drawable.out_all_green)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "00":
                            Glide.with(context)
                                    .load(R.drawable.out_rubbish_green)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "10":
                            Glide.with(context)
                                    .load(R.drawable.out_ws_green)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                        case "9":
                            Glide.with(context)
                                    .load(R.drawable.normal)
                                    .fitCenter()
                                    .into(viewHolder.realtime_type);
                            break;
                    }
                    break;
            }


            switch (smallTag){
                case "1":
                    Glide.with(context)
                            .load(R.drawable.out_gk)
                            .fitCenter()
                            .into(viewHolder.contralType);
                    break;
                case "2":
                    Glide.with(context)
                            .load(R.drawable.out_sk)
                            .fitCenter()
                            .into(viewHolder.contralType);
                    break;
                case "3":
                    Glide.with(context)
                            .load(R.drawable.out_shik)
                            .fitCenter()
                            .into(viewHolder.contralType);
                    break;
                case "4":
                    Glide.with(context)
                            .load(R.drawable.out_fz)
                            .fitCenter()
                            .into(viewHolder.contralType);
                    break;
            }

            viewHolder.name.setText(realTimeBeans.get(position).getName());

            convertView.setTag(viewHolder);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {

        ImageView realtime_type,contralType;
        TextView name;
        LinearLayout expandableItemRoot;

    }
}
