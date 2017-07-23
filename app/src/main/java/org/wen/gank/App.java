package org.wen.gank;

import android.app.Application;
import android.content.Context;
import org.wen.gank.di.AppComponent;
import org.wen.gank.di.AppModule;
import org.wen.gank.di.DaggerAppComponent;
import org.wen.gank.di.DataModule;
import timber.log.Timber;

/**
 * created by Jiahui.wen 2017-07-23
 */
public class App extends Application {

    public static App from(Context context) {
        return (App) context.getApplicationContext();
    }

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        initDi();
    }

    private void initDi() {
        appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .dataModule(new DataModule())
            .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
