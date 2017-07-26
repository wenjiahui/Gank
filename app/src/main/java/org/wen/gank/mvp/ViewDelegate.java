package org.wen.gank.mvp;

import android.os.Bundle;

/**
 * created by Jiahui.wen 2017-07-08
 */
abstract class ViewDelegate<V extends MvpView, P extends MvpPresenter<V>> {

    private P presenter;

    public void onCreate(Bundle saveInstance) {
        presenter = createPresenter();
        presenter.onCreate(saveInstance);
    }

    public void attachView(V view) {
        presenter.attachView(view);
    }

    protected void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    public void onDestroy() {
        presenter.onDestroy();
    }

    public void detachView() {
        presenter.detachView(false);
    }

    protected abstract P createPresenter();

    public P getPresenter() {
        return presenter;
    }
}
