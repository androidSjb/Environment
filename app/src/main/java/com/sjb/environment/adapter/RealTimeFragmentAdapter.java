package com.sjb.environment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.internal.LinkedTreeMap;
import com.sjb.environment.R;
import com.sjb.environment.activity.RealTimeDetailActivity;
import com.sjb.environment.bean.RealTimeBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2017/5/7.
 */
public class RealTimeFragmentAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<RealTimeBean> realTimeBeans;
    private LayoutInflater inflater;
    private String name = "";
    private String isOver = "";
    private String isOnLine = "";
    private String id = "";
    private String monitortype = "";
    ExpandableListView listview;

    public RealTimeFragmentAdapter(Context mContext,ArrayList<RealTimeBean> realTimeBeans,ExpandableListView listview){
        this.context = mContext;
        this.realTimeBeans = realTimeBeans;
        this.listview = listview;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getGroupCount() {
        return realTimeBeans.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return realTimeBeans.get(i).getMonitor().size();
    }

    @Override
    public Object getGroup(int i) {
        return i;
    }

    @Override
    public Object getChild(int i, int i1) {
        return ((LinkedHashMap) realTimeBeans.get(i).getMonitor().get(i1)).get("name");
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.expandable_list_item, null);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        ImageView realtime_type = (ImageView) convertView.findViewById(R.id.realtime_type);
        LinearLayout expandable_toggle_button = (LinearLayout) convertView.findViewById(R.id.expandable_toggle_button);
        expandable_toggle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listview.isGroupExpanded(i)){
                    listview.collapseGroup(i);
                }else {
                    listview.expandGroup(i);
                }
            }
        });
        ImageView contralType = (ImageView)convertView.findViewById(R.id.contralType);

        if( realTimeBeans.get(i).getMonitor().size()>0){
            expandable_toggle_button.setVisibility(View.VISIBLE);
        }else{
            expandable_toggle_button.setVisibility(View.GONE);
        }
        String iconType = realTimeBeans.get(i).getShowtype();
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
                                .into(realtime_type);
                        break;
                    case "1":
                        Glide.with(context)
                                .load(R.drawable.out_woter)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "2":
                        Glide.with(context)
                                .load(R.drawable.out_all)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "00":
                        Glide.with(context)
                                .load(R.drawable.out_rubbish)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "10":
                        Glide.with(context)
                                .load(R.drawable.out_ws)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "9":
                        Glide.with(context)
                                .load(R.drawable.normal)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                }
                break;
            case "2":
                switch (bigTag){
                    case "0":
                        Glide.with(context)
                                .load(R.drawable.out_air_grey)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "1":
                        Glide.with(context)
                                .load(R.drawable.out_woter_grey)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "2":
                        Glide.with(context)
                                .load(R.drawable.out_all_grey)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "00":
                        Glide.with(context)
                                .load(R.drawable.out_rubbish_grey)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "10":
                        Glide.with(context)
                                .load(R.drawable.out_ws_grey)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "9":
                        Glide.with(context)
                                .load(R.drawable.normal)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                }
                break;
            case "99":
                switch (bigTag){
                    case "0":
                        Glide.with(context)
                                .load(R.drawable.out_air_green)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "1":
                        Glide.with(context)
                                .load(R.drawable.out_woter_green)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "2":
                        Glide.with(context)
                                .load(R.drawable.out_all_green)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "00":
                        Glide.with(context)
                                .load(R.drawable.out_rubbish_green)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "10":
                        Glide.with(context)
                                .load(R.drawable.out_ws_green)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                    case "9":
                        Glide.with(context)
                                .load(R.drawable.normal)
                                .fitCenter()
                                .into(realtime_type);
                        break;
                }
                break;
        }


        switch (smallTag){
            case "1":
                Glide.with(context)
                        .load(R.drawable.out_gk)
                        .fitCenter()
                        .into(contralType);
                break;
            case "2":
                Glide.with(context)
                        .load(R.drawable.out_sk)
                        .fitCenter()
                        .into(contralType);
                break;
            case "3":
                Glide.with(context)
                        .load(R.drawable.out_shik)
                        .fitCenter()
                        .into(contralType);
                break;
            case "4":
                Glide.with(context)
                        .load(R.drawable.out_fz)
                        .fitCenter()
                        .into(contralType);
                break;
        }

        name.setText(realTimeBeans.get(i).getName());

        return convertView;
    }

    @Override
    public View getChildView(final int i,final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.expandable_item, null);
        LinearLayout expand_list_root = (LinearLayout)convertView.findViewById(R.id.expand_list_root);
        expand_list_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RealTimeDetailActivity.class);
                if(((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("name")!=null &&
                        !((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("name").equals("")) {
                    name = ((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("name").toString();
                }
                if(((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("id")!=null &&
                        !((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("id").equals("")) {
                    id = ((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("id").toString();
                }

                if(((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("monitortype")!=null &&
                        !((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("monitortype").equals("")) {
                    monitortype = ((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("monitortype").toString();
                }
                intent.putExtra("title", name);
                intent.putExtra("psInfoId", id);
                intent.putExtra("monitortype", monitortype);
                intent.putExtra("bean",realTimeBeans.get(i));
                if (realTimeBeans.get(i).getId() != null && !realTimeBeans.get(i).getId().equals("")) {
                    intent.putExtra("monId", realTimeBeans.get(i).getId());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }
        });
        TextView contentDetail = (TextView)convertView.findViewById(R.id.fan_number);
        ImageView detail_img = (ImageView)convertView.findViewById(R.id.detail_img);
        ImageView detail_c_img = (ImageView)convertView.findViewById(R.id.detail_c_img);
        if(((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("name")!=null &&
                !((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("name").equals("")) {
            name = ((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("name").toString();
        }

        if(((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("isover")!=null &&
                !((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("isover").equals("")) {
            isOver = ((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("isover").toString();
        }
        if(((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("isonline")!=null &&
                !((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("isonline").equals("")) {
            isOnLine = ((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("isonline").toString();
        }



        if(isOver.equals("0")){
            detail_c_img.setVisibility(View.GONE);
            switch (isOnLine){
                case "1":
                    Glide.with(context)
                            .load(R.drawable.icon_on)
                            .fitCenter()
                            .into(detail_img);
                    break;
                case "0":
                    Glide.with(context)
                            .load(R.drawable.icon_off)
                            .fitCenter()
                            .into(detail_img);
                    break;
            }
        }else{
            detail_c_img.setVisibility(View.VISIBLE);
//            Glide.with(context)
//                    .load(R.drawable.icon_outb)
//                    .fitCenter()
//                    .into(detail_img);
        }
        contentDetail.setText(name);

//        convertView.setTag(i);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, RealTimeDetailActivity.class);
//                intent.putExtra("title", name);
//                intent.putExtra("psInfoId", id);
//                intent.putExtra("monitortype", monitortype);
//                intent.putExtra("bean",realTimeBeans.get(position));
//                if (realTimeBeans.get(position).getId() != null && !realTimeBeans.get(position).getId().equals("")) {
//                    intent.putExtra("monId", realTimeBeans.get(position).getId());
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                context.startActivity(intent);
//
//        tv_child.setText(((LinkedTreeMap) realTimeBeans.get(i).getMonitor().get(i1)).get("name").toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
