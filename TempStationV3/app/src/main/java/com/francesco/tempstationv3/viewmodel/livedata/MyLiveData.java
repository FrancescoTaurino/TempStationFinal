package com.francesco.tempstationv3.viewmodel.livedata;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyLiveData<T> extends LiveData<T> {
    private static final String TAG = MyLiveData.class.getName();
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<T> observer) {
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (mPending.compareAndSet(true, false))
                    observer.onChanged(t);
            }
        });
    }

    @Override
    protected void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }
}
