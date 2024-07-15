package de.eso.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.assertj.core.api.Assertions.assertThat

fun <T> LiveData<T>.test(): LiveDataTestObserver<T> {
    val testObserver = LiveDataTestObserver<T>()
    observeForever(testObserver)
    return testObserver
}

class LiveDataTestObserver<T> : Observer<T> {

    private val _values: MutableList<T> = mutableListOf()

    /**
     * Returns a list of received onChanged values. This is _not_ a shared list in contrast to
     * RxJavas TestObserver. You will get a (shallow) copy.
     */
    val values: List<T>
        get() = _values.toList()

    override fun onChanged(value: T) {
        _values.add(value)
    }

    /** Assert that this LiveDataTestObserver has not received any values. */
    fun assertNoValues() = apply { assertValueCount(0) }

    /** Assert that this LiveDataTestObserver received the specified number of values. */
    fun assertValueCount(count: Int) = apply {
        if (values.size != count) {
            throw fail("Value counts differ; expected " + count + " but was " + values.size)
        }
    }

    /**
     * Assert that this LiveDataTestObserver received exactly one value which is equal to the given
     * value.
     */
    fun assertValue(value: T) = apply {
        when {
            values.size != 1 -> throw fail("Expected only $value but was $values")
            values[0] != value -> throw fail("Expected $value but was ${values[0]}")
        }
    }

    /**
     * Asserts that this LiveDataTestObserver received exactly one value for which the provided
     * predicate returns true.
     */
    fun assertValue(predicate: (T) -> Boolean) = apply {
        assertValueAt(0, predicate)
        when {
            (values.size > 1) -> throw fail("Value present but other values as well")
        }
    }

    /**
     * Asserts that this LiveDataTestObserver received a value at the given index which is equal to
     * the given value.
     */
    fun assertValueAt(index: Int, value: T) = apply {
        when {
            values.isEmpty() -> throw fail("Expected $value but no values present")
            index >= values.size ->
                throw fail("Invalid index $index: only ${values.size} values received")
            values[index] != value ->
                throw fail("Expected $value at index $index but was ${values[index]}")
        }
    }

    /**
     * Asserts that this LiveDataTestObserver received a value at the given index for which the
     * provided predicate returns true.
     */
    fun assertValueAt(index: Int, predicate: (T) -> Boolean) = apply {
        when {
            values.isEmpty() -> throw fail("No values present")
            index >= values.size ->
                throw fail("Invalid index $index: only ${values.size} values received")
            !predicate(values[index]) -> throw fail("Expected value not present at index $index")
        }
    }

    /**
     * Assert that the LiveDataTestObserver received only the specified values in the specified
     * order.
     */
    fun assertValues(vararg values: T) = apply {
        if (values.size != _values.size) {
            throw fail(
                "Value counts differ; expected: " + values.size + " but was: " + _values.size
            )
        }
        try {
            assertThat(_values).containsExactly(*values)
        } catch (e: AssertionError) {
            throw AssertionError("Expected values differ from received values:\n${e.message}")
        }
    }

    private fun fail(message: String): AssertionError {
        val whitespaces = " ".repeat((AssertionError::class.java.canonicalName?.length ?: 0) + 1)
        val contentString = "$whitespaces Received ${values.size} value(s): $values"
        return AssertionError("$message\n$contentString")
    }
}
