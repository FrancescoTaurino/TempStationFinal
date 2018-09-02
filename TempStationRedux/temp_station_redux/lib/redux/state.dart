import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';
import 'package:temp_station_redux/model/measurement.dart';

@immutable
class AppState {
  final bool isLoading;
  final bool isAuth;
  final bool isStarted;
  final StreamSubscription<Event> measurementsSub;
  final List<Measurement> measurements;

  AppState({
    @required this.isLoading,
    @required this.isAuth,
    @required this.isStarted,
    @required this.measurementsSub,
    @required this.measurements,
  });

  AppState.init() : this(isLoading: false, isAuth: false, isStarted: false, measurementsSub: null, measurements: []);

  AppState copyWith({bool isLoading, bool isAuth, bool isStarted, StreamSubscription<Event> measurementsSub, List<Measurement> measurements}) {
    return AppState(
      isLoading: isLoading ?? this.isLoading,
      isAuth: isAuth ?? this.isAuth,
      isStarted: isStarted ?? this.isStarted,
      measurementsSub: measurementsSub ?? this.measurementsSub,
      measurements: measurements ?? this.measurements,
    );
  }
}
