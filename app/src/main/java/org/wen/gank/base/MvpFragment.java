package org.wen.gank.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * created by Jiahui.wen 2017-07-08
 */
public abstract class MvpFragment<V extends MvpView, P extends MvpPresenter<V>> extends Fragment
    implements MvpView {

    private ViewDelegate<V, P> viewDelegate;
    private boolean firstLoad = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDelegate = new ViewDelegate<V, P>() {
            @Override
            protected P createPresenter() {
                return MvpFragment.this.createPresenter();
            }
        };
        viewDelegate.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();

    protected abstract int getLayoutRes();

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        initView(view);
        viewDelegate.attachView((V) this);
        return view;
    }

    protected abstract void initView(View view);

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkLazyLoad();
    }

    @CallSuper
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        checkLazyLoad();
    }

    private void checkLazyLoad() {
        if (firstLoad) return;
        if (getUserVisibleHint() && getView() != null) {
            firstLoad = true;
            onLazyLoad();
        }
    }

    protected void onLazyLoad() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewDelegate.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDelegate.onDestroy();
    }

    protected P getPresenter() {
        return viewDelegate.getPresenter();
    }
}
