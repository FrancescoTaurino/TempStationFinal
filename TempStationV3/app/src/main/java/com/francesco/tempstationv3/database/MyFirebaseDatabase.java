package com.francesco.tempstationv3.database;

import com.francesco.tempstationv3.model.Measurement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyFirebaseDatabase {
    private static DatabaseReference getReference(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }

    public static void addIsStartedValueEventListener(ValueEventListener valueEventListener) {
        getReference("isStarted").addValueEventListener(valueEventListener);
    }

    public static void removeIsStartedValueEventListener(ValueEventListener valueEventListener) {
        getReference("isStarted").removeEventListener(valueEventListener);
    }

    public static void pushMeasurement(Measurement measurement) {
        getReference("measurements").push().setValue(measurement);
    }
}
