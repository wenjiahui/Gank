package org.wen.gank

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import org.wen.gank.api.GankApi
import org.wen.gank.tools.AppDatabase
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

/**
 * created by Jiahui.wen 2017-07-23
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    initDi()
  }

  private fun initDi() {

    val modules = module {
      single { this@App }
      single {
        Room.databaseBuilder(this@App, AppDatabase::class.java, "gank").build()
      }
      single {
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
              Timber.tag("http").d(message)
            }).setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
            .build()
      }
      single {
        Moshi.Builder().build()
      }
      single {
        Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .baseUrl("http://gank.io")
            .build()
      }
      single {
        (get() as Retrofit).create(GankApi::class.java)
      }
    }
    startKoin(this, listOf(modules))
  }

  companion object {
    fun from(context: Context): App {
      return context.applicationContext as App
    }
  }
}
