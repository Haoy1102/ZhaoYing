package com.example.zhaoying_v13.ui.home.cameraX

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class CustomLifecycle : LifecycleOwner {
    private var mLifecycleRegistry: LifecycleRegistry? = null
    fun doOnResume() {
        mLifecycleRegistry!!.currentState = Lifecycle.State.RESUMED
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry!!
    }

    init {
        mLifecycleRegistry = LifecycleRegistry(this)
        //markState已经弃用了
        mLifecycleRegistry!!.currentState = Lifecycle.State.CREATED
    }
}