package org.wen.gank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Jiahui.wen 2017-07-23
 */
public class CommonTabAdapter extends FragmentPagerAdapter {

    private List<FragmentPagerItem> pagerItems;

    public CommonTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pagerItems.get(position).fragment;
    }

    @Override
    public int getCount() {
        return pagerItems != null ? pagerItems.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerItems.get(position).title;
    }

    void setData(List<FragmentPagerItem> pagerItems) {
        this.pagerItems = pagerItems;
    }

    //=============================================================
    // Builder
    //=============================================================

    public static class Builder {

        private final Context context;
        private final FragmentManager fragmentManager;

        private List<FragmentPagerItem> fragmentItems = new ArrayList<>();

        public Builder(Context context, FragmentManager fragmentManager) {
            this.context = context;
            this.fragmentManager = fragmentManager;
        }

        public Builder add(@StringRes int title, @NonNull Fragment fragment) {
            return add(context.getString(title), fragment);
        }

        public Builder add(String title, @NonNull Fragment fragment) {
            FragmentPagerItem item = new FragmentPagerItem(title, fragment);
            fragmentItems.add(item);
            return this;
        }

        public CommonTabAdapter into(@NonNull ViewPager viewPager) {
            CommonTabAdapter adapter = new CommonTabAdapter(fragmentManager);
            adapter.setData(fragmentItems);
            viewPager.setAdapter(adapter);
            return adapter;
        }
    }

    private static class FragmentPagerItem {

        public final String title;
        public final Fragment fragment;

        public FragmentPagerItem(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }
    }
}
