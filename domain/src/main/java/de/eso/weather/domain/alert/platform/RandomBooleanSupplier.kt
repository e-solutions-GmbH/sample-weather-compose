package de.eso.weather.domain.alert.platform

import kotlin.random.Random

class RandomBooleanSupplier {
    fun next() = Random.nextInt(2) == 1
}
