package org.wen.gank.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.squareup.moshi.Moshi;

import org.wen.gank.BuildConfig;
import org.wen.gank.api.GankApi;
import org.wen.gank.tools.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
    @Provides
    AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "gank").build();
    }


    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Timber.tag("http").d(message);
                    }
                }).setLevel(BuildConfig.DEBUG ? BODY : NONE)).build();
    }

    @Singleton
    @Provides
    Moshi provideMoshi() {
        return new Moshi.Builder().build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Moshi moshi) {
        return new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi)).client(okHttpClient).baseUrl("http://gank.io")
                .build();
    }

    @Singleton
    @Provides
    GankApi provideGankApi(Retrofit retrofit) {
        return retrofit.create(GankApi.class);
    }
}
