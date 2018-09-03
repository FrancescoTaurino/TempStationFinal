package com.francesco.tempstationv3.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.francesco.tempstationv3.viewmodel.livedata.IsStartedLiveData;
import com.francesco.tempstationv3.viewmodel.livedata.TemperatureLiveData;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.google.android.things.pio.Gpio;

import java.io.IOException;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = MainActivityViewModel.class.getName();

    private final IsStartedLiveData isStartedLiveData;
    private final TemperatureLiveData temperatureLiveData;

    private Gpio ledGreen;
    private Gpio ledRed;
    private Gpio ledBlue;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        this.isStartedLiveData = new IsStartedLiveData();
        this.temperatureLiveData = new TemperatureLiveData((SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE));

        try {
            this.ledGreen = RainbowHat.openLedGreen();
            this.ledRed = RainbowHat.openLedRed();
            this.ledBlue = RainbowHat.openLedBlue();

            this.ledGreen.setValue(false);
            this.ledRed.setValue(false);
            this.ledBlue.setValue(false);
        }
        catch (IOException e) {
            this.ledGreen = null;
            this.ledRed = null;
            this.ledBlue = null;

            Log.d(TAG, "MainActivityViewModel: " + e);
        }
    }

    public LiveData<Boolean> getIsStartedLiveData() {
        return isStartedLiveData;
    }

    public LiveData<Float> getTemperatureLiveData() {
        return temperatureLiveData;
    }

    public void lightLed(boolean isStarted) {
        if (ledGreen != null) {
            try {
                ledGreen.setValue(isStarted);
            }
            catch (IOException e) {
                Log.d(TAG, "lightLed: " + e);
            }
        }

        if (ledRed != null) {
            try {
                ledRed.setValue(!isStarted);
            }
            catch (IOException e) {
                Log.d(TAG, "lightLed: " + e);
            }
        }
    }

    public void lightBlueLed() {
        if (ledBlue != null) {
            try {
                ledBlue.setValue(true);
            }
            catch (IOException e) {
                Log.d(TAG, "lightBlueLed: " + e);
            }
        }
    }

    @Override
    protected void onCleared() {
        closeLeds();

        super.onCleared();
    }

    private void closeLeds() {
        if (ledGreen != null) {
            try {
                ledGreen.setValue(false);
                ledGreen.close();
            }
            catch (IOException e) {
                Log.d(TAG, "closeLeds: " + e);
            }
        }

        if (ledRed != null) {
            try {
                ledRed.setValue(false);
                ledRed.close();
            }
            catch (IOException e) {
                Log.d(TAG, "closeLeds: " + e);
            }
        }

        if (ledBlue != null) {
            try {
                ledBlue.setValue(false);
                ledBlue.close();
            }
            catch (IOException e) {
                Log.d(TAG, "closeLeds: " + e);
            }
        }
    }
}
