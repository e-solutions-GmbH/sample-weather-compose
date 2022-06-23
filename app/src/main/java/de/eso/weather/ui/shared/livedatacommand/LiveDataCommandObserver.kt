package de.eso.weather.ui.shared.livedatacommand

import androidx.lifecycle.Observer

class LiveDataCommandObserver(private val action: () -> Unit) : Observer<LiveDataCommand> {
    override fun onChanged(command: LiveDataCommand?) {
        command?.let {
            if (!it.hasBeenHandled) {
                it.hasBeenHandled = true
                action()
            }
        }
    }
}
