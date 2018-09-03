package com.francesco.tempstationv3.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.francesco.tempstationv3.database.MyFirebaseDatabase;
import com.francesco.tempstationv3.model.Measurement;
import com.francesco.tempstationv3.viewmodel.MainActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private MainActivityViewModel mainActivityViewModel;

    private long counter = 0;

    private final Observer<Float> temperatureLiveDataObserver = new Observer<Float>() {
        @Override
        public void onChanged(@Nullable Float value) {
            Log.d(TAG, "onChanged: " + value);

            if (value != null) {
                if (counter % 50 == 0)
                    MyFirebaseDatabase.pushMeasurement(new Measurement(value));

                counter++;
            }
        }
    };

    private final Observer<Boolean> isStartedLiveDataObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isStarted) {
            if (isStarted != null) {
                mainActivityViewModel.lightLed(isStarted);

                if (isStarted) {
                    Log.d(TAG, "onChanged: start temperature listening");

                    mainActivityViewModel.getTemperatureLiveData().observe(MainActivity.this, temperatureLiveDataObserver);
                }
                else {
                    Log.d(TAG, "onChanged: stop temperature listening");

                    mainActivityViewModel.getTemperatureLiveData().removeObservers(MainActivity.this);
                }
            }
        }
    };

    private final OnCompleteListener<AuthResult> myOnCompleteListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Log.d(TAG, "onComplete: login succeeded");

                mainActivityViewModel.getIsStartedLiveData().observe(MainActivity.this, isStartedLiveDataObserver);
            }
            else {
                Log.d(TAG, "onComplete: login failed");

                mainActivityViewModel.lightBlueLed();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        mainActivityViewModel = ViewModelProviders.of(MainActivity.this).get(MainActivityViewModel.class);

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            FirebaseAuth.getInstance().signInWithEmailAndPassword("email", "password").addOnCompleteListener(MainActivity.this, myOnCompleteListener);
        else
            mainActivityViewModel.getIsStartedLiveData().observe(MainActivity.this, isStartedLiveDataObserver);
    }
}
