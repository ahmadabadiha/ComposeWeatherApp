package com.example.composeweatherapp.data.remote

import com.example.composeweatherapp.data.model.CitiesItem
import com.example.composeweatherapp.data.model.WeatherResponse
import retrofit2.Response


interface RemoteDataSource {
    suspend fun searchCity(query: String): Response<List<CitiesItem>>
    suspend fun getWeatherInfo(lat: Float, lon: Float): Response<WeatherResponse>
}