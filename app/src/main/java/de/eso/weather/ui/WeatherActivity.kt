package de.eso.weather.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import de.eso.weather.ui.alert.AlertScreen
import de.eso.weather.ui.alert.AlertViewModel
import de.eso.weather.ui.forecast.ForecastScreen
import de.eso.weather.ui.forecast.ForecastViewModel
import de.eso.weather.ui.location.favorites.FavoriteLocationsScreen
import de.eso.weather.ui.location.favorites.FavoriteLocationsViewModel
import de.eso.weather.ui.routing.api.Routes
import de.eso.weather.ui.routing.api.Routes.ALERT_LOCATION_ID
import de.eso.weather.ui.shared.compose.Dimensions
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
            Column(modifier = Modifier.padding(Dimensions.PADDING_MEDIUM)) {
                Headline()
                NavHost()
            }
        }
    }

    @Composable
    fun Headline() {
        Text(text = "Welcome Driver", modifier = Modifier.padding(bottom = Dimensions.PADDING_MEDIUM))
    }

    @Composable
    fun NavHost() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.FORECAST) {
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

        setupNavigationUi(navController)
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

    private fun setupNavigationUi(navController: NavController) {
        setupActionBarWithNavController(navController, AppBarConfiguration(navController.graph))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return false
    }

    private companion object {
        private const val REQUEST_CODE_CHOOSE_LOCATION = 1
    }
}
