package de.eso.weather.ui.location.favorites

import android.app.Activity.RESULT_OK
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.rxjava3.RxDataStore
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.forecast.service.ForecastProvider
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.domain.shared.platform.Locations
import de.eso.weather.ui.Intents.initIntents
import de.eso.weather.ui.Intents.releaseIntents
import de.eso.weather.ui.WeatherActivity
import de.eso.weather.ui.createBeforeAndroidComposeRule
import de.eso.weather.ui.forecast.WeatherScreenPage
import de.eso.weather.ui.location.search.LocationSearchActivity
import de.eso.weather.ui.shared.location.LocationResult
import de.eso.weather.ui.shared.location.LocationSearchApi.RESULT_KEY_LOCATION
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteLocationsScreenIntegrationTest : KoinTest {

    @get:Rule
    val composeTestRule = createBeforeAndroidComposeRule<WeatherActivity> {
        clearSharedPreferences()
        prepareLocations()
        prepareActiveLocation()
    }

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val weatherScreenPage = WeatherScreenPage(context, composeTestRule)
    private val favoriteLocationsPage = FavoriteLocationsPage(context, composeTestRule)

    private val rxDataStore: RxDataStore<Preferences> by inject()

    @Before
    fun setup() {
        initIntents()

        loadKoinModules(
            listOf(
                module(override = true) {
                    single<ForecastProvider> {
                        DummyForecastProvider()
                    }
                }
            )
        )
    }

    @After
    fun teardown() {
        releaseIntents()
    }

    @Test
    fun should_show_the_favorite_locations() {
        // GIVEN
        weatherScreenPage.clickManageLocationsButton()

        // THEN
        favoriteLocationsPage.locationIsVisible("Erlangen")
    }

    @Test
    fun should_navigate_to_the_location_search_screen_when_clicking_the_add_location_button() {
        // GIVEN
        stubLocationActivityIntent()
        weatherScreenPage.clickManageLocationsButton()

        // WHEN
        favoriteLocationsPage.clickAddLocationButton()

        // THEN
        intended(hasComponent(LocationSearchActivity::class.java.name))
    }

    @Test
    fun should_show_the_selected_location_after_searching_for_one() {
        // GIVEN
        stubLocationActivityIntent(Intent().apply {
            putExtra(RESULT_KEY_LOCATION, LocationResult(AMMERNDORF.id, AMMERNDORF.name))
        })

        weatherScreenPage.clickManageLocationsButton()

        // WHEN
        favoriteLocationsPage.clickAddLocationButton()

        // THEN
        favoriteLocationsPage.locationIsVisible(AMMERNDORF.name)
    }

    private fun stubLocationActivityIntent(result: Intent? = null) =
        intending(hasComponent(LocationSearchActivity::class.java.name)).respondWith(
            ActivityResult(
                RESULT_OK,
                result
            )
        )

    private fun clearSharedPreferences() {
        rxDataStore.updateDataAsync { preferences ->
            Single.just(preferences.toMutablePreferences().apply { clear() })
        }.blockingSubscribe()
    }

    private fun prepareLocations() {
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val locationListAdapter: JsonAdapter<List<Location>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, Location::class.java))

        rxDataStore.updateDataAsync { preferences ->
            Single.just(
                preferences.toMutablePreferences().apply {
                    this[stringPreferencesKey("locations")] = locationListAdapter.toJson(listOf(ERLANGEN))
                }
            )
        }.blockingSubscribe()
    }

    private fun prepareActiveLocation() {
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val locationAdapter: JsonAdapter<Location> = moshi.adapter(Location::class.java)

        rxDataStore.updateDataAsync { preferences ->
            Single.just(
                preferences.toMutablePreferences().apply {
                    this[stringPreferencesKey("activeLocation")] = locationAdapter.toJson(ERLANGEN)
                }
            )
        }.blockingSubscribe()
    }

    private class DummyForecastProvider : ForecastProvider {
        override fun getCurrentWeather(location: Location): Single<WeatherTO> =
            Single.just(WeatherTO(GOOD_WEATHER, ERLANGEN))
    }

    private companion object {
        private val AMMERNDORF = Locations.knownLocations.first { it.name == "Ammerndorf" }
        private val ERLANGEN = Locations.knownLocations.first { it.name == "Erlangen" }

        private const val GOOD_WEATHER = "Good Weather"
    }
}
