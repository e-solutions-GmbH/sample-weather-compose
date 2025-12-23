package de.eso.weather.domain

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import de.eso.weather.domain.alert.api.WeatherAlertService
import de.eso.weather.domain.alert.platform.AlertProvider
import de.eso.weather.domain.alert.platform.RandomBooleanSupplier
import de.eso.weather.domain.alert.platform.WeatherAlertConnector
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
import de.eso.weather.domain.util.DummyLogger
import de.eso.weather.domain.util.Logger
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.koinApplication
import org.koin.dsl.module

fun domainModule(context: Context) = module {
    single<Logger> { DummyLogger() }
    // Forecast
    single<WeatherForecastService> { WeatherForecastServiceImpl(Schedulers.single(), get()) }
    single<ForecastProvider> { WeatherForecastProvider() }

    // Alert
    single<WeatherAlertService> { WeatherAlertServiceImpl() } binds arrayOf(AlertReceiver::class)
    single<AlertProvider> { WeatherAlertProvider(Schedulers.single(), get()) }
    single(createdAtStart = true) { WeatherAlertConnector(get(), get()) }

    single { RandomBooleanSupplier() }

    someOtherDomainModule(context)

    single(createdAtStart = true) {
        LocationsProvider(
            get(),
            get()
        )
    }
}

// Koin-IDE-Plugin: No navigation due to extension function?
fun Module.someOtherDomainModule(context: Context) {
    single<LocationService> { LocationServiceImpl() } binds arrayOf(LocationsReceiver::class)
    single<RxDataStore<Preferences>> { RxPreferenceDataStoreBuilder(context, "locations").build() }
    single<FavoriteLocationsRepository> { FavoriteLocationsRepositoryImpl(get()) }
}