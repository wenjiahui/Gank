package org.wen.gank.widgets

import android.content.Context
import android.support.v4.view.NestedScrollingChild
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView

/**
 * created by Jiahui.wen 2017-09-20
 */
class NestedWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.webViewStyle) : WebView(context, attrs, defStyleAttr), NestedScrollingChild {

  private var mLastY: Int = 0
  private val mScrollOffset = IntArray(2)
  private val mScrollConsumed = IntArray(2)

  private val nestedChildHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)

  init {
    isNestedScrollingEnabled = true
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    var returnValue = false

    val event = MotionEvent.obtain(ev)
    val action = event.actionMasked

    val eventY = event.y.toInt()

    when (action) {
      MotionEvent.ACTION_MOVE -> {
        var deltaY = mLastY - eventY
        if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
          deltaY -= mScrollConsumed[1]
        }
        returnValue = super.onTouchEvent(event)
        if (dispatchNestedScroll(0, mScrollOffset[1], 0, deltaY, mScrollOffset)) {
          mLastY -= mScrollOffset[1]
        }
      }
      MotionEvent.ACTION_DOWN -> {
        mLastY = eventY
        returnValue = super.onTouchEvent(event)
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        returnValue = super.onTouchEvent(event)
        stopNestedScroll()
      }
    }
    return returnValue
  }

  override fun setNestedScrollingEnabled(enabled: Boolean) {
    nestedChildHelper.isNestedScrollingEnabled = enabled
  }

  override fun isNestedScrollingEnabled(): Boolean {
    return nestedChildHelper.isNestedScrollingEnabled
  }

  override fun startNestedScroll(axes: Int): Boolean {
    return nestedChildHelper.startNestedScroll(axes)
  }

  override fun stopNestedScroll() {
    nestedChildHelper.stopNestedScroll()
  }

  override fun hasNestedScrollingParent(): Boolean {
    return nestedChildHelper.hasNestedScrollingParent()
  }

  override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                                    dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
    return nestedChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed, offsetInWindow)
  }

  override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
    return nestedChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
  }

  override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
    return nestedChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
  }

  override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
    return nestedChildHelper.dispatchNestedPreFling(velocityX, velocityY)
  }
}
