package de.eso.weather.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import de.eso.weather.ui.location.search.LocationSearchScreen
import de.eso.weather.ui.location.search.LocationSearchViewModel
import de.eso.weather.ui.routing.api.Routes
import de.eso.weather.ui.routing.api.Routes.ALERT_LOCATION_ID
import de.eso.weather.ui.shared.compose.ColorPalette
import de.eso.weather.ui.shared.compose.ColorPalettes
import de.eso.weather.ui.shared.compose.Dimensions
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.GridBackground
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherActivity : AppCompatActivity() {

    private val forecastViewModel: ForecastViewModel by viewModel()
    private val favoriteLocationsViewModel: FavoriteLocationsViewModel by viewModel()
    // TODO Move into screen after updating Koin and adding koin-androidx-compose
    private val locationSearchViewModel: LocationSearchViewModel by viewModel()

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
        var showColorPickerDialog by remember { mutableStateOf(false) }

        var activeColorPalette by remember { mutableStateOf(ColorPalettes.DarkBlue) }

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            showBackButton = controller.previousBackStackEntry != null

            screenName = when(destination.route) {
                Routes.ALERT -> "Alerts"
                Routes.MANAGE_LOCATIONS -> "Favorites"
                Routes.LOCATION_SEARCH -> "Available Locations"
                else -> null
            }
        }

        WeatherTheme(
            isLargeScreen = LocalConfiguration.current.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE),
            colorPalette = activeColorPalette
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = WeatherTheme.colors.primary,
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
                        actions = {
                            Button(
                                onClick = { showColorPickerDialog = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Brush,
                                    tint = EsoColors.Orange,
                                    contentDescription = "Select color palette"
                                )
                            }
                        },
                        modifier = Modifier.height(height = Dimensions.TitleBarHeight)
                    )
                }
            ) {
                GridBackground(
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            Pair(0f, EsoColors.Violet.copy(alpha = 0f)),
                            Pair(0.85f, EsoColors.Violet.copy(alpha = 0f)),
                            Pair(1f, EsoColors.Violet.copy(alpha = 0.3f))
                        )
                    )
                )
                NavHost(
                    navController = navController,
                    modifier = Modifier.padding(Dimensions.ContentPadding)
                )
            }

            if (showColorPickerDialog) {
                ColorPickerDialog(
                    onDialogDismissed = { showColorPickerDialog = false },
                    colorPalettes = ColorPalettes.all,
                    selectedColorPalette = activeColorPalette
                ) { selectedColorPalette ->
                    activeColorPalette = selectedColorPalette
                }
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
            composable(Routes.FORECAST) {
                ForecastScreen(navController, forecastViewModel)
            }
            composable(Routes.ALERT) { navBackStackEntry ->
                val locationId = requireNotNull(navBackStackEntry.arguments?.getString(ALERT_LOCATION_ID))
                val alertViewModel: AlertViewModel = getViewModel { parametersOf(locationId) }
                AlertScreen(alertViewModel)
            }
            composable(Routes.MANAGE_LOCATIONS) { navBackStackEntry ->
                FavoriteLocationsScreen(
                    viewModel = favoriteLocationsViewModel,
                    navController,
                    navBackStackEntry
                )
            }
            composable(Routes.LOCATION_SEARCH) {
                LocationSearchScreen(
                    navController,
                    locationSearchViewModel
                )
            }
        }
    }

    @Composable
    fun ColorPickerDialog(
        onDialogDismissed: () -> Unit,
        colorPalettes: List<ColorPalette>,
        selectedColorPalette: ColorPalette,
        onColorPaletteSelected: (ColorPalette) -> Unit
    ) {
        Dialog(
            onDismissRequest = onDialogDismissed,
            properties = DialogProperties()
        ) {
            Column(
                modifier = Modifier
                    .background(color = WeatherTheme.colors.secondary)
                    .padding(all = Dimensions.ContainerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = Dimensions.TitleBarHeight)
                ) {
                    Text(
                        text = "Choose color palette",
                        style = WeatherTheme.typography.h6
                    )
                }

                LazyColumn {
                    items(items = colorPalettes, key = { it.name }) { colorPalette ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Dimensions.ContainerPadding / 2)
                                .clickable {
                                    onColorPaletteSelected(colorPalette)
                                    onDialogDismissed()
                                }
                        ) {
                            RadioButton(
                                selected = selectedColorPalette == colorPalette,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = WeatherTheme.colors.onSecondary
                                ),
                                modifier = Modifier
                                    .padding(end = Dimensions.ContentPadding)
                            )

                            Text(
                                text = colorPalette.name,
                                style = WeatherTheme.typography.body2,
                                color = WeatherTheme.colors.onSecondary
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return false
    }
}
