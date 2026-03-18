package ru.sergeyabadzhev.weatherappkmp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.sergeyabadzhev.weatherappkmp.features.home.HomeScreen
import ru.sergeyabadzhev.weatherappkmp.features.home.HomeViewModel
import ru.sergeyabadzhev.weatherappkmp.features.search.SearchScreen
import ru.sergeyabadzhev.weatherappkmp.features.search.SearchViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = koinViewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onSearchClick = { navController.navigate(Screen.Search.route) }
            )
        }
        composable(Screen.Search.route) {
            val searchViewModel: SearchViewModel = koinViewModel()
            SearchScreen(
                viewModel = searchViewModel,
                onCitySelected = { city ->
                    homeViewModel.loadWeatherForCity(city)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
