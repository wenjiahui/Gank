package org.wen.gank.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * created by Jiahui.wen 2017-07-08
 */
abstract class MvpFragment<V : MvpView, P : MvpPresenter<V>> : Fragment(), MvpView {

  private lateinit var viewDelegate: ViewDelegate<V, P>
  private var firstLoad = false

  protected abstract var layoutRes: Int

  protected val presenter: P
    get() = viewDelegate.presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewDelegate = object : ViewDelegate<V, P>() {
      override fun createPresenter(): P {
        return this@MvpFragment.createPresenter()
      }
    }
    viewDelegate.onCreate(savedInstanceState)
  }

  protected abstract fun createPresenter(): P

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(layoutRes, container, false)
  }

  protected abstract fun initView(view: View)

  @Suppress("UNCHECKED_CAST")
  @CallSuper
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initView(view)
    viewDelegate.attachView(this as V)
    checkLazyLoad()
  }

  @CallSuper
  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    checkLazyLoad()
  }

  private fun checkLazyLoad() {
    if (firstLoad) return
    if (userVisibleHint && view != null) {
      firstLoad = true
      onLazyLoad()
    }
  }

  protected open fun onLazyLoad() {

  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewDelegate.onSaveInstanceState(outState)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    viewDelegate.detachView()
  }

  override fun onDestroy() {
    super.onDestroy()
    viewDelegate.onDestroy()
  }
}
