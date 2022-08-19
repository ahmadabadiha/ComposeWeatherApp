package com.example.composeweatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeweatherapp.ui.search.SearchScreen
import com.example.composeweatherapp.ui.search.SearchViewModel
import com.example.composeweatherapp.ui.theme.ComposeWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWeatherAppTheme {
               ComposeWeatherAppNavHost()
            }
        }
    }
}


@Composable
fun ComposeWeatherAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "search"
) {
    NavHost(modifier = modifier, navController = navController, startDestination = startDestination) {
        composable("search") {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(onNavigateToPrimaryWeather = { navController.navigate("primary") }, viewModel)
        }
        composable("primary"){

        }
        composable("details"){

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeWeatherAppTheme {
        val viewModel = hiltViewModel<SearchViewModel>()
        SearchScreen (onNavigateToPrimaryWeather = {}, searchViewModel = viewModel)

    }
}