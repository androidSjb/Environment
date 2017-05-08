package com.sjb.environment.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sjb.environment.R;
import com.sjb.environment.application.CoreApplication;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by yueneng-cc on 16/5/12.
 */
public class BaseActivity extends Activity {
    @ViewInject(R.id.base_windows_right) protected ImageView base_windows_right;
    @ViewInject(R.id.base_windows_right_llt) protected LinearLayout base_windows_right_llt;
    protected LinearLayout loadingFrame;
    private ImageView imageView;
    protected LinearLayout activity_base_container;
    protected FrameLayout base_windows_container;
    private AnimationDrawable animationDrawable;
    protected boolean isLogin = false;
    LinearLayout base_windows_left_llt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        x.view().inject(this);
        loadingFrame = (LinearLayout)findViewById(R.id.loadingFrame);
        imageView = (ImageView)findViewById(R.id.loading_image);
        activity_base_container = (LinearLayout) findViewById(R.id.activity_base_container);
        base_windows_container = (FrameLayout)findViewById(R.id.base_windows_container);

        AnimationDrawable  frameAnim=(AnimationDrawable) CoreApplication.getInstance().getResources().getDrawable(R.drawable.loading_animal);
        imageView.setBackgroundDrawable(frameAnim);

        activity_base_container.setBackgroundColor(getResources().getColor(R.color.app_bg_white));

        animationDrawable = (AnimationDrawable) imageView.getBackground();
        base_windows_left_llt = (LinearLayout)findViewById(R.id.base_windows_left_llt);
        base_windows_left_llt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoreApplication.getInstance().removeActivity(BaseActivity.this);
                finish();
            }
        });
        CoreApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoreApplication.isActivityReStart = true;
        CoreApplication.isFragmentReStart = true;
        CoreApplication.removeActivity(this);
    }

    protected void hideTitle(){
        base_windows_container.setVisibility(View.GONE);
    }
    protected void setMidTitle(String title){
        TextView base_windows_title = (TextView)findViewById(R.id.base_windows_title);
        base_windows_title.setText(title);
    }

    protected void hideLeftBtn(){
        findViewById(R.id.base_windows_left_llt).setVisibility(View.GONE);
    }
    protected void showFilter( View.OnClickListener listener){
        base_windows_right.setVisibility(View.VISIBLE);
        base_windows_right.setOnClickListener(listener);
    }

    protected void setRithtTextOnClickListener(String text, View.OnClickListener listener){
        TextView textView = (TextView)findViewById(R.id.base_windows_right_text);
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);
        textView.setOnClickListener(listener);
    }


    protected void setAnimal(){
        loadingFrame.setVisibility(View.VISIBLE);
        animationDrawable.start();

    }

    protected  void stopAnimal(){
        if (loadingFrame != null){
            animationDrawable.stop();
            loadingFrame.setVisibility(View.GONE);
        }

    }

    /**
     * 设置主内容区域的layoutRes
     * @param layoutResId
     */
    protected void setLayoutContenner(int layoutResId) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        activity_base_container.addView(v);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
    }
}
