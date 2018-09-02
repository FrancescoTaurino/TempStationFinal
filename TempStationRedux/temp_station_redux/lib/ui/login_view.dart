import 'package:flutter/material.dart';
import 'package:redux/redux.dart';
import 'package:temp_station_redux/redux/state.dart';
import 'package:temp_station_redux/redux/actions.dart';

class LoginView extends StatelessWidget {
  final _emailController = TextEditingController();
  final _pwdController = TextEditingController();

  final Store<AppState> store;

  LoginView({@required this.store});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("TempStation Redux - Login"),
      ),
      body: Center(
        child: ListView(
          padding: EdgeInsets.only(left: 24.0, right: 24.0),
          children: <Widget>[
            Padding(
              padding: EdgeInsets.all(4.0),
            ),
            TextFormField(
              keyboardType: TextInputType.emailAddress,
              autofocus: false,
              controller: _emailController,
              decoration: InputDecoration(
                hintText: 'Email',
                icon: Icon(Icons.person)
              ),
            ),
            Padding(
              padding: EdgeInsets.all(4.0),
            ),
            TextFormField(
              autofocus: false,
              obscureText: true,
              controller: _pwdController,
              decoration: InputDecoration(
                hintText: 'Password',
                icon: Icon(Icons.lock)
              ),
            ),
            Padding(
              padding: EdgeInsets.all(12.0),
            ),
            LoginButton(
              emailController: _emailController,
              pwdController: _pwdController,
              store: store,
            )
          ],
        ),
      )
    );
  }
}

class LoginButton extends StatelessWidget {
  final emailController;
  final pwdController;

  final Store<AppState> store;

  LoginButton({@required this.emailController, this.pwdController, @required this.store});

  @override
  Widget build(BuildContext context) {
    return Material(
      borderRadius: BorderRadius.circular(30.0),
      shadowColor: Colors.lightBlueAccent.shade100,
      elevation: 5.0,
      child: MaterialButton(
        height: 42.0,
        onPressed: () {
          if (emailController.text.isNotEmpty && pwdController.text.isNotEmpty) {
            store.dispatch(
              PerformLogIn(
                email: emailController.text,
                password: pwdController.text)
            );
          }
          else
            Scaffold.of(context).showSnackBar(
              SnackBar(
                content: Text("Please, type email and password"),
                duration: Duration(seconds: 3),
              )
            );
        },
        color: Colors.lightBlueAccent,
        child: Text('Log In',
          style: TextStyle(color: Colors.white
          )
        ),
      ),
    );
  }
}

