package org.wen.gank.base;

import android.os.Bundle;

/**
 * created by Jiahui.wen 2017-07-14
 */
public class MvpPresenter<V extends MvpView> {

    private V view;

    protected void onCreate(Bundle savedInstanceState) {

    }

    protected void onSaveInstanceState(Bundle outState) {

    }

    protected void onDestroy() {

    }

    protected final void attachView(V view) {
        this.view = view;
        onAttachView(view);
    }

    protected void onAttachView(V view) {

    }

    protected final void detachView(boolean retainInstance) {
        this.view = null;
        onDetachView();
    }

    protected void onDetachView() {

    }

    public V getView() {
        return view;
    }

    public boolean isViewAttach() {
        return view != null;
    }
}
