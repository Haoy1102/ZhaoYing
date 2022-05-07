package com.example.zhaoying_v13.ui.home.cameraX;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class CustomLifecycle implements LifecycleOwner {
    private LifecycleRegistry mLifecycleRegistry = null;

    public CustomLifecycle() {
        mLifecycleRegistry = new LifecycleRegistry(this);
        //markState已经弃用了
        mLifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
    }
    public void doOnResume(){
        mLifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
    }
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
