package de.eso.weather.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.eso.weather.ui.alert.AlertScreen
import de.eso.weather.ui.alert.AlertViewModel
import de.eso.weather.ui.forecast.ForecastScreen
import de.eso.weather.ui.forecast.ForecastViewModel
import de.eso.weather.ui.location.favorites.FavoriteLocationsScreen
import de.eso.weather.ui.location.favorites.FavoriteLocationsViewModel
import de.eso.weather.ui.routing.api.Routes
import de.eso.weather.ui.routing.api.Routes.ALERT_LOCATION_ID
import de.eso.weather.ui.shared.compose.Dimensions
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.location.LocationResult
import de.eso.weather.ui.shared.location.LocationSearchApi
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherActivity : AppCompatActivity() {

    private val forecastViewModel: ForecastViewModel by viewModel()
    private val favoriteLocationsViewModel: FavoriteLocationsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }

    @Composable
    fun WeatherApp() {
        val navController = rememberNavController()

        var showBackButton by remember { mutableStateOf(false) }
        var screenName: String? by remember { mutableStateOf(null) }

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            showBackButton = controller.previousBackStackEntry != null

            screenName = when(destination.route) {
                Routes.ALERT -> "Alerts"
                Routes.MANAGE_LOCATIONS -> "Manage Locations"
                else -> null
            }
        }

        WeatherTheme(LocalConfiguration.current.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = EsoColors.DarkBlue,
                        title = { Headline(screenName) },
                        navigationIcon = if (showBackButton) {
                            {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        tint = EsoColors.Orange,
                                        contentDescription = "backIcon"
                                    )
                                }
                            }
                        } else {
                            null
                        },
                        modifier = Modifier.height(height = Dimensions.TitleBarHeight)
                    )
                }
            ) {
                NavHost(
                    navController = navController,
                    modifier = Modifier.padding(Dimensions.ContentPadding)
                )
            }
        }
    }

    @Composable
    fun Headline(screenName: String? = null) {
        Text(text = screenName ?: "Welcome Driver")
    }

    @Composable
    fun NavHost(navController: NavHostController, modifier: Modifier = Modifier) {
        NavHost(navController = navController, startDestination = Routes.FORECAST, modifier = modifier) {
            composable(Routes.FORECAST) { ForecastScreen(navController, forecastViewModel) }
            composable(Routes.ALERT) { navBackStackEntry ->
                val locationId = requireNotNull(navBackStackEntry.arguments?.getString(ALERT_LOCATION_ID))
                val alertViewModel: AlertViewModel = getViewModel { parametersOf(locationId) }
                AlertScreen(alertViewModel)
            }
            composable(Routes.MANAGE_LOCATIONS) {
                FavoriteLocationsScreen(
                    viewModel = favoriteLocationsViewModel,
                    onShowLocationActivity = ::onShowLocationActivity,
                    onPopBackStack = { navController.popBackStack() })
            }
        }
    }

    private fun onShowLocationActivity() =
        startActivityForResult(
            LocationSearchApi.getLocationActivityIntent(this), REQUEST_CODE_CHOOSE_LOCATION
        )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_CHOOSE_LOCATION -> handleChooseLocationResult(resultCode, data)
        }
    }

    private fun handleChooseLocationResult(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_CANCELED) {
            return
        }

        data?.getParcelableExtra<LocationResult>(LocationSearchApi.RESULT_KEY_LOCATION)?.let {
            favoriteLocationsViewModel.onLocationSearchReturned(it.id)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return false
    }

    private companion object {
        private const val REQUEST_CODE_CHOOSE_LOCATION = 1
    }
}
