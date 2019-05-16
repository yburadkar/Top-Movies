package com.yb.uadnd.popularmovies;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource {

    private AtomicBoolean isIdle= new AtomicBoolean(true);
    private ResourceCallback mCallback;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        isIdle.set(isIdleNow);
        if(mCallback != null){
            if(isIdle.get()) mCallback.onTransitionToIdle();
        }
    }
}
