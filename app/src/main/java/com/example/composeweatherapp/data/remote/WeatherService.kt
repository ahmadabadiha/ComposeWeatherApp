package com.example.composeweatherapp.data.remote

import android.provider.UserDictionary.Words.APP_ID
import com.example.composeweatherapp.data.model.CitiesItem
import com.example.composeweatherapp.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("http://api.openweathermap.org/geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("appid") appId: String = API_KEY
    ): Response<List<CitiesItem>>

    @GET("data/2.5/onecall")
    suspend fun getWeatherInfo(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") unit: String = "metric",
        @Query("exclude") exclude: String = "minutely,hourly,alerts",
        @Query("appid") appId: String = API_KEY
    ): Response<WeatherResponse>
}