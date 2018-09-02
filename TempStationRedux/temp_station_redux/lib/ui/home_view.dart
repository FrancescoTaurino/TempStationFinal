import 'package:flutter/material.dart';
import 'package:redux/redux.dart';
import 'package:temp_station_redux/redux/actions.dart';
import 'package:temp_station_redux/redux/state.dart';

class HomeView extends StatelessWidget {
  final Store<AppState> store;

  HomeView({@required this.store});

  _buildAppBarActions() {
    if(store.state.isStarted)
      return <Widget>[];
    else
      return <Widget>[
        IconButton(
          onPressed: () => store.dispatch(PerformLogOut()),
          icon: Icon(Icons.exit_to_app),
        ),
      ];
  }

  _buildBodyWidgets() {
    if(store.state.measurements.isEmpty) {
      return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Center(
            child: Icon(Icons.warning,
              color: Colors.grey,
              size: 40.0,
            ),
          ),
          Text("No Measurements",
            style: TextStyle(
              fontSize: 22.0,
              color: Colors.grey,
              fontStyle: FontStyle.italic
            ),
          ),
        ],
      );
    }
    else {
      return ListView.builder(
        padding: EdgeInsets.all(10.0),
        itemBuilder: (BuildContext context, int i) {
          return Column(
            children: <Widget>[
              ListTile(
                title: Center(
                  child: Text("${store.state.measurements[i].value.toString()}°",
                    style: TextStyle(
                      fontSize: 24.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                subtitle: Center(
                  child: Text(store.state.measurements[i].datetime().toString().substring(0, 19),
                    style: TextStyle(
                        fontStyle: FontStyle.italic
                    ),
                  ),
                ),
              ),
              Divider()
            ],
          );
        },
        itemCount: store.state.measurements.length,
      );
    }
  }

  _buildFloatingActionButton() {
    return FloatingActionButton(
      onPressed: () => store.dispatch(SetIsStartedDatabase()),
      child: Icon(store.state.isStarted ? Icons.stop : Icons.play_arrow)
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("TempStation Redux"),
        actions: _buildAppBarActions(),
      ),
      body: _buildBodyWidgets(),
      floatingActionButton: _buildFloatingActionButton(),
    );
  }
}
