package org.wen.gank;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.wen.gank.api.GankApi;
import org.wen.gank.api.HttpResult;
import org.wen.gank.model.GankModel2;
import org.wen.gank.mvp.MvpFragment;
import org.wen.gank.tools.AppDatabase;
import org.wen.gank.tools.LoadMoreDelegate;
import org.wen.gank.widgets.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;
import timber.log.Timber;

/**
 * created by Jiahui.wen 2017-07-23
 */
public class CategoryFragment extends MvpFragment<CategoryView, CategoryPresenter> implements CategoryView {

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

    @Inject
    GankApi gankApi;
    @Inject
    AppDatabase mDatabase;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindColor(R.color.divider)
    int dividerColor;
    @BindDimen(R.dimen.divider)
    int dividerHeight;

    private String category;
    private int pageStart = 1;

    MultiTypeAdapter adapter;
    LoadMoreDelegate loadMoreDelegate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.from(context).getAppComponent().inject(this);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(dividerColor, dividerHeight));
        adapter = new MultiTypeAdapter(new ArrayList<>());
        adapter.register(GankModel2.class, new CategoryItemProvider());
        recyclerView.setAdapter(adapter);
        loadMoreDelegate = new LoadMoreDelegate().adapter(adapter).listen(new LoadMoreDelegate.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(pageStart);
            }
        }).into(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();

        mDatabase.gankDao().getGanksLimit(category).take(1).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GankModel2>>() {
                    @Override
                    public void accept(List<GankModel2> ganks) throws Exception {
                        adapter.setItems(ganks);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(true);
                        loadData(1);
                        Timber.d("load database cache with size: %d", ganks.size());
                    }
                });
    }

    private void loadData(final int start) {
        gankApi.getCategoryDatas(category, start, 15).subscribeOn(Schedulers.io())
                .map(new Function<HttpResult<List<GankModel2>>, List<GankModel2>>() {
                    @Override
                    public List<GankModel2> apply(HttpResult<List<GankModel2>> result) throws Exception {
                        return result.results;
                    }
                }).doOnNext(new Consumer<List<GankModel2>>() {
            @Override
            public void accept(List<GankModel2> gankModel2s) throws Exception {
                mDatabase.gankDao().batchinsert(gankModel2s);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<GankModel2>>() {
            @Override
            public void accept(List<GankModel2> ganks) throws Exception {
                if (start == 1) pageStart = 1;
                pageStart++;
                if (ganks.isEmpty()) {
                    loadMoreDelegate.loadEnd();
                } else {
                    loadMoreDelegate.loadFinish(ganks, start == 1);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                loadMoreDelegate.loadError(throwable);
            }
        });
    }
}
