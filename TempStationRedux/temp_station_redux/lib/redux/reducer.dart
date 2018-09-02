import 'package:temp_station_redux/model/measurement.dart';
import 'package:temp_station_redux/redux/actions.dart';
import 'package:temp_station_redux/redux/state.dart';

AppState reducer(AppState prevState, dynamic action) {
  if (action is SetIsLoading) {
    return prevState.copyWith(isLoading: action.isLoading);
  }

  if (action is SetIsAuth) {
    return prevState.copyWith(isAuth: action.isAuth);
  }

  if (action is SetIsStartedState) {
    return prevState.copyWith(isStarted: action.isStarted);
  }

  if (action is AddMeasurement) {
    return prevState.copyWith(measurements: List<Measurement>.from(prevState.measurements)
      ..insert(0, action.measurement));
  }

  if (action is SetMeasurementsSub) {
    return prevState.copyWith(measurementsSub: action.measurementsSub);
  }

  if (action is ClearMeasurementsState) {
    return prevState.copyWith(measurements: []);
  }

  return prevState;
}