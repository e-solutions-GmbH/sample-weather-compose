package de.eso.weather.domain.location.platform

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.rxjava3.RxDataStore
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Optional

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteLocationsRepositoryImpl(
    private val preferenceStore: RxDataStore<Preferences>
) : FavoriteLocationsRepository {

    private val LOCATIONS_KEY = stringPreferencesKey("locations")
    private val ACTIVE_LOCATION = stringPreferencesKey("activeLocation")

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val locationAdapter: JsonAdapter<Location> =
        moshi.adapter(Location::class.java)

    private val locationListAdapter: JsonAdapter<List<Location>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, Location::class.java))

    private val savedLocationsSubject = BehaviorSubject.createDefault<List<Location>>(emptyList())
    private val activeLocationSubject = BehaviorSubject.createDefault(OptionalLocation())

    init {
        preferenceStore
            .data()
            .map { preferences ->
                val serializedLocations: String? = preferences[LOCATIONS_KEY]
                if (serializedLocations == null) {
                    emptyList()
                } else {
                    locationListAdapter.fromJson(serializedLocations) ?: emptyList()
                }
            }.subscribe {
                savedLocationsSubject.onNext(it)
            }

        preferenceStore
            .data()
            .map { preferences ->
                val activeLocation: String? = preferences[ACTIVE_LOCATION]
                if (activeLocation == null) {
                    OptionalLocation()
                } else {
                    locationAdapter
                        .fromJson(activeLocation)
                        .let { OptionalLocation(it) }
                }
            }.subscribe {
                activeLocationSubject.onNext(it)
            }
    }

    override val savedLocations: Observable<List<Location>> = savedLocationsSubject.hide()

    override val activeLocation: Observable<Optional<Location>> =
        activeLocationSubject
            .map { optionalLocation -> Optional.ofNullable(optionalLocation.value) }
            .hide()

    override fun saveLocation(location: Location) {
        preferenceStore
            .updateDataAsync { preferences: Preferences ->
                val currentLocations: List<Location> = savedLocationsSubject.value

                if (location !in currentLocations) {
                    val updatedLocations: List<Location> = currentLocations.plus(location)

                    Single.just(
                        preferences
                            .toMutablePreferences()
                            .apply {
                                this[LOCATIONS_KEY] = locationListAdapter.toJson(updatedLocations)
                            }
                    )
                } else {
                    Single.just(preferences)
                }
            }
            .subscribe()
    }

    override fun deleteLocation(location: Location) {
        preferenceStore
            .updateDataAsync { preferences ->
                val currentLocations: List<Location> = savedLocationsSubject.value
                if (location in currentLocations) {
                    val updatedLocations: List<Location> = currentLocations.minus(location)

                    Single.just(
                        preferences
                            .toMutablePreferences()
                            .apply {
                                this[LOCATIONS_KEY] = locationListAdapter.toJson(updatedLocations)
                            }
                    )
                } else {
                    Single.just(preferences)
                }
            }
            .subscribe()
    }

    override fun setActive(location: Location) {
        preferenceStore
            .updateDataAsync { preferences ->
                Single.just(
                    preferences
                        .toMutablePreferences()
                        .apply {
                            this[ACTIVE_LOCATION] = locationAdapter.toJson(location)
                        }
                )
            }
            .subscribe()
    }

    /**
     * Needed because MoshiObjectRxDataStore can only serialize data classes
     */
    private data class OptionalLocation(val value: Location? = null)
}
