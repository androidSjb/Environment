package com.sjb.environment.singleton;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.sjb.environment.activity.LoginActivity;
import com.sjb.environment.activity.MainActivity;
import com.sjb.environment.util.SharePreferenceUtil;
import com.sjb.environment.view.CheckVersionDialog;

/**
 * Created by yj on 16/1/26.
 */
public class AppSingleton {

    private static AppSingleton instance = null;
    private boolean showLatestVersionTipsInSetting = false;//在设置页面显示版本提示
    private boolean notNetWorkInRefreshing = false;//自动刷新过程中网络中断了
    public Dialog downloadDialog = null;//dialog

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MainActivity mainActivity;


    public static AppSingleton getInstance()
    {
        if(instance == null)
        {
            instance = new AppSingleton();
        }
        return instance;
    }
    public boolean isShowLatestVersionTipsInSetting() {
        return showLatestVersionTipsInSetting;
    }
    public void setShowLatestVersionTipsInSetting(boolean showLatestVersionTipsInSetting) {
        this.showLatestVersionTipsInSetting = showLatestVersionTipsInSetting;
    }
    private AppSingleton()
    {
    }

    public boolean isNotNetWorkInRefreshing() {
        return notNetWorkInRefreshing;
    }

    public void setNotNetWorkInRefreshing(boolean notNetWorkInRefreshing) {
        this.notNetWorkInRefreshing = notNetWorkInRefreshing;
    }


    public void showDialog(String msg,final Context mContext,final boolean isQuit){
        if(downloadDialog == null){
           CheckVersionDialog.Builder builder = new CheckVersionDialog.Builder(mContext);
            builder.setMessage(msg);
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isQuit) {
                        SharePreferenceUtil.setPrefString(((Activity) mContext), "loginMode", "loginMode", "pass");
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                    dialog.dismiss();
                }
            });
            downloadDialog = builder.create();
            downloadDialog.show();
        }else {
            downloadDialog.dismiss();
            downloadDialog = null;
            CheckVersionDialog.Builder builder = new CheckVersionDialog.Builder(mContext);
            builder.setMessage(msg);
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isQuit) {
                        SharePreferenceUtil.setPrefString(((Activity) mContext), "loginMode", "loginMode", "pass");
                        Intent intent = new Intent(mContext,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                    dialog.dismiss();
                }
            });
            downloadDialog = builder.create();
            downloadDialog.show();
        }

    }

}
