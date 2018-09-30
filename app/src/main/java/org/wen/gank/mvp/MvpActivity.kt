package org.wen.gank.mvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * created by Jiahui.wen 2017-07-14
 */
abstract class MvpActivity<V : MvpView, P : MvpPresenter<V>> : AppCompatActivity(), MvpView {

  private lateinit var viewDelegate: ViewDelegate<V, P>

  protected val presenter: P
    get() = viewDelegate.presenter

  protected abstract fun createPresenter(): P

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewDelegate = object : ViewDelegate<V, P>() {
      override fun createPresenter(): P {
        return this@MvpActivity.createPresenter()
      }
    }
    viewDelegate.onCreate(savedInstanceState)
  }

  @Suppress("UNCHECKED_CAST")
  override fun setContentView(layoutResID: Int) {
    super.setContentView(layoutResID)
    initView()
    viewDelegate.attachView(this as V)
  }

  @Suppress("UNCHECKED_CAST")
  override fun setContentView(view: View) {
    super.setContentView(view)
    initView()
    viewDelegate.attachView(this as V)
  }

  protected abstract fun initView()

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewDelegate.onSaveInstanceState(outState)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewDelegate.detachView()
    viewDelegate.onDestroy()
  }
}
