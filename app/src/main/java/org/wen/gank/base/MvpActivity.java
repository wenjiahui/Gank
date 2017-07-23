package org.wen.gank.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * created by Jiahui.wen 2017-07-14
 */
public abstract class MvpActivity<V extends MvpView, P extends MvpPresenter<V>>
    extends AppCompatActivity implements MvpView {

    private ViewDelegate<V, P> viewDelegate;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDelegate = new ViewDelegate<V, P>() {
            @Override
            protected P createPresenter() {
                return MvpActivity.this.createPresenter();
            }
        };
        viewDelegate.onCreate(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        viewDelegate.attachView((V) this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initView();
        viewDelegate.attachView((V) this);
    }

    protected abstract void initView();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewDelegate.detachView();
        viewDelegate.onDestroy();
    }
}
