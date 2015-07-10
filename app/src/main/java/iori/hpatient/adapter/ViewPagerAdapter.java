package iori.hpatient.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import iori.hpatient.activity.base.BasePager;
import iori.hpatient.view.MyViewPager;


/**
 * Created by Administrator on 2015/6/6.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<BasePager> list;

    public ViewPagerAdapter(List<BasePager> pages) {
        this.list = pages;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((MyViewPager) container).removeView(list.get(position)
                .getRootView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((MyViewPager) container).addView(list.get(position).getRootView(),
                0);
        return list.get(position).getRootView();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}