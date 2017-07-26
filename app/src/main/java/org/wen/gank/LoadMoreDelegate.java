package org.wen.gank;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * created by Jiahui.wen 2017-07-25
 */
public class LoadMoreDelegate {

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private RecyclerView recyclerView;
    private MultiTypeAdapter adapter;
    private OnLoadMoreListener loadMoreListener;

    private boolean loading = false;
    private final LoadMoreModel model = new LoadMoreModel();

    public LoadMoreDelegate listen(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
        return this;
    }

    public LoadMoreDelegate adapter(MultiTypeAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public LoadMoreDelegate into(@NonNull RecyclerView recyclerView) {
        adapter.register(LoadMoreModel.class, new LoadMoreItemViewBinder());
        this.recyclerView = recyclerView;
        final RecyclerView.LayoutManager layoutManager = this.recyclerView.getLayoutManager();
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (model.state == LoadMoreModel.STATE_END) return;

                if (dy > 0 && model.state != LoadMoreModel.STATE_LOADING) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = ((LinearLayoutManager) layoutManager);
                        int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                        int indexOf = adapter.getItems().indexOf(model);
                        if (indexOf != lastVisibleItemPosition) {
                            model.state = LoadMoreModel.STATE_LOADING;
                        }
                    }
                }
            }
        });

        return this;
    }

    public void loadError(Throwable throwable) {
        loading = false;
        changeLoadStatus(LoadMoreModel.STATE_ERROR);
    }

    private void changeLoadStatus(int status) {
        model.state = status;
        List items = adapter.getItems();
        int indexOf = items.indexOf(model);
        if (indexOf >= 0) {
            adapter.notifyItemChanged(indexOf);
        } else {
            int position = items.size();
            items.add(model);
            adapter.notifyItemInserted(position);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFinish(List<? extends Object> datas) {
        loadFinish(datas, false);
    }

    @SuppressWarnings("unchecked")
    public void loadFinish(List<? extends Object> datas, boolean clear) {
        loading = false;
        List<Object> items = (List<Object>) adapter.getItems();
        if (clear) {
            int size = items.size();
            items.clear();
            adapter.notifyItemRangeRemoved(0, size);
        }
        int indexOf = items.indexOf(model);
        if (indexOf >= 0) {
            items.remove(indexOf);
            adapter.notifyItemRemoved(indexOf);
        }
        int start = items.size();
        items.addAll(datas);
        adapter.notifyItemRangeInserted(start, datas.size());

        changeLoadStatus(LoadMoreModel.STATE_IDLE);
    }

    public void loadEnd() {
        loading = false;
        changeLoadStatus(LoadMoreModel.STATE_END);
    }

    private void startLoadMore(boolean notify) {
        if (loading) return;
        loading = true;
        if (notify) {
            changeLoadStatus(LoadMoreModel.STATE_LOADING);
            model.state = LoadMoreModel.STATE_LOADING;
        }
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }

    class LoadMoreItemViewBinder extends ItemViewBinder<LoadMoreModel, LoadMoreItemView> {

        @NonNull
        @Override
        protected LoadMoreItemView onCreateViewHolder(@NonNull LayoutInflater inflater,
            @NonNull ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.item_loading_more, parent, false);
            return new LoadMoreItemView(itemView);
        }

        @Override
        protected void onBindViewHolder(@NonNull LoadMoreItemView holder,
            @NonNull LoadMoreModel item) {

            holder.errorView.setVisibility(
                item.state == LoadMoreModel.STATE_ERROR ? View.VISIBLE : View.GONE);

            holder.idleView.setVisibility(
                item.state == LoadMoreModel.STATE_IDLE ? View.VISIBLE : View.GONE);

            holder.endView.setVisibility(
                item.state == LoadMoreModel.STATE_END ? View.VISIBLE : View.GONE);

            holder.loadingView.setVisibility(
                item.state == LoadMoreModel.STATE_LOADING ? View.VISIBLE : View.GONE);

            if (item.state == LoadMoreModel.STATE_LOADING) {
                startLoadMore(false);
            }
        }
    }

    class LoadMoreItemView extends RecyclerView.ViewHolder {

        @BindView(R.id.error_view) TextView errorView;
        @BindView(R.id.idle_view) TextView idleView;
        @BindView(R.id.end_view) TextView endView;
        @BindView(R.id.loading_view) ProgressBar loadingView;

        public LoadMoreItemView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({ R.id.error_view, R.id.idle_view, R.id.loading_view })
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.error_view:
                    startLoadMore(true);
                    break;
                case R.id.idle_view:
                    startLoadMore(true);
                    break;
                case R.id.loading_view:
                    break;
            }
        }
    }

    public class LoadMoreModel {

        public static final int STATE_IDLE = 0;
        public static final int STATE_LOADING = 1;
        public static final int STATE_ERROR = 2;
        public static final int STATE_END = 3;

        public int state;
    }
}
