package de.eso.weather.ui.shared.livedatacommand

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LiveDataEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /** Returns the content if it has not been handled yet */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /** Returns the content, even if it's already been handled. */
    fun peekContent(): T = content
}

/** Emits a new LiveDataEvent on the main thread synchronously */
fun <T> MutableLiveData<LiveDataEvent<T>>.send(t: T) {
    value = LiveDataEvent(t)
}

/** Emits a new LiveDataEvent on the main thread asynchronously using postValue(). */
fun <T> MutableLiveData<LiveDataEvent<T>>.post(t: T) {
    postValue(LiveDataEvent(t))
}

/**
 * Subscribe a fragment by its view lifecycle to a [LiveDataEvent] using a [LiveDataEventObserver]
 */
fun <T> LiveData<LiveDataEvent<T>>.subscribeEvent(fragment: Fragment, observer: (T) -> Unit) {
    observe(fragment.viewLifecycleOwner, LiveDataEventObserver { observer(it) })
}

/** Subscribe an activity to a [LiveDataEvent] using a [LiveDataEventObserver] */
fun <T> LiveData<LiveDataEvent<T>>.subscribeEvent(
    activity: ComponentActivity,
    observer: (T) -> Unit
) {
    observe(activity, LiveDataEventObserver { observer(it) })
}
