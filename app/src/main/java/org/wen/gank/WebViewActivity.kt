package org.wen.gank

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.wen.gank.extensions.isVisible
import org.wen.gank.extensions.safeStartActivity
import org.wen.gank.extensions.setup

class WebViewActivity : AppCompatActivity() {

  private var url: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_web_view)
    url = intent.getStringExtra("url")
    if (url == null) finish()

    setSupportActionBar(toolbar)
    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      title = intent.getStringExtra("title")
    }

    webView.setup()
    webView.webViewClient = object : WebViewClient() {
      override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        progressBar.progress = 0
        progressBar.isVisible = true
      }

      override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        progressBar.isVisible = false
      }
    }

    webView.webChromeClient = object : WebChromeClient() {
      override fun onProgressChanged(view: WebView, progress: Int) {
        if (progressBar.progress != progress) progressBar.progress = progress
      }
    }

    webView.loadUrl(url)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_webview, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> finish()
      R.id.menu_share -> {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, url)
        sendIntent.type = "text/plain"
        safeStartActivity(Intent.createChooser(sendIntent, resources.getText(R.string.action_share_to)))
      }
      R.id.menu_open_with_browser -> {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        safeStartActivity(intent)
      }
    }

    return super.onOptionsItemSelected(item)
  }

  companion object {
    fun go(context: Context, url: String, title: String) {
      val intent = Intent(context, WebViewActivity::class.java)
      intent.putExtra("url", url)
      intent.putExtra("title", title)
      context.safeStartActivity(intent)
    }
  }
}
