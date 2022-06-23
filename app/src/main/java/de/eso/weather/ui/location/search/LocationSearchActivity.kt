package de.eso.weather.ui.location.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.eso.weather.ui.shared.livedatacommand.LiveDataEventObserver
import de.eso.weather.R
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.location.LocationResult
import de.eso.weather.ui.shared.location.LocationSearchApi.RESULT_KEY_LOCATION
import org.koin.android.ext.android.inject
import org.koin.androidx.fragment.android.setupKoinFragmentFactory

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinExperimentalAPI

/**
 * This is a showcase how to use more than one Activity in an app.
 * That may be useful e.g. if you want to start this Activity by result from outside the weather app.
 * There might be other usecases, too, e.g. if you need to extend a third-party Activity from some library.
 *
 * However, always try to stick to the Single-Activity-Pattern if possible, because that's generally less complex and avoids some overhead.
 */
class LocationSearchActivity : AppCompatActivity(R.layout.activity_location) {

    private val locationSearchViewModel: LocationSearchViewModel by viewModel()

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)

        locationSearchViewModel.finishWithResult.observe(this, LiveDataEventObserver(::onFinishWithResult))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return false
    }

    private fun onFinishWithResult(location: Location) {
        val intent = Intent().apply {
            putExtra(RESULT_KEY_LOCATION, LocationResult(location.id, location.name))
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}
