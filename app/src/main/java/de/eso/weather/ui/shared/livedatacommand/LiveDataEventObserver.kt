package de.eso.weather.ui.shared.livedatacommand

import androidx.lifecycle.Observer

class LiveDataEventObserver<T>(private val action: (T) -> Unit) : Observer<LiveDataEvent<T>> {
    override fun onChanged(event: LiveDataEvent<T>?) {
        event?.getContentIfNotHandled()?.let { action(it) }
    }
}
