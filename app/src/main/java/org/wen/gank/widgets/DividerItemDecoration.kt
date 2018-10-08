package org.wen.gank.widgets

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

/**
 * created by Jiahui.wen 2017-09-20
 */
class DividerItemDecoration(@ColorInt dividerColor: Int, private val dividerHeight: Int) : RecyclerView.ItemDecoration() {
  private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

  init {
    paint.color = dividerColor
  }

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                              state: RecyclerView.State) {
    outRect.bottom += dividerHeight
  }

  override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    super.onDrawOver(c, parent, state)

    val childCount = parent.childCount
    val left = parent.paddingLeft
    val right = parent.width - parent.paddingRight
    for (i in 0 until childCount) {
      val child = parent.getChildAt(i)

      val top = child.bottom
      val bottom = top + dividerHeight
      c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
    }
  }
}
