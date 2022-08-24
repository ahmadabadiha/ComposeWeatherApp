package com.example.composeweatherapp.ui.primaryweather

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.composeweatherapp.LoadingLottieView
import com.example.composeweatherapp.R
import com.example.composeweatherapp.convertToTempString
import com.example.composeweatherapp.data.model.Current
import com.example.composeweatherapp.data.model.Daily
import com.example.composeweatherapp.data.model.WeatherResponse
import com.example.composeweatherapp.data.remote.ResultWrapper
import com.example.composeweatherapp.textModifierWithPadding4
import com.example.composeweatherapp.ui.sharedviewmodel.SharedViewModel
import com.example.composeweatherapp.ui.theme.LightBlue
import com.example.composeweatherapp.ui.theme.LighterBlue
import com.example.composeweatherapp.ui.theme.Purple200
import kotlinx.coroutines.flow.collectLatest


@Composable
fun PrimaryWeatherScreen(
    primaryWeatherViewModel: PrimaryWeatherViewModel,
    sharedViewModel: SharedViewModel,
    navigateToWeatherDetails: (Float, Float, String) -> Unit
) {
    var showLoadingAnimState by remember { mutableStateOf(false) }
    var weatherState: WeatherResponse? by remember { mutableStateOf(null) }
    var errorTextState by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = true) {
        primaryWeatherViewModel.weatherResults.collectLatest {
            when (it) {
                ResultWrapper.Loading -> {
                    showLoadingAnimState = true
                    errorTextState = ""
                }
                is ResultWrapper.Success -> {
                    showLoadingAnimState = false
                    errorTextState = ""
                    weatherState = it.value
                }
                is ResultWrapper.Error -> {
                    showLoadingAnimState = false
                    errorTextState = it.message.toString()
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PageTitle(cityName = primaryWeatherViewModel.cityName)
        if (showLoadingAnimState) {
            LoadingLottieView()
        }
        if (weatherState != null) {
            CurrentWeather(currentWeather = weatherState!!.current) {
                sharedViewModel.forecast = weatherState!!.daily[0]
                navigateToWeatherDetails(primaryWeatherViewModel.lat, primaryWeatherViewModel.lon, primaryWeatherViewModel.cityName)
            }
            Text(
                modifier = textModifierWithPadding4.align(Alignment.Start),
                text = "Weekly forecast",
                textAlign = TextAlign.Left,
                fontWeight = Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
            WeeklyForecast(dailyForecasts = weatherState!!.daily) {
                sharedViewModel.forecast = weatherState!!.daily[it]
                navigateToWeatherDetails(primaryWeatherViewModel.lat, primaryWeatherViewModel.lon, primaryWeatherViewModel.cityName)
            }
        }
        if (errorTextState != "") {
            Toast.makeText(LocalContext.current, errorTextState, Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun PageTitle(cityName: String) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = CenterVertically) {
        Icon(painter = painterResource(id = R.drawable.ic_round_location_city_24), null, tint = Color.Black)
        Text(modifier = textModifierWithPadding4, text = cityName, fontSize = 24.sp, fontWeight = Bold, color = Color.Black)
    }
}

@Composable
fun CurrentWeather(currentWeather: Current, onNavigateToWeatherDetails: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                Brush.linearGradient(
                    listOf(Purple200, Color.Blue), start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ), RoundedCornerShape(24.dp)
            )
            .clickable { onNavigateToWeatherDetails() }, horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(modifier = textModifierWithPadding4, text = "Current Weather", fontSize = 24.sp, fontWeight = Bold, color = Color.White)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter("http://openweathermap.org/img/wn/${currentWeather.weather[0].icon}@2x.png"),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )

                val date = java.text.SimpleDateFormat("dd MMMM yyyy")
                val time = java.text.SimpleDateFormat("HH:mm")
                val weekDay = java.text.SimpleDateFormat("EEEE")
                val receivedDate = java.util.Date(currentWeather.dt.toLong() * 1000)
                Text(
                    modifier = textModifierWithPadding4,
                    text = currentWeather.weather[0].description,
                    fontSize = 20.sp,
                    color = Color.White, fontWeight = Bold
                )
                Text(modifier = textModifierWithPadding4, text = weekDay.format(receivedDate), fontSize = 20.sp, color = Color.White)
                Text(modifier = textModifierWithPadding4, text = date.format(receivedDate), fontSize = 16.sp, color = Color.White)
                Text(modifier = textModifierWithPadding4, text = time.format(receivedDate), fontSize = 16.sp, color = Color.White)

            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = textModifierWithPadding4,
                    text = convertToTempString(currentWeather.temp),
                    fontSize = 68.sp,
                    color = Color.White, fontWeight = Bold
                )
                Text(
                    modifier = textModifierWithPadding4,
                    text = "Feels like " + convertToTempString(currentWeather.feels_like),
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun WeeklyForecast(dailyForecasts: List<Daily>, onNavigateToWeatherDetails: (Int) -> Unit) {
    LazyRow {
        itemsIndexed(dailyForecasts) { index, item ->
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Brush.linearGradient(listOf(LightBlue, LighterBlue)), RoundedCornerShape(48.dp))
                    .border(width = 2.dp, shape = RoundedCornerShape(48.dp), color = Color.Blue)
                    .clickable { onNavigateToWeatherDetails(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val dayOfMonth = java.text.SimpleDateFormat("dd MMM")
                val dayOfWeek = java.text.SimpleDateFormat("E")
                val receivedDate = java.util.Date(item.dt.toLong() * 1000)
                Text(modifier = textModifierWithPadding4, text = dayOfWeek.format(receivedDate), fontSize = 32.sp, color = Color.Black)
                Text(modifier = textModifierWithPadding4, text = dayOfMonth.format(receivedDate), fontSize = 20.sp)
                Image(
                    painter = rememberAsyncImagePainter("http://openweathermap.org/img/wn/${dailyForecasts[index].weather[0].icon}@2x.png"),
                    contentDescription = null,
                    modifier = Modifier
                        .size(104.dp)
                        .padding(4.dp)
                )
                Text(
                    modifier = textModifierWithPadding4.offset(y = (-4).dp),
                    text = convertToTempString(item.temp.day),
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = Bold
                )
            }
        }
    }
}