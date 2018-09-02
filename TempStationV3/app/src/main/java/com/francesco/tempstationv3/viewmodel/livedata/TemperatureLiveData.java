package com.francesco.tempstationv3.viewmodel.livedata;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;

import java.io.IOException;

public class TemperatureLiveData extends MyLiveData<Float> {
    private static final String TAG = TemperatureLiveData.class.getName();

    private final SensorManager sensorManager;
    private Bmx280SensorDriver bmx280SensorDriver;

    private final SensorEventListener mySensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            setValue(event.values[0]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d(TAG, "onAccuracyChanged: " + accuracy);
        }
    };

    private final SensorManager.DynamicSensorCallback myDynamicSensorCallback = new SensorManager.DynamicSensorCallback() {
        @Override
        public void onDynamicSensorConnected(Sensor sensor) {
            if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE)
                sensorManager.registerListener(mySensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    };

    public TemperatureLiveData(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive");

        try {
            this.bmx280SensorDriver = RainbowHat.createSensorDriver();
        }
        catch (IOException e) {
            Log.d(TAG, "TemperatureLiveData: " + e);
            return;
        }

        sensorManager.registerDynamicSensorCallback(myDynamicSensorCallback);
        bmx280SensorDriver.registerTemperatureSensor();
    }

    @Override
    protected void onInactive() {
        bmx280SensorDriver.unregisterTemperatureSensor();
        sensorManager.unregisterListener(mySensorEventListener);
        sensorManager.unregisterDynamicSensorCallback(myDynamicSensorCallback);

        try {
            bmx280SensorDriver.close();
        }
        catch (IOException e) {
            Log.d(TAG, "onInactive: " + e);
        }

        Log.d(TAG, "onInactive");
        super.onInactive();
    }
}
