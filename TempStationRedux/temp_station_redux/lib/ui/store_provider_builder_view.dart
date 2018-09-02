import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:temp_station_redux/redux/actions.dart';
import 'package:temp_station_redux/redux/middleware.dart';
import 'package:temp_station_redux/redux/reducer.dart';
import 'package:temp_station_redux/redux/state.dart';
import 'package:temp_station_redux/ui/home_view.dart';
import 'package:temp_station_redux/ui/loading_view.dart';

import 'package:temp_station_redux/ui/login_view.dart';

class StoreProviderBuilderView extends StatelessWidget {
  final store = Store<AppState>(reducer, initialState: AppState.init());

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "TempStation Redux",
      debugShowCheckedModeBanner: false,
      home: StoreProvider<AppState>(
        store: Store<AppState>(
          reducer,
          initialState:AppState.init(),
          middleware: [middleware],
        ),
        child: StoreBuilder(
          onInit: (store) => store.dispatch(StartListenAuthChangement()),
          // onDispose: null
          builder: (BuildContext context, Store<AppState> store) {
            if (store.state.isLoading)
              return LoadingView();
            else {
              if (store.state.isAuth)
                return HomeView(store: store);
              else
                return LoginView(store: store);
            }
          },
        )
      )
    );
  }
}
