package com.example.composeweatherapp.ui.weatherdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.composeweatherapp.R
import com.example.composeweatherapp.convertToTempString
import com.example.composeweatherapp.data.model.Daily
import com.example.composeweatherapp.textModifierWithPadding4
import com.example.composeweatherapp.textModifierWithPadding8
import com.example.composeweatherapp.ui.sharedviewmodel.SharedViewModel
import com.example.composeweatherapp.ui.theme.LightBlue
import com.example.composeweatherapp.ui.theme.LighterBlue

@Composable
fun WeatherDetailsScreen(lat: Float, lon: Float, cityName: String, sharedViewModel: SharedViewModel) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState), horizontalAlignment = CenterHorizontally
    ) {
        PageTitleWithCoordinates(lat, lon, cityName)
        WeatherDetailsView(dailyWeather = sharedViewModel.forecast)
    }
}

@Composable
fun PageTitleWithCoordinates(lat: Float, lon: Float, cityName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(), horizontalAlignment = CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.ic_round_location_city_24), null, tint = Color.Black)
            Text(modifier = textModifierWithPadding4, text = cityName, fontSize = 24.sp, fontWeight = Bold, color = Color.Black)
        }
        Text(
            modifier = textModifierWithPadding4.align(CenterHorizontally),
            text = "latitude: $lat, longitude: $lon",
            fontSize = 16.sp,
            fontWeight = Bold
        )
    }
}

@Composable
fun WeatherDetailsView(dailyWeather: Daily) {

    Column(
        Modifier
            .padding(16.dp)
            .background(Brush.verticalGradient(listOf(LightBlue, Color.White)), RoundedCornerShape(24.dp))
    ) {
        val date = java.text.SimpleDateFormat("EEEE dd MMMM yyyy")
        val receivedDate = java.util.Date(dailyWeather.dt.toLong() * 1000)
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(CenterHorizontally),
            text = date.format(receivedDate),
            fontWeight = Bold,
            fontSize = 20.sp
        )
        Image(
            painter = rememberAsyncImagePainter("http://openweathermap.org/img/wn/${dailyWeather.weather[0].icon}@2x.png"),
            contentDescription = null,
            modifier = Modifier
                .size(192.dp)
                .padding(8.dp)
                .align(CenterHorizontally)
        )
        Text(
            modifier = textModifierWithPadding8.align(CenterHorizontally),
            text = convertToTempString(dailyWeather.temp.min) + " / " + convertToTempString(dailyWeather.temp.max),
            fontSize = 32.sp,
            fontWeight = Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = "Morning",
                    fontSize = 20.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = convertToTempString(dailyWeather.temp.morn),
                    fontSize = 24.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = "Feels like\n" + convertToTempString(dailyWeather.feels_like.morn),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = Bold,
                    color = Color.Black
                )
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = "Day",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = Bold,
                    color = Color.Black
                )
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = convertToTempString(dailyWeather.temp.day),
                    fontSize = 24.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = "Feels like\n" + convertToTempString(dailyWeather.feels_like.day),
                    fontSize = 16.sp,
                    fontWeight = Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = "Evening",
                    fontSize = 20.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = convertToTempString(dailyWeather.temp.eve),
                    fontSize = 24.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = "Feels like\n" + convertToTempString(dailyWeather.feels_like.eve),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = Bold,
                    color = Color.Black
                )
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = "Night",
                    fontSize = 20.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = convertToTempString(dailyWeather.temp.night),
                    fontSize = 24.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
                Text(
                    modifier = textModifierWithPadding8.align(CenterHorizontally),
                    text = "Feels like\n" + convertToTempString(dailyWeather.feels_like.night),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
            }
        }
        RiseSetView(dailyWeather)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(horizontalAlignment = CenterHorizontally) {
                Text("Humidity", fontSize = 20.sp, fontWeight = Bold)
                Text(text = dailyWeather.humidity.toString() + "%", fontSize = 32.sp, fontWeight = Bold)
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text("Wind(m/s)", fontSize = 20.sp, fontWeight = Bold)
                Text(text = dailyWeather.wind_speed.toString(), fontSize = 32.sp, fontWeight = Bold)
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text("UV index", textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = Bold)
                Text(text = dailyWeather.uvi.toString(), textAlign = TextAlign.Center, fontSize = 32.sp, fontWeight = Bold)
            }
        }
    }
}

@Composable
fun RiseSetView(dailyWeather: Daily) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(LighterBlue, RoundedCornerShape(32.dp))
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_outline_wb_sunny_24),
                contentDescription = null,
                Modifier
                    .size(40.dp)
                    .align(CenterVertically)
            )
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.SpaceAround) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = "Sunrise: " + convertToStringTime(dailyWeather.sunrise),
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = "Sunset: " + convertToStringTime(dailyWeather.sunset),
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
            }
        }
        Row(modifier = Modifier.padding(4.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_outline_nights_stay_24), contentDescription = null,
                Modifier
                    .size(40.dp)
                    .align(CenterVertically)
            )
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = "Moonrise: " + convertToStringTime(dailyWeather.moonrise),
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = "Moonset: " + convertToStringTime(dailyWeather.moonset),
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
            }
        }
    }
}

private fun convertToStringTime(unixTime: Int): String {
    val timeFormat = java.text.SimpleDateFormat("HH:MM")
    val time = java.util.Date(unixTime.toLong() * 1000)
    return timeFormat.format(time)
}
