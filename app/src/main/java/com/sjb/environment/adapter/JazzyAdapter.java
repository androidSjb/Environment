package com.sjb.environment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.sjb.environment.view.jazzyviewpager.JazzyViewPager;
import java.util.ArrayList;

/**
 * Created by yueneng-cc on 16/6/16.
 */
public class JazzyAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;//需要添加到上面的Fragment
    private JazzyViewPager mJazzy;

    public JazzyAdapter(FragmentManager fm) {
        super(fm);
    }
    /**
     * 自定义的构造函数
     * @param fm
     * @param fragments ArrayList<Fragment>
     */
    public JazzyAdapter(FragmentManager fm, ArrayList<Fragment> fragments, JazzyViewPager mJazzy) {
        super(fm);
        this.fragments = fragments;
        this.mJazzy = mJazzy;
    }
    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);//返回Fragment对象
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        mJazzy.setObjectForPosition(obj, position);
        return obj;
    }

    @Override
    public int getCount() {
        return fragments.size();//返回Fragment的个数
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        super.destroyItem(container, position, object);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (object != null) {
            return ((Fragment) object).getView() == view;
        } else {
            return false;
        }
    }
}
