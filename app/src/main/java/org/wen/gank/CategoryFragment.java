package org.wen.gank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqldelight.SqlDelightStatement;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;
import org.wen.gank.api.GankApi;
import org.wen.gank.api.HttpResult;
import org.wen.gank.model.Gank;
import org.wen.gank.model.GankModel;
import org.wen.gank.mvp.MvpFragment;
import org.wen.gank.tools.LoadMoreDelegate;
import timber.log.Timber;

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

    @Inject GankApi gankApi;
    @Inject BriteDatabase database;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

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
        adapter = new MultiTypeAdapter(new ArrayList<>());
        adapter.register(Gank.class, new ItemViewBinder<Gank, GankViewHolder>() {
            @NonNull
            @Override
            protected GankViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                @NonNull ViewGroup parent) {
                View itemView =
                    inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                return new GankViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull GankViewHolder holder,
                @NonNull final Gank item) {
                holder.titleView.setText(item.description());
                holder.titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.go(getContext(), item.url(), item.description());
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        loadMoreDelegate = new LoadMoreDelegate().adapter(adapter)
            .listen(new LoadMoreDelegate.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    loadData(pageStart);
                }
            })
            .into(recyclerView);

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

        SqlDelightStatement statement = Gank.FACTORY.selectAllByCategory(category);
        database.createQuery(statement.tables, statement.statement, statement.args)
            .mapToList(new Function<Cursor, Gank>() {
                @Override
                public Gank apply(Cursor cursor) throws Exception {
                    return Gank.MAPPER.map(cursor);
                }
            })
            .take(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Gank>>() {
                @Override
                public void accept(List<Gank> ganks) throws Exception {
                    adapter.setItems(ganks);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(true);
                    loadData(1);
                    Timber.d("load database cache with size: %d", ganks.size());
                }
            });
    }

    private void loadData(final int start) {
        gankApi.getCategoryDatas(category, start, 15)
            .subscribeOn(Schedulers.io())
            .map(new Function<HttpResult<List<Gank>>, List<Gank>>() {
                @Override
                public List<Gank> apply(HttpResult<List<Gank>> result) throws Exception {
                    return result.results;
                }
            })
            .doOnNext(new Consumer<List<Gank>>() {
                @Override
                public void accept(List<Gank> ganks) throws Exception {
                    BriteDatabase.Transaction transaction = database.newTransaction();
                    try {
                        SQLiteDatabase db = database.getWritableDatabase();

                        if (start == 1) {
                            Gank.ClearByCategory clearAction = new GankModel.ClearByCategory(db);
                            clearAction.bind(category);
                            database.executeUpdateDelete(Gank.TABLE_NAME, clearAction.program);
                        }

                        Gank.InsertRow insertRow = new GankModel.InsertRow(db, Gank.FACTORY);
                        for (Gank gank : ganks) {
                            insertRow.bind(gank._id(), gank.url(), gank.type(), gank.description(),
                                gank.who(), gank.images(), gank.used(), gank.createdAt(),
                                gank.updatedAt(), gank.publishedAt());
                            database.executeInsert(Gank.TABLE_NAME, insertRow.program);
                        }

                        transaction.markSuccessful();
                    } finally {
                        transaction.end();
                    }
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Gank>>() {
                @Override
                public void accept(List<Gank> ganks) throws Exception {
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

    class GankViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;

        public GankViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
