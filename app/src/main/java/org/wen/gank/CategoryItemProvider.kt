package org.wen.gank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.ItemViewBinder
import org.wen.gank.CategoryItemProvider.GankViewHolder
import org.wen.gank.model.GankModel

/**
 * created by Jiahui.wen 2017-09-20
 */
class CategoryItemProvider : ItemViewBinder<GankModel, GankViewHolder>() {

  override fun onCreateViewHolder(
      inflater: LayoutInflater, parent: ViewGroup): GankViewHolder {
    val itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
    return GankViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: GankViewHolder, item: GankModel) {
    holder.titleView.text = item.desc
    holder.titleView.setOnClickListener { v -> WebViewActivity.go(v.context, item.url!!, item.desc!!) }
  }

  inner class GankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var titleView: TextView = itemView.findViewById<View>(android.R.id.text1) as TextView
  }
}
