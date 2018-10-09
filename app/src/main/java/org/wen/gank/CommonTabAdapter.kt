package org.wen.gank

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

/**
 * created by Jiahui.wen 2017-07-23
 */
class CommonTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

  private var pagerItems: List<FragmentPagerItem>? = null

  override fun getItem(position: Int): Fragment {
    return pagerItems!![position].fragment
  }

  override fun getCount(): Int {
    return if (pagerItems != null) pagerItems!!.size else 0
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return pagerItems!![position].title
  }

  fun setData(pagerItems: List<FragmentPagerItem>) {
    this.pagerItems = pagerItems
  }

  //=============================================================
  // Builder
  //=============================================================

  class Builder(private val context: Context, private val fragmentManager: FragmentManager) {

    private val fragmentItems = ArrayList<FragmentPagerItem>()

    fun add(@StringRes title: Int, fragment: Fragment): Builder {
      return add(context.getString(title), fragment)
    }

    fun add(title: String, fragment: Fragment): Builder {
      val item = FragmentPagerItem(title, fragment)
      fragmentItems.add(item)
      return this
    }

    fun into(viewPager: ViewPager): CommonTabAdapter {
      val adapter = CommonTabAdapter(fragmentManager)
      adapter.setData(fragmentItems)
      viewPager.adapter = adapter
      return adapter
    }
  }

  class FragmentPagerItem(val title: String, val fragment: Fragment)
}
