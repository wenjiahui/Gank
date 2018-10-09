package org.wen.gank.mvp

import android.os.Bundle

/**
 * created by Jiahui.wen 2017-07-14
 */
open class MvpPresenter<V : MvpView> {

  var view: V? = null
    private set

  val isViewAttach: Boolean
    get() = view != null

  @Suppress("UseExpressionBody", "UNUSED_PARAMETER")
  fun onCreate(savedInstanceState: Bundle?) {

  }

  @Suppress("UseExpressionBody", "UNUSED_PARAMETER")
  fun onSaveInstanceState(outState: Bundle) {

  }

  fun onDestroy() {

  }

  fun attachView(view: V) {
    this.view = view
    onAttachView(view)
  }

  @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
  protected fun onAttachView(view: V) {

  }

  @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
  fun detachView(retainInstance: Boolean) {
    this.view = null
    onDetachView()
  }

  @Suppress("MemberVisibilityCanBePrivate")
  protected fun onDetachView() {

  }
}
