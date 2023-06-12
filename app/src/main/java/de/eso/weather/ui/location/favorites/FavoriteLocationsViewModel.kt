package de.eso.weather.ui.location.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.eso.weather.ui.shared.livedatacommand.LiveDataCommand
import de.eso.weather.ui.shared.livedatacommand.send
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class FavoriteLocationsViewModel(
    private val favoriteLocationsRepository: FavoriteLocationsRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    val locations = favoriteLocationsRepository.savedLocations
    val finishScreen = MutableLiveData<LiveDataCommand>()

    fun onLocationSelected(location: Location) {
        favoriteLocationsRepository.setActive(location)
        finishScreen.send()
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
