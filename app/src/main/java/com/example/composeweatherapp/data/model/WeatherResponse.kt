package com.example.composeweatherapp.data.model

data class WeatherResponse(
    val current: Current,
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)