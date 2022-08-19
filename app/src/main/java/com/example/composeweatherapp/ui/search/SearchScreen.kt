package com.example.composeweatherapp.ui.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.composeweatherapp.LoadingLottieView
import com.example.composeweatherapp.R
import com.example.composeweatherapp.data.model.CitiesItem
import com.example.composeweatherapp.data.remote.ResultWrapper
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    onNavigateToPrimaryWeather: () -> Unit,
    searchViewModel: SearchViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var searchText by remember { mutableStateOf(TextFieldValue("")) }
        var showLoadingAnim by rememberSaveable { mutableStateOf(false) }
        var resultList: List<CitiesItem>? by rememberSaveable { mutableStateOf(null) }
        var errorText: String? by rememberSaveable { mutableStateOf("") }
        SearchView(
            searchText = searchText,
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color(0xFFE7F1F1), RoundedCornerShape(16.dp))
        ) {
            searchText = it
        }

        if (showLoadingAnim) {
            LoadingLottieView()
        }
        if (errorText != "") {
            Toast.makeText(LocalContext.current, errorText, Toast.LENGTH_LONG).show()
        }
        if (resultList != null) {
            ResultList(cities = resultList!!, onNavigateToPrimaryWeather)
        }
        if (searchText.text == "") {
            WelcomeLottieView()
        } else {
            LaunchedEffect(key1 = searchText.text) {
                searchViewModel.searchCities(searchText.text)
                searchViewModel.searchResults.collectLatest {
                    when (it) {
                        ResultWrapper.Loading -> {
                            showLoadingAnim = true
                            resultList = null
                            errorText = ""
                        }
                        is ResultWrapper.Success -> {
                            showLoadingAnim = false
                            errorText = ""
                            resultList = it.value
                        }
                        is ResultWrapper.Error -> {
                            showLoadingAnim = false
                            errorText = it.message
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
    Spacer(modifier = Modifier.height(104.dp))
    val animationSpec by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.location_weather_radar))
    LottieAnimation(
        animationSpec,
        modifier = Modifier.size(300.dp)
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResultList(cities: List<CitiesItem>, onClick: () -> Unit) {
    LazyColumn {
        items(cities) {
            Surface(onClick = onClick) {


                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .border(width = 4.dp, color = Color.White, RoundedCornerShape(48.dp))
                        .background(Brush.horizontalGradient(colors = listOf(Color.Blue, Color.White)), RoundedCornerShape(48.dp))


                ) {
                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.Start) {
                        Text(text = it.name, fontSize = 24.sp, color = Color.White)
                        Text(text = it.state + ", " + it.country, fontSize = 24.sp, color = Color.White)
                    }
                }
            }
        }
    }
}