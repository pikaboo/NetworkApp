package com.lenabru.networkapp

import androidx.lifecycle.*
import com.lenabru.networkapp.viewmodel.LifeCycleOwnerImpl


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

class CountedObserver<T>(private val handler: (T, Int) -> Unit) : Observer<T>, LifecycleOwner {

    private val lifecycle = LifecycleRegistry(this)
    var times = 1
    private var count = 0

    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
        if (count < times) {
            count++
            handler(t, count)
        }
        if (count == times) {
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }
    }
}

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = CountedObserver { t: T, _: Int ->
        onChangeHandler(t)
    }
    observe(observer, observer)
}

/**
 * @param - times, the max times to have this observer invoked
 * the int in the onChangeHandler indicated the current iteration
 */
fun <T> LiveData<T>.observeTimes(times: Int, onChangeHandler: (T, Int) -> Unit) {
    val observer = CountedObserver(handler = onChangeHandler)
    observer.times = times
    observe(observer, observer)
}