package de.eso.weather.ui

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import de.eso.weather.domain.alert.api.WeatherAlertService
import de.eso.weather.domain.alert.platform.AlertProvider
import de.eso.weather.domain.alert.platform.RandomBooleanSupplier
import de.eso.weather.domain.alert.platform.WeatherAlertProvider
import de.eso.weather.domain.alert.service.AlertReceiver
import de.eso.weather.domain.alert.service.WeatherAlertServiceImpl
import de.eso.weather.domain.forecast.api.WeatherForecastService
import de.eso.weather.domain.forecast.platform.WeatherForecastProvider
import de.eso.weather.domain.forecast.service.ForecastProvider
import de.eso.weather.domain.forecast.service.WeatherForecastServiceImpl
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.location.platform.FavoriteLocationsRepositoryImpl
import de.eso.weather.domain.location.platform.LocationsProvider
import de.eso.weather.domain.location.service.LocationServiceImpl
import de.eso.weather.domain.location.service.LocationsReceiver
import de.eso.weather.ui.alert.AlertViewModel
import de.eso.weather.ui.forecast.ForecastViewModel
import de.eso.weather.ui.location.favorites.FavoriteLocationsViewModel
import de.eso.weather.ui.location.search.LocationSearchViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        ForecastViewModel(
            weatherForecastService = get(),
            favoriteLocationsRepository = get()
        )
    }

    viewModel { (id: String) ->
        AlertViewModel(
            weatherAlertService = get(),
            locationService = get(),
            locationId = id
        )
    }

    viewModel {
        FavoriteLocationsViewModel(
            favoriteLocationsRepository = get()
        )
    }

    viewModel { LocationSearchViewModel(locationService = get(), favoriteLocationsRepository = get()) }
}

val domainModule = module {
    // Forecast
    single<WeatherForecastService> { WeatherForecastServiceImpl(get()) }
    single<ForecastProvider> { WeatherForecastProvider() }

    // Alert
    single<WeatherAlertService> { WeatherAlertServiceImpl() } bind AlertReceiver::class
    single<AlertProvider> { WeatherAlertProvider(get(), MainScope()) }

    single { RandomBooleanSupplier() }

    // Location
    single<LocationService> { LocationServiceImpl() } bind LocationsReceiver::class
    single<RxDataStore<Preferences>> { RxPreferenceDataStoreBuilder(get(), "locations").build() }
    single<FavoriteLocationsRepository> { FavoriteLocationsRepositoryImpl(get()) }

    single(createdAtStart = true) { LocationsProvider(get()) }
}
