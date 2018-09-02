package com.francesco.tempstationv3.viewmodel.livedata;

import android.support.annotation.NonNull;
import android.util.Log;

import com.francesco.tempstationv3.database.MyFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class IsStartedLiveData extends MyLiveData<Boolean> {
    private static final String TAG = IsStartedLiveData.class.getName();

    private final ValueEventListener myValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.getValue(Boolean.class));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "onCancelled: " + databaseError.getMessage());
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive");

        MyFirebaseDatabase.addIsStartedValueEventListener(myValueEventListener);
    }

    @Override
    protected void onInactive() {
        MyFirebaseDatabase.removeIsStartedValueEventListener(myValueEventListener);

        Log.d(TAG, "onInactive");
        super.onInactive();
    }
}
