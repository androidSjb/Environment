package com.sjb.environment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.litesuits.http.data.Json;
import com.sjb.environment.R;
import com.sjb.environment.activity.ContralImfActivity;
import com.sjb.environment.activity.RealTimeDetailActivity;
import com.sjb.environment.adapter.RealTimeAdapter;
import com.sjb.environment.adapter.RealTimeFragmentAdapter;
import com.sjb.environment.application.CoreApplication;
import com.sjb.environment.base.BaseFragment;
import com.sjb.environment.bean.JKTypeBean;
import com.sjb.environment.bean.RealTimeBean;
import com.sjb.environment.singleton.AppSingleton;
import com.sjb.environment.util.Constants;
import com.sjb.environment.util.DensityUtil;
import com.sjb.environment.util.HttpUtil;
import com.sjb.environment.util.SharePreferenceUtil;
import com.sjb.environment.view.popwindow.OnePopView;
import com.sjb.environment.view.popwindow.PopListener;
import com.sjb.environment.view.popwindow.TopPop;
import com.sjb.environment.view.popwindow.TwoPopView;
import com.sjb.environment.view.scrollexpandable.ActionSlideExpandableListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/15.
 */

public class RealTimeFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    @ViewInject(R.id.expandableListView) private ExpandableListView listview;
    @ViewInject(R.id.real_city) private FrameLayout realCity;
    @ViewInject(R.id.real_look) private FrameLayout realLook;
    @ViewInject(R.id.real_type) private FrameLayout realType;

    @ViewInject(R.id.cityText) private TextView cityText;
    @ViewInject(R.id.gzText) private TextView gzText;
    @ViewInject(R.id.typeText) private TextView typeText;

    @ViewInject(R.id.noData) private View noData;

    private RealTimeFragmentAdapter mRealTimeAdapter;
    private ArrayList<String> typeCodes = new ArrayList<String>();
    private ArrayList<String> typeNames = new ArrayList<String>();

    private ArrayList<String> gzCodes = new ArrayList<String>();
    private ArrayList<String> gzNames = new ArrayList<String>();

    private ArrayList<String> cityNames = new ArrayList<String>();
    private ArrayList<String> departIds = new ArrayList<String>();
        private ArrayList<ArrayList<String>> cityChileNames = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> cityChileIds = new ArrayList<ArrayList<String>>();

    private ArrayList<RealTimeBean> realTimeBeans = new ArrayList<>();
    private TopPop pop,popType,popGz;
    private String url = "";



    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_realtime, container, false);
        x.view().inject(this, view);
        showSearch(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    monName = v.getText().toString();
                    loadData();
                    return true;
                }
                return false;
            }
        });
        url = SharePreferenceUtil.getPrefString(getActivity(), "url", "url", "");
        setBigList(listview);
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                try {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ContralImfActivity.class);
                    intent.putExtra("title", realTimeBeans.get(i).getName());
                    intent.putExtra("id", realTimeBeans.get(i).getId());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        realCity.setOnClickListener(this);
        realLook.setOnClickListener(this);
        realType.setOnClickListener(this);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                try {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        // 判断是否滚动到底部
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            //加载更多功能的代码
                            page++;
                            getRealTimeList(false,false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        getCity();
        getGZType();
        getJKType();
        getRealTimeList(true,true);
        return view;
    }

    @Override
    protected int getScrollView() {
        return 3;
    }

    @Override
    protected void loadData() {
        page = 1;
        realTimeBeans.clear();
        getRealTimeList(false,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.real_city:
                pop = new TopPop(getActivity(), getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2, new TwoPopView(getActivity(), cityNames,cityChileIds, cityChileNames, departIds, new PopListener() {
                    @Override
                    public void onClick(String str, int position) {
                        realTimeBeans.clear();
                        cityText.setText(str.split("@@")[0]);
                        pop.hide();
                        environ = str.split("@@")[1];
                        loadData();
                    }
                }));
                if(pop!=null) {
                    pop.showPop(0, v, 0);
                }
                break;
            case R.id.real_look:
                if(popGz!=null) {
                    popGz.showPop(0, v, 0);
                }
                break;
            case R.id.real_type:
                if(popType!=null) {
                    popType.showPop(0, v, 0);
                }

                break;
        }
    }

    private int page = 1;
    private String monName = "";
    private String monType = "";
    private String environ = "";
    private String attention = "";
    private int firstItem = 0;

    public void getRealTimeList(boolean needAnimal,final boolean needGone) {
        try {
            if (needAnimal){
                setAnimal();
            }
            firstItem = listview.getFirstVisiblePosition();

            HttpUtil httpUtil = new HttpUtil(getActivity());

            RequestParams params = new RequestParams(url+"/mobile/psBaseInfo.do?psBaseInfo");
            params.addParameter("pageIndex",page);
            if(!monName.equals("")) {
                params.addParameter("monName", monName);
            }
            if(!monType.equals("")&&!monType.equals("全部")) {
                params.addParameter("monType", monType);
            }
            if(!environ.equals("")&&!environ.equals("全部")) {
                params.addParameter("environ", environ);
            }
            if(!attention.equals("")&&!attention.equals("全部")) {
                params.addParameter("attention", attention);
            }
            httpUtil.HttpGet(params, new HttpUtil.MyHttpListener() {
                @Override
                public void callback(String obj) {

                    JsonParser parser = new JsonParser();

                    JsonElement el = parser.parse(obj);

                    JsonObject json = el.getAsJsonObject();
                    String code = json.get("code").getAsString();
                    Gson gson = new Gson();
                    String success = json.get("success").getAsString();

                    if (code.equals("202")) {
                        if (success.equals("true")) {

                            JsonArray jsonArray = json.get("obj").getAsJsonArray();
                            if(jsonArray.isJsonNull()||jsonArray.size()==0){
                                if(needGone) {
                                    noData.setVisibility(View.VISIBLE);
                                }
                            }else {
                                noData.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    RealTimeBean mRealTimeBean = gson.fromJson(jsonArray.get(i).toString(), RealTimeBean.class);
                                    realTimeBeans.add(mRealTimeBean);
                                }

                                mRealTimeAdapter = new RealTimeFragmentAdapter(CoreApplication.getInstance(), realTimeBeans,listview);
                                listview.setAdapter(mRealTimeAdapter);
                                if (firstItem >= realTimeBeans.size()) {
                                    firstItem = realTimeBeans.size() - 1;
                                }
                                listview.setSelection(firstItem);
                            }
                        }else{
                            if(needGone) {
                                noData.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        if(needGone) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    }

                }
                @Override
                public void onError(Throwable ex) {
                    Log.e("error",ex.toString());

                }

                @Override
                public void onCanceled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    stopAnimal();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getJKType(){
        try {
            HttpUtil httpUtil = new HttpUtil(getActivity());

            RequestParams params = new RequestParams(url+"/mobile/type.do?getTypeByCode");
            params.addParameter("typeParentcode","mon_type");
            httpUtil.HttpGet(params, new HttpUtil.MyHttpListener() {
                @Override
                public void callback(String obj) {
                    //{"attributes":null,"code":"202","obj":[{"typecode":"0","typename":"气"},{"typecode":"1","typename":"水"}],"success":true,"msg":"操作成功","jsonStr":"{\"obj\":[{\"typecode\":\"0\",\"typename\":\"气\"},{\"typecode\":\"1\",\"typename\":\"水\"}],\"msg\":\"操作成功\",\"success\":true}"}
                    JsonParser parser = new JsonParser();

                    JsonElement el = parser.parse(obj);

                    JsonObject json = el.getAsJsonObject();
                    String code = json.get("code").getAsString();
                    String success = json.get("success").getAsString();

                    if (code.equals("202")) {
                        if (success.equals("true")) {
                            JsonArray jsonArray = json.get("obj").getAsJsonArray();
                            typeNames.clear();
                            typeNames.add("全部");
                            typeCodes.clear();
                            typeCodes.add("");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                typeCodes.add(jsonArray.get(i).getAsJsonObject().get("typecode").getAsString());
                                typeNames.add(jsonArray.get(i).getAsJsonObject().get("typename").getAsString());
                            }

                            popType = new TopPop(getActivity(), getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2, new OnePopView(getActivity(), typeNames, 1, new PopListener() {
                                @Override
                                public void onClick(String str, int position) {
                                    realTimeBeans.clear();
                                    popType.hide();
                                    typeText.setText(str);
                                    monType = typeCodes.get(position);
                                    loadData();
                                }
                            }));
                        }
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    Log.e("error",ex.toString());
                }

                @Override
                public void onCanceled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    stopAnimal();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getGZType(){

        try {
            HttpUtil httpUtil = new HttpUtil(getActivity());

            RequestParams params = new RequestParams(url+"/mobile/type.do?getTypeByCode");
            params.addParameter("typeParentcode","attentionCode");
            httpUtil.HttpGet(params, new HttpUtil.MyHttpListener() {
                @Override
                public void callback(String obj) {
                    //{"attributes":null,"code":"202","obj":[{"typecode":"1","typename":"国控"},{"typecode":"2","typename":"省控"},{"typecode":"3","typename":"市控"},{"typecode":"4","typename":"非重点污染源"}],"success":true,"msg":"操作成功","jsonStr":"{\"obj\":[{\"typecode\":\"1\",\"typename\":\"国控\"},{\"typecode\":\"2\",\"typename\":\"省控\"},{\"typecode\":\"3\",\"typename\":\"市控\"},{\"typecode\":\"4\",\"typename\":\"非重点污染源\"}],\"msg\":\"操作成功\",\"success\":true}"}
                    JsonParser parser = new JsonParser();

                    JsonElement el = parser.parse(obj);

                    JsonObject json = el.getAsJsonObject();
                    String code = json.get("code").getAsString();
                    String success = json.get("success").getAsString();
                    gzCodes.clear();
                    gzNames.clear();
                    gzNames.add("全部");
                    gzCodes.add("");
                    if (code.equals("202")) {
                        if (success.equals("true")) {
                            JsonArray jsonArray = json.get("obj").getAsJsonArray();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                gzCodes.add(jsonArray.get(i).getAsJsonObject().get("typecode").getAsString());
                                gzNames.add(jsonArray.get(i).getAsJsonObject().get("typename").getAsString());
                            }

                            popGz = new TopPop(getActivity(), getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2, new OnePopView(getActivity(), gzNames, 1, new PopListener() {
                                @Override
                                public void onClick(String str, int position) {
                                    realTimeBeans.clear();
                                    popGz.hide();
                                    gzText.setText(str);
                                    attention = gzCodes.get(position);
                                    loadData();
                                }
                            }));
                        }
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    Log.e("error", ex.toString());
                }

                @Override
                public void onCanceled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    stopAnimal();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCity(){

        try {
            HttpUtil httpUtil = new HttpUtil(getActivity());

            RequestParams params = new RequestParams(url+"/mobile/type.do?getEnvironForUser");
            httpUtil.HttpGet(params, new HttpUtil.MyHttpListener() {
                @Override
                public void callback(String obj) {
                    //{"attributes":null,"code":"202","obj":[{"typecode":"1","typename":"国控"},{"typecode":"2","typename":"省控"},{"typecode":"3","typename":"市控"},{"typecode":"4","typename":"非重点污染源"}],"success":true,"msg":"操作成功","jsonStr":"{\"obj\":[{\"typecode\":\"1\",\"typename\":\"国控\"},{\"typecode\":\"2\",\"typename\":\"省控\"},{\"typecode\":\"3\",\"typename\":\"市控\"},{\"typecode\":\"4\",\"typename\":\"非重点污染源\"}],\"msg\":\"操作成功\",\"success\":true}"}
                    JsonParser parser = new JsonParser();

                    JsonElement el = parser.parse(obj);

                    JsonObject json = el.getAsJsonObject();
                    String code = json.get("code").getAsString();
                    String success = json.get("success").getAsString();

                    if (code.equals("202")) {
                        if (success.equals("true")) {
                            cityNames.add("全部");
                            departIds.add("全部");
                            cityChileNames.add(new ArrayList<String>());
                            cityChileIds.add(new ArrayList<String>());
                            JsonObject jsonArray = json.get("obj").getAsJsonObject();
                            cityNames.add(jsonArray.get("departname").getAsString());
                            String grade = jsonArray.get("grade").getAsString();
                            String departid = jsonArray.get("departid").getAsString();
                            departIds.add(departid);

                            if (grade.equals("1")) {
                                getChildCity(departid);
                            } else {
                                cityChileNames.add(new ArrayList<String>());
                                cityChileIds.add(new ArrayList<String>());
                            }
                        }
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    Log.e("error",ex.toString());
                }

                @Override
                public void onCanceled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    stopAnimal();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getChildCity(String pId){

        try {
            HttpUtil httpUtil = new HttpUtil(getActivity());

            RequestParams params = new RequestParams(url+"/mobile/type.do?getEnvironByPID");
            params.addParameter("pID",pId);
            httpUtil.HttpGet(params, new HttpUtil.MyHttpListener() {
                @Override
                public void callback(String obj) {
                    //{"attributes":null,"code":"202","obj":[{"typecode":"1","typename":"国控"},{"typecode":"2","typename":"省控"},{"typecode":"3","typename":"市控"},{"typecode":"4","typename":"非重点污染源"}],"success":true,"msg":"操作成功","jsonStr":"{\"obj\":[{\"typecode\":\"1\",\"typename\":\"国控\"},{\"typecode\":\"2\",\"typename\":\"省控\"},{\"typecode\":\"3\",\"typename\":\"市控\"},{\"typecode\":\"4\",\"typename\":\"非重点污染源\"}],\"msg\":\"操作成功\",\"success\":true}"}
                    JsonParser parser = new JsonParser();

                    JsonElement el = parser.parse(obj);

                    JsonObject json = el.getAsJsonObject();
                    String code = json.get("code").getAsString();
                    String success = json.get("success").getAsString();

                    if (code.equals("202")) {
                        if (success.equals("true")) {
                            JsonArray jsonArray = json.get("obj").getAsJsonArray();
                            ArrayList<String> childName = new ArrayList<String>();
                            ArrayList<String> childId = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                childName.add(jsonArray.get(i).getAsJsonObject().get("departname").getAsString());
                                childId.add(jsonArray.get(i).getAsJsonObject().get("id").getAsString());
    //                        String grade = jsonArray.get(i).getAsJsonObject().get("grade").getAsString();
    //                        String departid = jsonArray.get(i).getAsJsonObject().get("departid").getAsString();
                            }
                            cityChileNames.add(childName);
                            cityChileIds.add(childId);

                        }

                    }
                }

                @Override
                public void onError(Throwable ex) {
                    Log.e("error", ex.toString());
                }

                @Override
                public void onCanceled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    stopAnimal();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
