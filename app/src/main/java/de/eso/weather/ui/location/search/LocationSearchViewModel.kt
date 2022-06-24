package de.eso.weather.ui.location.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.eso.weather.ui.shared.livedatacommand.LiveDataEvent
import de.eso.weather.ui.shared.livedatacommand.send
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class LocationSearchViewModel(private val locationService: LocationService) : ViewModel() {

    val locationsResult = MutableLiveData<List<Location>>()
    val finishWithResult = MutableLiveData<LiveDataEvent<Location>>()

    private val disposables = CompositeDisposable()

    init {
        triggerLocationSearch("")
    }

    fun onTextSubmit(query: String) {
        triggerLocationSearch(query)
    }

    fun onLocationSelected(location: Location) {
        finishWithResult.send(location)
    }

    private fun triggerLocationSearch(query: String) {
        locationService.queryLocations(query).subscribe { locations ->
            locationsResult.value = locations
        }.addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
