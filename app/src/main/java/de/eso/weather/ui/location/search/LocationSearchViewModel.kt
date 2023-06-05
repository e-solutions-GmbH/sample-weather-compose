package de.eso.weather.ui.location.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.livedatacommand.LiveDataEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class LocationSearchViewModel(private val locationService: LocationService) : ViewModel() {

    val queryInput = mutableStateOf("")
    val locations = mutableStateOf(emptyList<Location>())
    val finishWithResult = mutableStateOf<LiveDataEvent<Location>?>(null)

    private val disposables = CompositeDisposable()

    init {
        triggerLocationSearch("")
    }

    fun onQueryChanged(query: String) {
        queryInput.value = query
        triggerLocationSearch(query)
    }

    fun onLocationSelected(location: Location) {
        finishWithResult.value = LiveDataEvent(location)
    }

    private fun triggerLocationSearch(query: String) {
        locationService.queryLocations(query).subscribe { foundLocations ->
            locations.value = foundLocations
        }.addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
