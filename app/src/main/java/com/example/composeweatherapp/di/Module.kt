package com.example.composeweatherapp.di

import com.example.composeweatherapp.data.remote.RemoteDataSource
import com.example.composeweatherapp.data.remote.RetrofitDataSource
import com.example.composeweatherapp.data.remote.WeatherService
import com.example.composeweatherapp.domain.repository.Repository
import com.example.composeweatherapp.domain.usecases.SearchCityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideRetrofitDataSource(weatherService: WeatherService): RemoteDataSource = RetrofitDataSource(weatherService)

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
        .addConverterFactory(gsonConverterFactory)
        .baseUrl("https://api.openweathermap.org/")
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)
}