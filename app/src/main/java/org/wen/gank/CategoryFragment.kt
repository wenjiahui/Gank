package org.wen.gank

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_category.*
import me.drakeet.multitype.MultiTypeAdapter
import org.wen.gank.api.GankApi
import org.wen.gank.extensions.getCompatColor
import org.wen.gank.extensions.getDimen
import org.wen.gank.model.GankModel
import org.wen.gank.mvp.MvpFragment
import org.wen.gank.tools.AppDatabase
import org.wen.gank.tools.LoadMoreDelegate
import org.wen.gank.widgets.DividerItemDecoration
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * created by Jiahui.wen 2017-07-23
 */
class CategoryFragment : MvpFragment<CategoryView, CategoryPresenter>(), CategoryView {

  @Inject lateinit var gankApi: GankApi
  @Inject lateinit var mDatabase: AppDatabase

  override var layoutRes: Int = R.layout.fragment_category

  private var dividerColor: Int = 0
  private var dividerHeight: Int = 0

  private lateinit var category: String
  private var pageStart = 1

  private lateinit var adapter: MultiTypeAdapter
  private lateinit var loadMoreDelegate: LoadMoreDelegate

  override fun onAttach(context: Context) {
    super.onAttach(context)
    App.from(context).appComponent!!.inject(this)
    category = arguments?.getString(EXTRA_CATEGORY, ANDROID) ?: ANDROID
    dividerColor = context.getCompatColor(R.color.divider)
    dividerHeight = context.getDimen(R.dimen.divider)
  }

  override fun createPresenter(): CategoryPresenter {
    return CategoryPresenter()
  }

  override fun initView(view: View) {
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.addItemDecoration(DividerItemDecoration(dividerColor, dividerHeight))
    adapter = MultiTypeAdapter(ArrayList<Any>())
    adapter.register(GankModel::class.java, CategoryItemProvider())
    recyclerView.adapter = adapter
    loadMoreDelegate = LoadMoreDelegate().adapter(adapter).listen(object : LoadMoreDelegate.OnLoadMoreListener {
      override fun onLoadMore() {
        loadData(pageStart)
      }
    }).into(recyclerView!!)

    swipeRefreshLayout!!.setOnRefreshListener { loadData(1) }
  }

  override fun onLazyLoad() {
    super.onLazyLoad()

    mDatabase.gankDao().getGanksLimit(category).take(1).observeOn(AndroidSchedulers.mainThread())
        .subscribe { ganks ->
          adapter.items = ganks
          adapter.notifyDataSetChanged()
          swipeRefreshLayout!!.isRefreshing = true
          loadData(1)
          Timber.d("load database cache with size: %d", ganks.size)
        }
  }

  private fun loadData(start: Int) {
    gankApi.getCategoryDatas(category, start, 15).subscribeOn(Schedulers.io())
        .map { (_, results) -> results }.doOnNext { gankModels -> mDatabase.gankDao().batchInsert(gankModels) }.observeOn(AndroidSchedulers.mainThread()).subscribe({ ganks ->
          if (start == 1) pageStart = 1
          pageStart++
          if (ganks.isEmpty()) {
            loadMoreDelegate.loadEnd()
          } else {
            loadMoreDelegate.loadFinish(ganks, start == 1)
          }
          swipeRefreshLayout.isRefreshing = false
        }, { throwable ->
          Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
          swipeRefreshLayout!!.isRefreshing = false
          loadMoreDelegate.loadError(throwable)
          Timber.e(throwable)
        })
  }

  companion object {

    private const val EXTRA_CATEGORY = "category"

    const val ANDROID = "Android"
    const val IOS = "iOS"
    const val FRONT = "前端"

    fun getInstance(category: String): CategoryFragment {
      val fragment = CategoryFragment()
      val data = Bundle()
      data.putString(EXTRA_CATEGORY, category)
      fragment.arguments = data
      return fragment
    }
  }
}
