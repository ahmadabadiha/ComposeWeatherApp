package com.example.composeweatherapp.data.model

data class CitiesItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String?
)