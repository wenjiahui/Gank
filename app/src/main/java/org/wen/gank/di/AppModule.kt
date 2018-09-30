package org.wen.gank.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.wen.gank.App

/**
 * created by Jiahui.wen 2017-07-23
 */
@Module(includes = [DataModule::class])
class AppModule(private val app: App) {

  @Provides
  fun provideApplicationContext(): Context {
    return app
  }
}
