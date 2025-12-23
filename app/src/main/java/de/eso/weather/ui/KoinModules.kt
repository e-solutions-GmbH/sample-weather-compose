package de.eso.weather.ui

import de.eso.weather.domain.util.getLogger
import de.eso.weather.domain.DomainKoin
import de.eso.weather.domain.util.DummyLogger
import de.eso.weather.domain.util.Logger
import de.eso.weather.ui.alert.AlertViewModel
import de.eso.weather.ui.forecast.ForecastViewModel
import de.eso.weather.ui.location.favorites.FavoriteLocationsViewModel
import de.eso.weather.ui.location.search.LocationSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    val domainKoinInstance = DomainKoin.instance.koin

    single<Logger> { DummyLogger() }

    viewModel {
        ForecastViewModel(
            weatherForecastService = domainKoinInstance.get(),
            favoriteLocationsRepository = domainKoinInstance.get(),
            // Koin-IDE-Plugin: Missing navigation icon for extension functions.
            logger2 = getLogger(),
            logger = get()
        )
    }

    viewModel { (id: String) ->
        AlertViewModel(
            weatherAlertService = domainKoinInstance.get(),
            locationService = domainKoinInstance.get(),
            locationId = id
        )
    }

    viewModel {
        FavoriteLocationsViewModel(
            favoriteLocationsRepository = domainKoinInstance.get()
        )
    }

    viewModel { LocationSearchViewModel(locationService = domainKoinInstance.get(), favoriteLocationsRepository = domainKoinInstance.get()) }
}