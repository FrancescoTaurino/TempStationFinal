import 'package:flutter/material.dart';

class LoadingView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("TempStation Redux"),
      ),
      body: Center(
        child: CircularProgressIndicator(),
      ),
    );
  }
}
