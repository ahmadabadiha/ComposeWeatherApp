package com.example.composeweatherapp.domain.usecases

import com.example.composeweatherapp.data.model.CitiesItem
import com.example.composeweatherapp.data.remote.ResultWrapper
import com.example.composeweatherapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow


class SearchCityUseCase(private val repository: Repository) {
    suspend operator fun invoke(query: String): Flow<ResultWrapper<List<CitiesItem>>> {
        return repository.searchCities(query)
    }
}