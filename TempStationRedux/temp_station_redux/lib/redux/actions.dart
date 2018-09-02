import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';
import 'package:temp_station_redux/model/measurement.dart';

class StartListenAuthChangement {

}

class SetIsLoading {
  final bool isLoading;

  SetIsLoading.start() : this.isLoading = true;
  SetIsLoading.stop() : this.isLoading = false;
}

class SetIsAuth {
  final bool isAuth;

  SetIsAuth.auth(): this.isAuth = true;
  SetIsAuth.notAuth(): this.isAuth = false;
}

class PerformLogIn {
  final String email;
  final String password;

  PerformLogIn({@required this.email, @required this.password});
}

class PerformLogOut {

}

class SyncWithDatabase {

}

class SetMeasurementsSub {
  final StreamSubscription<Event> measurementsSub;

  SetMeasurementsSub({@required this.measurementsSub});
}

class ClearMeasurementsState {

}

class AddMeasurement {
  final Measurement measurement;

  AddMeasurement({@required this.measurement});
}

class SetIsStartedDatabase {

}

class SetIsStartedState {
  final bool isStarted;

  SetIsStartedState.start(): this.isStarted = true;
  SetIsStartedState.stop(): this.isStarted = false;
}