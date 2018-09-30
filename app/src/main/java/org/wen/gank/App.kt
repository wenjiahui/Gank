package org.wen.gank

import android.app.Application
import android.content.Context
import org.wen.gank.di.AppComponent
import org.wen.gank.di.AppModule
import org.wen.gank.di.DaggerAppComponent
import org.wen.gank.di.DataModule
import timber.log.Timber

/**
 * created by Jiahui.wen 2017-07-23
 */
class App : Application() {

  var appComponent: AppComponent? = null
    private set

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    initDi()
  }

  private fun initDi() {
    appComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .dataModule(DataModule())
        .build()
  }

  companion object {
    fun from(context: Context): App {
      return context.applicationContext as App
    }
  }
}
