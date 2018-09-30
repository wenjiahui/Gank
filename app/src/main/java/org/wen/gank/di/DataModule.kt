package org.wen.gank.di

import android.arch.persistence.room.Room
import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import org.wen.gank.BuildConfig
import org.wen.gank.api.GankApi
import org.wen.gank.tools.AppDatabase
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

/**
 * created by Jiahui.wen 2017-07-23
 */
@Module
class DataModule {

  @Singleton
  @Provides
  internal fun provideAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "gank").build()
  }


  @Singleton
  @Provides
  internal fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.tag("http").d(message) }).setLevel(if (BuildConfig.DEBUG) BODY else NONE)).build()
  }

  @Singleton
  @Provides
  internal fun provideMoshi(): Moshi {
    return Moshi.Builder().build()
  }

  @Singleton
  @Provides
  internal fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi)).client(okHttpClient).baseUrl("http://gank.io")
        .build()
  }

  @Singleton
  @Provides
  internal fun provideGankApi(retrofit: Retrofit): GankApi {
    return retrofit.create(GankApi::class.java)
  }
}
