package de.eso.weather.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.eso.weather.R
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
import de.eso.weather.ui.routing.api.screenNameFor
import de.eso.weather.ui.shared.compose.ColorPalette
import de.eso.weather.ui.shared.compose.ColorPalettes
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.LocalScreenSize
import de.eso.weather.ui.shared.compose.ScreenSize
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.GridBackground
import de.eso.weather.ui.themeselection.ThemeSelectionScreen
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

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun WeatherApp() {
        val navController = rememberNavController()

        var showBackButton by remember { mutableStateOf(false) }
        var currentRoute: String? by remember { mutableStateOf(null) }
        val currentScreenName = screenNameFor(currentRoute)
        var activeColorPalette by remember { mutableStateOf(ColorPalettes.DarkBlue) }
        var activeDimensionScale by remember { mutableStateOf(1.0f) }


        DisposableEffect(true) {
            val destinationChangedListener =
                NavController.OnDestinationChangedListener { controller, destination, _ ->
                    showBackButton = controller.previousBackStackEntry != null
                    currentRoute = destination.route
                }

            navController.addOnDestinationChangedListener(destinationChangedListener)

            onDispose {
                navController.removeOnDestinationChangedListener(destinationChangedListener)
            }
        }

        WeatherTheme(
            isLargeScreen = LocalConfiguration.current.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE),
            colorPalette = activeColorPalette,
            dimensionScale = activeDimensionScale
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                GridBackground(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                Pair(0f, EsoColors.Violet.copy(alpha = 0f)),
                                Pair(0.85f, EsoColors.Violet.copy(alpha = 0f)),
                                Pair(1f, EsoColors.Violet.copy(alpha = 0.3f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                ) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = WeatherTheme.colorPalette.colorScheme.primary
                        ),
                        title = {
                            Headline(screenName = currentScreenName)
                        },
                        navigationIcon = {
                            if (showBackButton) {
                                Box(
                                    modifier = Modifier.fillMaxHeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(
                                        onClick = { navController.popBackStack() }
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(WeatherTheme.dimensions.iconSizeButton),
                                            imageVector = Icons.Default.ArrowBack,
                                            tint = WeatherTheme.colorPalette.iconTint,
                                            contentDescription = "backIcon"
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(height = WeatherTheme.dimensions.titleBarHeight)
                    )

                    val isLargeScreen = WeatherTheme.isLargeScreen()
                    val constraints = ConstraintSet {
                        val navigationRef = createRefFor("navigation")
                        val contentRef = createRefFor("content")

                        if (isLargeScreen) {
                            constrain(navigationRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            }
                            constrain(contentRef) {
                                top.linkTo(parent.top)
                                start.linkTo(navigationRef.end)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                            }
                        } else {
                            constrain(contentRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                        }
                    }

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize(),
                        constraintSet = constraints
                    ) {
                        if (isLargeScreen) {
                            EsoNavigationRail(
                                modifier = Modifier.layoutId("navigation"),
                                navController = navController
                            )
                        }

                        WeatherNavHost(
                            modifier = Modifier
                                .layoutId("content")
                                .padding(8.dp)
                                .windowInsetsPadding(WindowInsets.safeContent),
                            navController = navController,
                            onColorPaletteSelected = {
                                activeColorPalette = it
                            },
                            onSizeSelected = {
                                activeDimensionScale = it
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun EsoNavigationRail(
        modifier: Modifier = Modifier,
        navController: NavHostController
    ) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()

        NavigationRail(
            modifier = modifier
                .width(104.dp),
            containerColor = WeatherTheme.colorPalette.colorScheme.secondaryContainer
        ) {
            EsoNavigationRailItem(
                label = {
                    Text(
                        stringResource(R.string.forecast_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_forecast_sun),
                        contentDescription = stringResource(R.string.forecast_title),
                    )
                },
                selected = currentBackStackEntry?.destination?.route == Routes.FORECAST
            ) {
                navController.popBackStack(
                    Routes.FORECAST,
                    inclusive = false
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            EsoNavigationRailItem(
                label = {
                    Text(
                        stringResource(R.string.favorite_locations_title_short),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.Filled.LocationCity,
                        contentDescription = stringResource(R.string.favorite_locations_title_short)
                    )
                },
                selected = currentBackStackEntry?.destination?.route == Routes.MANAGE_LOCATIONS
                        || currentBackStackEntry?.destination?.route == Routes.LOCATION_SEARCH,
            ) {
                if (!navController.popBackStack(Routes.MANAGE_LOCATIONS, inclusive = false)) {
                    navController.navigate(Routes.MANAGE_LOCATIONS)
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            EsoNavigationRailItem(
                label = {
                    Text(
                        stringResource(R.string.theme_selection_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.Filled.Brush,
                        contentDescription = stringResource(R.string.favorite_locations_title_short)
                    )
                },
                selected = currentBackStackEntry?.destination?.route == Routes.THEME_SELECTION
            ) {
                if (!navController.popBackStack(Routes.THEME_SELECTION, inclusive = false)) {
                    navController.navigate(Routes.THEME_SELECTION)
                }
            }
        }
    }

    @Composable
    fun EsoNavigationRailItem(
        modifier: Modifier = Modifier,
        label: @Composable () -> Unit,
        icon: @Composable () -> Unit,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        val backgroundModifier = if (selected) {
            Modifier.background(WeatherTheme.colorPalette.colorScheme.secondary)
        } else {
            Modifier
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .selectable(true, onClick = onClick)
                .clip(RoundedCornerShape(4.dp))
                .then(backgroundModifier)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val inActiveAlpha = 0.4f
            val iconAlpha = if (selected) 1.0f else inActiveAlpha
            CompositionLocalProvider(LocalContentColor provides WeatherTheme.colorPalette.colorScheme.onBackground.copy(alpha = iconAlpha)) {
                Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                    icon()
                }
            }

            Spacer(Modifier.padding(vertical = 4.dp))

            val textStyle = if (selected) {
                LocalTextStyle.current.copy(
                    color = WeatherTheme.colorPalette.colorScheme.onBackground,
                    fontSize = 18.sp
                )
            } else {
                LocalTextStyle.current.copy(
                    color = WeatherTheme.colorPalette.colorScheme.onBackground.copy(
                        alpha = inActiveAlpha
                    ),
                    fontSize = 18.sp
                )
            }
            ProvideTextStyle(textStyle) {
                label()
            }
        }
    }

    @Composable
    fun Headline(
        modifier: Modifier = Modifier,
        screenName: String? = null
    ) {
        Row(
            modifier = modifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = screenName ?: "Welcome Driver")
        }
    }

    @Composable
    fun WeatherNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController,
        onColorPaletteSelected: (ColorPalette) -> Unit,
        onSizeSelected: (Float) -> Unit
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.FORECAST,
            modifier = modifier
        ) {
            composable(Routes.FORECAST) {
                ForecastScreen(navController, forecastViewModel)
            }
            composable(Routes.ALERT) { navBackStackEntry ->
                val locationId =
                    requireNotNull(navBackStackEntry.arguments?.getString(ALERT_LOCATION_ID))
                val alertViewModel: AlertViewModel = getViewModel { parametersOf(locationId) }
                AlertScreen(alertViewModel)
            }
            composable(Routes.MANAGE_LOCATIONS) {
                FavoriteLocationsScreen(
                    viewModel = favoriteLocationsViewModel,
                    navController
                )
            }
            composable(Routes.LOCATION_SEARCH) {
                LocationSearchScreen(
                    navController,
                    locationSearchViewModel
                )
            }
            composable(Routes.THEME_SELECTION) {
                ThemeSelectionScreen(
                    onColorPaletteSelected = onColorPaletteSelected,
                    onSizeSelected = onSizeSelected
                )
            }
        }
    }

    @Preview
    @Composable
    fun WeatherAppPreview() {
        WeatherApp()
    }

    @Preview(device = Devices.AUTOMOTIVE_1024p)
    @Composable
    fun WeatherAppPreviewAutomotive() {
        CompositionLocalProvider(LocalScreenSize provides ScreenSize(isLargeScreen = true)) {
            WeatherApp()
        }
    }

    @Composable
    @Preview
    fun NavigationRailItemPreview(@PreviewParameter(BooleanPreviewParameterProvider::class) selected: Boolean) {
        WeatherTheme {
            Column {
                EsoNavigationRailItem(
                    label = { Text("Item") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_forecast_sun),
                            contentDescription = stringResource(R.string.forecast_title),
                        )
                    },
                    selected = selected
                ) { }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return false
    }
}

class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(false, true)
}
