package de.eso.weather.domain

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

/**
 * Simulates that a Koin instance is created in a library.
 */
object DomainKoin {
    private var _instance: KoinApplication? = null
    val instance: KoinApplication
        get() = checkNotNull(_instance)

    fun init(modules: Module) {
        _instance = koinApplication {
            modules(modules)
        }
    }
}