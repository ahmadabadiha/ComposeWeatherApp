package com.example.composeweatherapp.domain.usecases

import com.example.composeweatherapp.data.model.CitiesItem
import com.example.composeweatherapp.data.remote.ResultWrapper
import com.example.composeweatherapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchCityUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(query: String): Flow<ResultWrapper<List<CitiesItem>>> {
        return repository.searchCities(query)
    }
}