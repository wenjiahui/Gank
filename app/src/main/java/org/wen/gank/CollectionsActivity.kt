package org.wen.gank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.wen.gank.mvp.MvpActivity

/**
 * created by Jiahui.wen 2017-08-21
 */
class CollectionsActivity : MvpActivity<CollectionsView, CollectionsPresenter>(), CollectionsView {

  override fun createPresenter(): CollectionsPresenter {
    return CollectionsPresenter()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_collections)
  }

  override fun initView() {
  }

  companion object {

    fun navigateTo(context: Context) {
      val intent = Intent(context, CollectionsActivity::class.java)
      context.startActivity(intent)
    }
  }
}
