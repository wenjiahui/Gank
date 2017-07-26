package org.wen.gank.di;

import dagger.Component;
import javax.inject.Singleton;
import org.wen.gank.CategoryFragment;

/**
 * created by Jiahui.wen 2017-07-23
 */
@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    void inject(CategoryFragment target);
}
