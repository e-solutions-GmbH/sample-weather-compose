package de.eso.weather

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentFactory
import de.eso.weather.ui.domainModule
import de.eso.weather.ui.uiModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.androidx.fragment.android.KoinFragmentFactory
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

open class WeatherApplication : Application() {

    companion object {

        /**
         * Initialize DMB and global Koin instance
         *
         * Do this in a separate method, since it might be necessary to call this method before onCreate().
         */
        internal fun setupDmbAndKoin(context: Context) {
            startKoin {
                logger(AndroidLogger(Level.ERROR))
                androidContext(context)
                modules(
                    domainModule
                )
            }.createEagerInstances()

            Log.i("Weather", "setupDMBandKoin($context) finished")
        }
    }

    override fun onCreate() {
        super.onCreate()
        initUIKoin()
    }

    private fun initUIKoin() {
        loadKoinModules(
            listOf(
                module { single<FragmentFactory> { KoinFragmentFactory() } },
                uiModule
            )
        )
    }
}
