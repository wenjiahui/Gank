package org.wen.gank.di

import dagger.Component
import org.wen.gank.CategoryFragment
import javax.inject.Singleton

/**
 * created by Jiahui.wen 2017-07-23
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

  fun inject(target: CategoryFragment)
}
