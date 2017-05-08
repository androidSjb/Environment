package com.sjb.environment.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sjb.environment.R;
import com.sjb.environment.base.BaseActivity;
import com.sjb.environment.singleton.AppSingleton;
import com.sjb.environment.util.HttpUtil;
import com.sjb.environment.util.SharePreferenceUtil;
import com.sjb.environment.util.ToastUtil;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by yueneng-cc on 16/4/5.608561013548
 */
public class AnswerActivity extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.reportEdit) private EditText reportEdit;
    @ViewInject(R.id.feedBack) private TextView feedBack;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setLayoutContenner(R.layout.answer);
        x.view().inject(this);
        setMidTitle("意见反馈");
        feedBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feedBack:
                feedBack();
                break;
        }
    }

    public void feedBack(){

        try {
            String text = reportEdit.getText().toString();
            if(text.length()>200){
                ToastUtil.showToast(AnswerActivity.this,"反馈信息不能大于200字", Toast.LENGTH_SHORT);
            }else if(text.equals("")){
                ToastUtil.showToast(AnswerActivity.this,"反馈信息不能为空", Toast.LENGTH_SHORT);
            }else {
                setAnimal();
                HttpUtil httpUtil = new HttpUtil(AnswerActivity.this);
                String url = SharePreferenceUtil.getPrefString(AnswerActivity.this, "url", "url", "");
                RequestParams params = new RequestParams(url + "/mobile/reportMobile.do?addReport");
                params.setConnectTimeout(15000);
                params.addQueryStringParameter("reportContext", text);
                httpUtil.HttpPost(params, new HttpUtil.MyHttpListener() {
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
                                    reportEdit.setText("");
                                    AppSingleton.getInstance().showDialog("提交成功", AnswerActivity.this,false);
                                } else {
                                    String msg = json.get("msg").getAsString();
                                    AppSingleton.getInstance().showDialog(msg, AnswerActivity.this,false);
                                }
                            } else {
                                String msg = json.get("msg").getAsString();
                                AppSingleton.getInstance().showDialog(msg, AnswerActivity.this,false);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
