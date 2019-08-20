package com.lenabru.networkapp.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry


/* 
 * Created by Lena Brusilovski on 2019-08-20
 */

class LifeCycleOwnerImpl : LifecycleOwner {

    private val lifecycle = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = lifecycle

    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun clear() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}