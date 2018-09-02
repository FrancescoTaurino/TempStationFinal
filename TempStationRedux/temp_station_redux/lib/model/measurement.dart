import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';

@immutable
class Measurement {
  final String key;
  final num value;
  final int timestamp;

  Measurement({@required this.key, @required this.value, @required this.timestamp});

  Measurement.fromDataSnapshot(DataSnapshot ds) : this(key: ds.key, value: ds.value["value"], timestamp: ds.value["timestamp"]);

  @override
  String toString() {
    return "key: $key, value: $value, timestamp: $timestamp";
  }

  DateTime datetime() {
    return DateTime.fromMillisecondsSinceEpoch(timestamp * 1000);
  }
}