package de.eso.weather.ui.shared.livedatacommand

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LiveDataCommand {
    var hasBeenHandled = false
}

/** Emits a new LiveDataCommand on the main thread synchronously */
fun MutableLiveData<LiveDataCommand>.send() {
    value = LiveDataCommand()
}

/** Emits a new LiveDataCommand on the main thread asynchronously using postValue() */
fun MutableLiveData<LiveDataCommand>.post() {
    postValue(LiveDataCommand())
}

/**
 * Subscribe a fragment by its view lifecycle to a [LiveDataCommand] using a
 * [LiveDataCommandObserver]
 */
fun LiveData<LiveDataCommand>.subscribeCommand(fragment: Fragment, observer: () -> Unit) {
    observe(fragment.viewLifecycleOwner, LiveDataCommandObserver { observer() })
}

/** Subscribe an activity to a [LiveDataCommand] using a [LiveDataCommandObserver] */
fun LiveData<LiveDataCommand>.subscribeCommand(activity: ComponentActivity, observer: () -> Unit) {
    observe(activity, LiveDataCommandObserver { observer() })
}
