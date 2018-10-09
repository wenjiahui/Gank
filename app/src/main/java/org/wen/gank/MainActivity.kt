package org.wen.gank

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)
    val actionBar = supportActionBar
    actionBar?.setTitle(R.string.main_title)

    val tabAdapter = CommonTabAdapter.Builder(this, supportFragmentManager).add(
        CategoryFragment.ANDROID, CategoryFragment.getInstance(CategoryFragment.ANDROID))
        .add(CategoryFragment.IOS, CategoryFragment.getInstance(CategoryFragment.IOS))
        .add(CategoryFragment.FRONT, CategoryFragment.getInstance(CategoryFragment.FRONT))
        .into(viewPager)
    viewPager.offscreenPageLimit = tabAdapter.count

    tabLayout.setupWithViewPager(viewPager)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.menu_search) {
      Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
    } else if (item.itemId == R.id.menu_collection) {
      CollectionsActivity.navigateTo(this)
    }
    return super.onOptionsItemSelected(item)
  }
}
