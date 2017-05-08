package com.sjb.environment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sjb.environment.R;
import com.sjb.environment.activity.CheckUpdateActivity;
import com.sjb.environment.activity.NoticeActivity;
import com.sjb.environment.activity.WranActivity;
import com.sjb.environment.adapter.MessageAdapter;
import com.sjb.environment.base.BaseFragment;
import com.sjb.environment.bean.MessageBean;
import com.sjb.environment.util.HttpUtil;
import com.sjb.environment.util.SharePreferenceUtil;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/15.
 */

public class MessageFragment extends BaseFragment{

    View view;

    @ViewInject(R.id.list) private ListView list;

    private MessageAdapter mMessageAdapter;

    private ArrayList<MessageBean> mMessageBeans = new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_message, container, false);

        x.view().inject(this, view);

        setTitle("消息中心");

        hideArrow();

        setListView(list);

        mMessageAdapter = new MessageAdapter(getActivity(),mMessageBeans);

        list.setAdapter(mMessageAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(mMessageBeans.get(position).getIsCurrent().equals(0)){
                        setRead(mMessageBeans.get(position).getMessageId());
                    }

                    String type = mMessageBeans.get(position).getType();
                    Intent intent = new Intent();
                    switch (type) {
                        case "1":
                            intent.setClass(getActivity(), NoticeActivity.class);
                            intent.putExtra("id",mMessageBeans.get(position).getNoticeId());
                            break;
                        case "2":
                            intent.setClass(getActivity(), WranActivity.class);
                            intent.putExtra("json", mMessageBeans.get(position).getDataParam());
                            break;
                        case "3":
                            intent.setClass(getActivity(), CheckUpdateActivity.class);
                            break;
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                try {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        // 判断是否滚动到底部
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            //加载更多功能的代码
                            page++;
                            getMessageList();
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
        getMessageList();
        return view;
    }
    int page = 1;
    @Override
    protected int getScrollView() {
        return 1;
    }

    @Override
    protected void loadData() {
        page = 1;
        mMessageBeans.clear();
        getMessageList();
    }

    public void updateList(String message){
        try {
            JsonArray jsonArray = new JsonParser().parse(message).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String type = jsonObject.get("noticetype").getAsString();
                String content = jsonObject.get("noticetitle").getAsString();
                String time = jsonObject.get("sendtime").getAsString();
                String messageId = jsonObject.get("messageid").getAsString();
                String isCurrent = jsonObject.get("iscurrent").getAsString();
                JsonElement jso = jsonObject.get("noticeid");
                String id = "";
                if(jso.isJsonNull()){
                    id = "";
                }else {
                    id = jso.getAsString();
                }

                JsonElement jsonE = jsonObject.get("dataparam");
                String dataParam = "";
                if(jsonE.isJsonNull()){
                    dataParam = "";
                }else{
                    JsonParser parser = new JsonParser();

                    JsonElement el = parser.parse(jsonE.getAsString());
                    JsonArray jsonA = el.getAsJsonArray();
                    JsonObject jsonO = jsonA.get(0).getAsJsonObject();
                    if(!jsonO.get("dataParam").getAsJsonObject().get("overParam").isJsonNull()) {
                        dataParam = jsonO.get("dataParam").getAsJsonObject().get("overParam").getAsJsonArray().toString();
                    }
                }
                String title = "";

                switch (type){
                    case "1":
                        title = "通知";
                        break;
                    case "2":
                        title = "提醒";
                        break;
                    case "3":
                        title = "温馨提示";
                        break;
                }
                MessageBean mMessageBean = new MessageBean();
                mMessageBean.setContent(content);
                mMessageBean.setTime(time);
                mMessageBean.setTitle(title);
                mMessageBean.setType(type);
                mMessageBean.setNoticeId(id);
                mMessageBean.setMessageId(messageId);
                mMessageBean.setDataParam(dataParam);
                mMessageBean.setIsCurrent(isCurrent);
                mMessageBeans.add(mMessageBean);

                handler.sendEmptyMessage(0);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMessageAdapter.notifyDataSetChanged();
            if(firstItem>=mMessageBeans.size()){
                firstItem = mMessageBeans.size() - 1;
            }
            list.setSelection(firstItem);
        }
    };

    int firstItem = 0;
    public void getMessageList(){
        firstItem = list.getFirstVisiblePosition();
        HttpUtil httpUtil = new HttpUtil(getActivity());
        String url = SharePreferenceUtil.getPrefString(getActivity(), "url", "url", "");
        RequestParams params = new RequestParams(url+"/mobile/notice.do?getAllNoticeByUser");
        params.addParameter("pageIndex", page);
        httpUtil.HttpGet(params, new HttpUtil.MyHttpListener() {
            @Override
            public void callback(String obj) {
                try {
                    JsonParser parser = new JsonParser();

                    JsonElement el = parser.parse(obj);

                    JsonObject json = el.getAsJsonObject();
                    String code = json.get("code").getAsString();
                    String success = json.get("success").getAsString();

                    if (code.equals("202")) {
                        if (success.equals("true")) {
                            updateList(json.get("obj").getAsJsonArray().toString());
                        } else {

                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
    }

    public void setRead(String messageId){
        HttpUtil httpUtil = new HttpUtil(getActivity());
        String url = SharePreferenceUtil.getPrefString(getActivity(), "url", "url", "");
        RequestParams params = new RequestParams(url+"/mobile/notice.do?setNoticeCurrent");
        params.addParameter("noticeID", messageId.replace(" ",""));
        httpUtil.HttpPost(params, new HttpUtil.MyHttpListener() {
            @Override
            public void callback(String obj) {
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
    }

}
