package org.wen.gank;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.MultiTypeAdapter;
import org.wen.gank.base.MvpFragment;

/**
 * created by Jiahui.wen 2017-07-23
 */
public class CategoryFragment extends MvpFragment<CategoryView, CategoryPresenter>
    implements CategoryView {

    private static final String EXTRA_CATEGORY = "category";

    public static final String ANDROID = "Android";
    public static final String IOS = "iOS";
    public static final String FRONT = "前端";

    public static CategoryFragment getInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle data = new Bundle();
        data.putString(EXTRA_CATEGORY, category);
        fragment.setArguments(data);
        return fragment;
    }

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    private String category;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        category = getArguments().getString(EXTRA_CATEGORY, "");
    }

    @Override
    protected CategoryPresenter createPresenter() {
        return new CategoryPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        recyclerView.setAdapter(adapter);
    }
}
