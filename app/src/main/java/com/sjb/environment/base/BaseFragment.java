
package com.sjb.environment.base;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.sjb.environment.R;
import com.sjb.environment.application.CoreApplication;
import com.sjb.environment.view.jazzyviewpager.JazzyViewPager;
import com.sjb.environment.view.scrollexpandable.ActionSlideExpandableListView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * Created by yueneng-cc on 16/6/15.
 */
public abstract class BaseFragment extends Fragment {

    @ViewInject(R.id.basew_search) private LinearLayout baseSearch;
    @ViewInject(R.id.base_search_edit) private EditText base_search_edit;
    private ListView listView;
    private JazzyViewPager mJazzyViewPager;
    public Activity mActivity;
    private LinearLayout loadingFrame;
    private ImageView imageView;
    public ViewGroup view;
    private LinearLayout baseMiddle;
    private TextView base_windows_title;
    private ImageView base_windows_arrow_down;
    private LinearLayout contentContainerScroll;
    private ScrollView yn_base;
    protected PtrClassicFrameLayout ptrFrameLayout;
    private AnimationDrawable animationDrawable;
    private ExpandableListView mActionSlideExpandableListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(contentContainerScroll!=null){
            contentContainerScroll.removeAllViews();
        }
        if(yn_base!=null){
            yn_base.removeAllViews();
        }
        view = (ViewGroup) inflater.inflate(R.layout.base_fragment, null);

        x.view().inject(this, view);

        ptrFrameLayout = (PtrClassicFrameLayout)view.findViewById(R.id.material_style_ptr_frame);

        contentContainerScroll = (LinearLayout)view.findViewById(R.id.yn_base_scroll);
        yn_base = (ScrollView)view.findViewById(R.id.yn_base);

        base_windows_arrow_down = (ImageView)view.findViewById(R.id.base_windows_arrow_down);
        base_windows_title = (TextView)view.findViewById(R.id.base_windows_title);
        baseMiddle = (LinearLayout) view.findViewById(R.id.base_windows_midllt_1);

        loadingFrame = (LinearLayout)view.findViewById(R.id.loadingFrame);
        imageView = (ImageView)view.findViewById(R.id.loading_image);

        base_windows_title.setTextColor(getResources().getColor(R.color.text_select_white));

        contentContainerScroll.setBackgroundColor(getResources().getColor(R.color.app_bg_white));

        final MaterialHeader header = new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(ptrFrameLayout);
        ptrFrameLayout.setPinContent(true);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);

        AnimationDrawable frameAnim=(AnimationDrawable) CoreApplication.getInstance().getResources().getDrawable(R.drawable.loading_animal);
        imageView.setBackgroundDrawable(frameAnim);

        ptrFrameLayout.setLoadingMinTime(100);
        ptrFrameLayout.setDurationToCloseHeader(500);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (getScrollView() == 0) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, yn_base, header);
                } else if (getScrollView() == 1) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
                } else if (getScrollView() == 2) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, mJazzyViewPager, header);
                } else if (getScrollView() == 3) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, mActionSlideExpandableListView, header);
                } else {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, contentContainerScroll, header);
                }
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                        loadData();
            }
        });

        animationDrawable = (AnimationDrawable) imageView.getBackground();

        View contentView = createView(inflater, view, savedInstanceState);

        contentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (getScrollView()==0) {
            yn_base.addView(contentView);
            yn_base.setVisibility(View.VISIBLE);
            contentContainerScroll.setVisibility(View.GONE);
        } else {
            contentContainerScroll.addView(contentView);
            contentContainerScroll.setVisibility(View.VISIBLE);
            yn_base.setVisibility(View.GONE);
        }

        return view;
    }


    public void setTitle(String title){
        baseMiddle.setVisibility(View.VISIBLE);
        base_windows_title.setText(title);
    }

    protected void hideArrow(){
        base_windows_arrow_down.setVisibility(View.GONE);
        baseMiddle.setClickable(false);
    }


    public void setListView(ListView listView){
        this.listView = listView;
    }

    public void setBigList(ExpandableListView mActionSlideExpandableListView){
        this.mActionSlideExpandableListView = mActionSlideExpandableListView;
    }
    public void setViewPager(JazzyViewPager mJazzyViewPager){
        this.mJazzyViewPager = mJazzyViewPager;
    }

    public void setAnimal(){
        loadingFrame.setVisibility(View.VISIBLE);
        animationDrawable.start();
    }

    public void showSearch(TextView.OnEditorActionListener mOnEditorActionListener){
        baseSearch.setVisibility(View.VISIBLE);
        base_search_edit.setOnEditorActionListener(mOnEditorActionListener);
    }
    public void stopAnimal(){
        if (loadingFrame != null){
            if (animationDrawable != null)
                animationDrawable.stop();
            loadingFrame.setVisibility(View.GONE);


        }
        if (ptrFrameLayout != null){
            ptrFrameLayout.refreshComplete();
        }
    }

    protected void canRefresh(boolean tag){
        if(!tag){
            ptrFrameLayout.setEnabled(false);
        }else{
            ptrFrameLayout.setEnabled(true);
        }
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract int getScrollView();
    protected abstract void loadData();


}

