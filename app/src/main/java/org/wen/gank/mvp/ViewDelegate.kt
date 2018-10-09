package org.wen.gank.mvp

import android.os.Bundle

/**
 * created by Jiahui.wen 2017-07-08
 */
internal abstract class ViewDelegate<V : MvpView, P : MvpPresenter<V>> {

  lateinit var presenter: P

  fun onCreate(saveInstance: Bundle?) {
    presenter = createPresenter()
    presenter.onCreate(saveInstance)
  }

  fun attachView(view: V) {
    presenter.attachView(view)
  }

  fun onSaveInstanceState(outState: Bundle) {
    presenter.onSaveInstanceState(outState)
  }

  fun onDestroy() {
    presenter.onDestroy()
  }

  fun detachView() {
    presenter.detachView(false)
  }

  protected abstract fun createPresenter(): P
}
