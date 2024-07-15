package de.eso.weather.ui.location.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.livedatacommand.LiveDataCommand
import de.eso.weather.ui.shared.livedatacommand.LiveDataEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.launch

class LocationSearchViewModel(
    private val locationService: LocationService,
    private val favoriteLocationsRepository: FavoriteLocationsRepository
) : ViewModel() {
    val queryInput = mutableStateOf("")
    val locations = mutableStateOf(emptyList<Location>())
    val finishScreen = mutableStateOf<LiveDataCommand?>(null)

    init {
        triggerLocationSearch("")
    }

    fun onQueryChanged(query: String) {
        queryInput.value = query
        triggerLocationSearch(query)
    }

    fun onLocationSelected(location: Location) {
        favoriteLocationsRepository.saveLocation(location)
        favoriteLocationsRepository.setActive(location)
        finishScreen.value = LiveDataCommand()
    }

    private fun triggerLocationSearch(query: String) {
        viewModelScope.launch {
            locationService.queryLocations(query).collect { foundLocations ->
                locations.value = foundLocations
            }
        }
    }
}
