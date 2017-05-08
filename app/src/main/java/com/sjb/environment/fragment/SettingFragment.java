package com.sjb.environment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sjb.environment.R;
import com.sjb.environment.activity.AnswerActivity;
import com.sjb.environment.activity.BindAccountActivity;
import com.sjb.environment.activity.ChangePasActivity;
import com.sjb.environment.activity.CheckUpdateActivity;
import com.sjb.environment.activity.LoginActivity;
import com.sjb.environment.activity.PushSettingActivity;
import com.sjb.environment.adapter.RealTimeAdapter;
import com.sjb.environment.application.CoreApplication;
import com.sjb.environment.base.BaseFragment;
import com.sjb.environment.util.DensityUtil;
import com.sjb.environment.view.popwindow.OnePopView;
import com.sjb.environment.view.popwindow.PopListener;
import com.sjb.environment.view.popwindow.TopPop;
import com.sjb.environment.view.scrollexpandable.ActionSlideExpandableListView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/15.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener{
    View view;
    @ViewInject(R.id.updatePass) private FrameLayout updatePass;
    @ViewInject(R.id.bindCount) private FrameLayout bindCount;
    @ViewInject(R.id.setMessage) private FrameLayout setMessage;
    @ViewInject(R.id.checkNew) private FrameLayout checkNew;
    @ViewInject(R.id.answer) private FrameLayout answer;
    @ViewInject(R.id.quit) private TextView quit;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        x.view().inject(this, view);
        setTitle("设置中心");
        hideArrow();
        canRefresh(false);
        updatePass.setOnClickListener(this);
        bindCount.setOnClickListener(this);
        setMessage.setOnClickListener(this);
        checkNew.setOnClickListener(this);
        answer.setOnClickListener(this);
        quit.setOnClickListener(this);
        return view;
    }

    @Override
    protected int getScrollView() {
        return 0;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.updatePass:
                intent.setClass(getActivity(),ChangePasActivity.class);
                startActivity(intent);
                break;
            case R.id.bindCount:
                intent.setClass(getActivity(),BindAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.setMessage:
                intent.setClass(getActivity(),PushSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.checkNew:
                intent.setClass(getActivity(),CheckUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.answer:
                intent.setClass(getActivity(),AnswerActivity.class);
                startActivity(intent);
                break;
            case R.id.quit:
                intent.setClass(getActivity(),LoginActivity.class);
                intent.putExtra("isCheck",1);
                startActivity(intent);
                getActivity().finish();
                break;
        }

    }
}
