package org.wen.gank.tools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter
import org.wen.gank.R
import org.wen.gank.extensions.isVisible
import timber.log.Timber

/**
 * created by Jiahui.wen 2017-07-25
 */
class LoadMoreDelegate {

  private lateinit var recyclerView: RecyclerView
  private lateinit var adapter: MultiTypeAdapter
  private var loadMoreListener: OnLoadMoreListener? = null

  private var loading = false
  private val model = LoadMoreModel()

  interface OnLoadMoreListener {
    fun onLoadMore()
  }

  fun listen(loadMoreListener: OnLoadMoreListener): LoadMoreDelegate {
    this.loadMoreListener = loadMoreListener
    return this
  }

  fun adapter(adapter: MultiTypeAdapter): LoadMoreDelegate {
    this.adapter = adapter
    return this
  }

  fun into(recyclerView: RecyclerView): LoadMoreDelegate {
    adapter.register(LoadMoreModel::class.java, LoadMoreItemViewBinder())
    this.recyclerView = recyclerView
    val layoutManager = this.recyclerView.layoutManager
    this.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (model.state == LoadMoreModel.STATE_END) return

        if (dy > 0 && model.state != LoadMoreModel.STATE_LOADING) {
          if (layoutManager is LinearLayoutManager) {
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val indexOf = adapter.items.indexOf(model)
            if (indexOf != lastVisibleItemPosition) {
              model.state = LoadMoreModel.STATE_LOADING
            }
          }
        }
      }
    })

    return this
  }

  fun loadError(throwable: Throwable) {
    Timber.e(throwable)
    loading = false
    changeLoadStatus(LoadMoreModel.STATE_ERROR)
  }

  @Suppress("UNCHECKED_CAST")
  private fun changeLoadStatus(status: Int) {
    model.state = status
    val items = adapter.items as MutableList<Any>
    val indexOf = items.indexOf(model)
    if (indexOf >= 0) {
      adapter.notifyItemChanged(indexOf)
    } else {
      val position = items.size
      items.size
      items.add(model)
      adapter.notifyItemInserted(position)
    }
  }

  @Suppress("UNCHECKED_CAST")
  @JvmOverloads
  fun loadFinish(datas: List<Any>, clear: Boolean = false) {
    loading = false
    val items = adapter.items as MutableList<Any>
    if (clear) {
      val size = items.size
      items.clear()
      adapter.notifyItemRangeRemoved(0, size)
    }
    val indexOf = items.indexOf(model)
    if (indexOf >= 0) {
      items.removeAt(indexOf)
      adapter.notifyItemRemoved(indexOf)
    }
    val start = items.size
    items.addAll(datas)
    adapter.notifyItemRangeInserted(start, datas.size)

    changeLoadStatus(LoadMoreModel.STATE_IDLE)
  }

  fun loadEnd() {
    loading = false
    changeLoadStatus(LoadMoreModel.STATE_END)
  }

  private fun startLoadMore(notify: Boolean) {
    if (loading) return
    loading = true
    if (notify) {
      changeLoadStatus(LoadMoreModel.STATE_LOADING)
      model.state = LoadMoreModel.STATE_LOADING
    }
    if (loadMoreListener != null) {
      loadMoreListener!!.onLoadMore()
    }
  }

  internal inner class LoadMoreItemViewBinder : ItemViewBinder<LoadMoreModel, LoadMoreItemView>() {

    override fun onCreateViewHolder(inflater: LayoutInflater,
                                    parent: ViewGroup): LoadMoreItemView {
      val itemView = inflater.inflate(R.layout.item_loading_more, parent, false)
      return LoadMoreItemView(itemView)
    }

    override fun onBindViewHolder(holder: LoadMoreItemView, item: LoadMoreModel) {
      holder.errorView.isVisible = item.state == LoadMoreModel.STATE_ERROR
      holder.idleView.isVisible = item.state == LoadMoreModel.STATE_IDLE
      holder.endView.isVisible = item.state == LoadMoreModel.STATE_END
      holder.loadingView.isVisible = item.state == LoadMoreModel.STATE_LOADING

      if (item.state == LoadMoreModel.STATE_LOADING) {
        startLoadMore(false)
      }
    }
  }

  internal inner class LoadMoreItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var errorView: TextView = itemView.findViewById(R.id.error_view)
    var idleView: TextView = itemView.findViewById(R.id.idle_view)
    var endView: TextView = itemView.findViewById(R.id.end_view)
    var loadingView: ProgressBar = itemView.findViewById(R.id.loading_view)

    init {
      errorView.setOnClickListener {
        startLoadMore(true)
      }
      idleView.setOnClickListener { startLoadMore(true) }
    }
  }

  class LoadMoreModel {

    var state: Int = 0

    companion object {

      const val STATE_IDLE = 0
      const val STATE_LOADING = 1
      const val STATE_ERROR = 2
      const val STATE_END = 3
    }
  }
}
