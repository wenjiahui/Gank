package org.wen.gank.di;

import com.squareup.moshi.Moshi;
import dagger.Module;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.wen.gank.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * created by Jiahui.wen 2017-07-23
 */
@Module
public class DataModule {

    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().addNetworkInterceptor(
            new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Timber.d(message);
                }
            }).setLevel(BuildConfig.DEBUG ? BODY : NONE)).build();
    }

    @Singleton
    Moshi provideMoshi() {
        return new Moshi.Builder().build();
    }

    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Moshi moshi) {
        return new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl("http://gank.io/api")
            .build();
    }
}
