package com.example.composeweatherapp.ui.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.composeweatherapp.LoadingLottieView
import com.example.composeweatherapp.R
import com.example.composeweatherapp.data.model.CitiesItem
import com.example.composeweatherapp.data.remote.ResultWrapper
import com.example.composeweatherapp.ui.theme.LightBlue
import com.example.composeweatherapp.ui.theme.Purple700
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = viewModel(),
    onNavigateToPrimaryWeather: (Float, Float, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var searchTextState by remember { mutableStateOf(TextFieldValue("")) }
        var showLoadingAnimState by rememberSaveable { mutableStateOf(false) }
        var resultListState: List<CitiesItem>? by rememberSaveable { mutableStateOf(null) }
        var errorTextState: String? by rememberSaveable { mutableStateOf("") }
        SearchView(
            searchText = searchTextState,
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color(0xFFE7F1F1), RoundedCornerShape(16.dp))
        ) {
            searchTextState = it
        }

        if (showLoadingAnimState) {
            LoadingLottieView()
        }
        if (errorTextState != "") {
            Toast.makeText(LocalContext.current, errorTextState, Toast.LENGTH_LONG).show()
        }
        if (resultListState != null) {
            ResultList(cities = resultListState!!, onNavigateToPrimaryWeather)
        }
        if (searchTextState.text == "") {
            WelcomeLottieView()
        } else {
            LaunchedEffect(key1 = searchTextState.text) {
                delay(1200)
                searchViewModel.searchCities(searchTextState.text)
            }
            LaunchedEffect(key1 = true) {
                searchViewModel.searchResults.collectLatest {
                    when (it) {
                        ResultWrapper.Loading -> {
                            showLoadingAnimState = true
                            resultListState = null
                            errorTextState = ""
                        }
                        is ResultWrapper.Success -> {
                            showLoadingAnimState = false
                            errorTextState = ""
                            resultListState = it.value
                        }
                        is ResultWrapper.Error -> {
                            showLoadingAnimState = false
                            errorTextState = it.message
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchView(
    searchText: TextFieldValue,
    modifier: Modifier,
    onSearch: (TextFieldValue) -> Unit
) {
    TextField(
        value = searchText,
        onValueChange = { onSearch(it) },
        textStyle = TextStyle(fontSize = 17.sp),
        leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color.Gray) },
        modifier = modifier,
        placeholder = { Text(text = "Search places") },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = Color.DarkGray
        )
    )
}

@Composable
fun WelcomeLottieView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(104.dp))
        val animationSpec by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.location_weather_radar))
        LottieAnimation(
            animationSpec,
            modifier = Modifier.size(300.dp)
        )
        Text(text = "Search any place in the world", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Purple700)
    }
}


@Composable
fun ResultList(cities: List<CitiesItem>, onClick: (Float, Float, String) -> Unit) {
    LazyColumn {
        items(cities) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .border(width = 4.dp, color = Color.White, RoundedCornerShape(48.dp))
                    .background(Brush.horizontalGradient(colors = listOf(LightBlue, Color.White)), RoundedCornerShape(48.dp))
                    .clickable { onClick(it.lat.toFloat(), it.lon.toFloat(), it.name) }
            ) {
                Column(modifier = Modifier.padding(12.dp).offset(x = 4.dp), horizontalAlignment = Alignment.Start) {
                    Text(text = it.name, fontSize = 24.sp, color = Color.White)
                    Text(text = it.state + ", " + it.country, fontSize = 24.sp, color = Color.White)
                }
            }

        }
    }
}