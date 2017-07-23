package org.wen.gank.di;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import org.wen.gank.App;

/**
 * created by Jiahui.wen 2017-07-23
 */
@Module(includes = { DataModule.class })
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    public Context provideApplicationContext() {
        return app;
    }
}
