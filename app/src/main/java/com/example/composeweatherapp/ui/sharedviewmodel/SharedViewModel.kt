package com.example.composeweatherapp.ui.sharedviewmodel

import androidx.lifecycle.ViewModel
import com.example.composeweatherapp.data.model.Daily
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    lateinit var forecast: Daily
}