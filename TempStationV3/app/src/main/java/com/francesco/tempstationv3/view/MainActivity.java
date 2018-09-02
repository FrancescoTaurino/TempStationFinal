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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private long counter = 0;
    private boolean flag = true;

    private final FirebaseAuth.AuthStateListener myAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                final MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(MainActivity.this).get(MainActivityViewModel.class);
                mainActivityViewModel.getIsStartedLiveData().observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isStarted) {
                        if (isStarted != null) {
                            mainActivityViewModel.lightLed(isStarted);

                            if (isStarted) {
                                Log.d(TAG, "onChanged: start temperature listening");

                                mainActivityViewModel.getTemperatureLiveData().observe(MainActivity.this, new Observer<Float>() {
                                    @Override
                                    public void onChanged(@Nullable Float value) {
                                        Log.d(TAG, "onChanged: " + value);

                                        if (value != null) {
                                            if (counter % 50 == 0)
                                                MyFirebaseDatabase.pushMeasurement(new Measurement(value));

                                            counter++;
                                        }
                                    }
                                });
                            }
                            else {
                                Log.d(TAG, "onChanged: stop temperature listening");

                                mainActivityViewModel.getTemperatureLiveData().removeObservers(MainActivity.this);
                            }
                        }
                    }
                });
            }
            else if (flag) {
                firebaseAuth.signInWithEmailAndPassword("email", "password");
                flag = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth.getInstance().addAuthStateListener(myAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().removeAuthStateListener(myAuthStateListener);

        super.onDestroy();
    }
}
