package com.example.composeweatherapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.data.model.CitiesItem
import com.example.composeweatherapp.data.remote.ResultWrapper
import com.example.composeweatherapp.domain.usecases.SearchCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchCityUseCase: SearchCityUseCase) : ViewModel() {

    private val _searchResults = MutableStateFlow<ResultWrapper<List<CitiesItem>>>(ResultWrapper.Loading)
    val searchResults = _searchResults.asStateFlow()

    fun searchCities(query: String) = viewModelScope.launch(Dispatchers.IO) {
        searchCityUseCase(query).collectLatest {
            _searchResults.emit(it)
        }
    }
}