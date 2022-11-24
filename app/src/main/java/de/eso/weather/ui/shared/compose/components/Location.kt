package de.eso.weather.ui.shared.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location

@Composable
fun LocationCard(
    locationName: String,
    weather: WeatherTO?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = locationName, modifier = Modifier.fillMaxWidth())

        weather?.let {
            Text(text = it.weather, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview
@Composable
fun LocationCardPreview() {
    val previewLocation = Location(name = "Erlangen")
    val previewWeather = WeatherTO(location = previewLocation, weather = "Rainy")

    LocationCard(locationName = previewLocation.name, weather = previewWeather)
}
