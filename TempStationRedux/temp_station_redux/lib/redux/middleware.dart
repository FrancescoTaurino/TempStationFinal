import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:redux/redux.dart';
import 'package:temp_station_redux/model/measurement.dart';
import 'package:temp_station_redux/redux/actions.dart';
import 'package:temp_station_redux/redux/state.dart';

import 'package:firebase_auth/firebase_auth.dart';

void middleware(Store<AppState> store, dynamic action, NextDispatcher next) {
  if (action is StartListenAuthChangement) {
    store.dispatch(SetIsLoading.start());

    FirebaseAuth.instance.onAuthStateChanged.listen((FirebaseUser firebaseUser) {
      if (firebaseUser != null) {
        store.dispatch(SetIsAuth.auth());

        FirebaseDatabase.instance.reference().child("isStarted").once().then(
          (event) {
            store.dispatch(event.value ? SetIsStartedState.start() : SetIsStartedState.stop());
            store.dispatch(SetIsLoading.stop());

            // ignore: cancel_subscriptions
            StreamSubscription<Event> measurementsSub = FirebaseDatabase.instance.reference().child("measurements").orderByChild("timestamp").onChildAdded.listen(
              (event) {
                print(Measurement.fromDataSnapshot(event.snapshot).toString());
                store.dispatch(AddMeasurement(measurement: Measurement.fromDataSnapshot(event.snapshot)));
              }
            );

            store.dispatch(SetMeasurementsSub(measurementsSub: measurementsSub));
          }
        );
      }
      else {
        store.dispatch(SetIsAuth.notAuth());

        if (store.state.measurementsSub != null) {
          store.state.measurementsSub.cancel();
          store.dispatch(ClearMeasurementsState());
        }

        store.dispatch(SetIsLoading.stop());
      }
    });
  }
  else if(action is PerformLogIn) {
    store.dispatch(SetIsLoading.start());

    FirebaseAuth.instance.signInWithEmailAndPassword(
      email: action.email,
      password: action.password
    ).catchError((e) {
      print(e.message);
      store.dispatch(SetIsLoading.stop());
    });
  }
  else if(action is PerformLogOut) {
    store.dispatch(SetIsLoading.start());

    FirebaseAuth.instance.signOut().catchError((e) {
      print(e.message);
      store.dispatch(SetIsLoading.stop());
    });
  }
  else if(action is SetIsStartedDatabase) {
    store.dispatch(SetIsLoading.start());

    FirebaseDatabase.instance.reference().child("isStarted").set(!store.state.isStarted).then(
      (_) {
        store.dispatch(store.state.isStarted ? SetIsStartedState.stop() : SetIsStartedState.start());
        store.dispatch(SetIsLoading.stop());
      }
    );
  }

  next(action);
}