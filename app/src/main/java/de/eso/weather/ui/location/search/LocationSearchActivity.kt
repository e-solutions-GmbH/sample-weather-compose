package de.eso.weather.ui.location.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import de.eso.weather.databinding.ActivityLocationBinding
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.GridBackground
import de.eso.weather.ui.shared.livedatacommand.LiveDataEventObserver
import de.eso.weather.ui.shared.location.LocationResult
import de.eso.weather.ui.shared.location.LocationSearchApi.RESULT_KEY_LOCATION
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
class LocationSearchActivity : AppCompatActivity() {

    private val locationSearchViewModel: LocationSearchViewModel by viewModel()

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)

        val viewBinding = ActivityLocationBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        viewBinding.locationBackground.setContent {
            WeatherTheme {
                GridBackground(
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            Pair(0f, EsoColors.Violet.copy(alpha = 0f)),
                            Pair(0.85f, EsoColors.Violet.copy(alpha = 0f)),
                            Pair(1f, EsoColors.Violet.copy(alpha = 0.3f))
                        )
                    )
                )
            }
        }

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
