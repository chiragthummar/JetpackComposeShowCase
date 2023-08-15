package com.cb.myshowcase.di

import android.app.Activity
import android.app.Application
import com.cb.myshowcase.data.remote.ImageApi
import com.cb.myshowcase.data.remote.InfiniteImageApi
import com.cb.myshowcase.data.remote.InfiniteImageApi.Companion.INFINITE_IMAGE_BASE_URL
import com.cb.myshowcase.presentation.MainActivity
import com.cb.myshowcase.common.Prefs
import com.cb.myshowcase.data.remote.InfiniteImageApi.Companion.IMAGE_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideInfiniteImageApi(): InfiniteImageApi {
        return Retrofit.Builder()
            .baseUrl(INFINITE_IMAGE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            )
            .build()
            .create(InfiniteImageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideImageApi(): ImageApi {
        return Retrofit.Builder()
            .baseUrl(IMAGE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            )
            .build()
            .create(ImageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideActivity(): Activity {
        return MainActivity()
    }

    @Provides
    @Singleton
    fun providePrefs(app: Application): Prefs {
        return Prefs(app)
    }


}