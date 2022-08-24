package com.example.composeweatherapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composeweatherapp.ui.primaryweather.PrimaryWeatherScreen
import com.example.composeweatherapp.ui.primaryweather.PrimaryWeatherViewModel
import com.example.composeweatherapp.ui.search.SearchScreen
import com.example.composeweatherapp.ui.search.SearchViewModel
import com.example.composeweatherapp.ui.sharedviewmodel.SharedViewModel
import com.example.composeweatherapp.ui.theme.ComposeWeatherAppTheme
import com.example.composeweatherapp.ui.theme.Pink
import com.example.composeweatherapp.ui.weatherdetails.WeatherDetailsScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(color = Pink)
            ComposeWeatherAppTheme {
                ComposeWeatherAppNavHost()
            }
        }
    }
}


@Composable
fun ComposeWeatherAppNavHost(
    modifier: Modifier = Modifier
        .background(color = MaterialTheme.colors.background)
        .fillMaxSize(),
    navController: NavHostController = rememberNavController(),
    startDestination: String = "search"
) {
    val sharedViewModel = hiltViewModel<SharedViewModel>()
    NavHost(modifier = modifier, navController = navController, startDestination = startDestination) {
        composable("search") {
            val viewModel = hiltViewModel<SearchViewModel>(it)
            SearchScreen(searchViewModel = viewModel) { lat, lon, cityName ->
                navController.navigate("primary/$lat/$lon/$cityName")
            }
        }
        composable(
            "primary/{lat}/{lon}/{city name}",
            arguments = listOf(navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType },
                navArgument("city name") { type = NavType.StringType })
        ) {
            val viewModel = hiltViewModel<PrimaryWeatherViewModel>(it)
            PrimaryWeatherScreen(primaryWeatherViewModel = viewModel, sharedViewModel = sharedViewModel) { lat, lon, cityName ->
                navController.navigate("details/$lat/$lon/$cityName")
            }
        }
        composable(
            "details/{lat}/{lon}/{city name}",
            arguments = listOf(navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType },
                navArgument("city name") { type = NavType.StringType })
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            Log.d("ahmad", "ComposeWeatherAppNavHost: " + args.toString() + " " + args.getString("lat").toString())
            WeatherDetailsScreen(args.getFloat("lat"), args.getFloat("lon"), args.getString("city name")!!, sharedViewModel)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Pink)
    ComposeWeatherAppTheme {
        ComposeWeatherAppNavHost()
    }
}