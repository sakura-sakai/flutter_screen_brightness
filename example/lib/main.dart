import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:screen_brightness/screen_brightness.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  double _brightness = -1.0;
  bool _isKeptOn = false;

  @override
  void initState() {
    super.initState();
    initBrightnessState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initBrightnessState() async {
    double brightness;
    bool isKeptOn;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      brightness = await ScreenBrightness.brightness;
      isKeptOn = await ScreenBrightness.isKeeptOn;
    } on PlatformException {
      brightness = -2.0;
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _brightness = brightness;
      _isKeptOn = isKeptOn;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('ScreenBrightness plugin'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('brightness: $_brightness'),
              Text('isKeptOn: $_isKeptOn'),
              const SizedBox(
                height: 50,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  TextButton(
                    onPressed: () {
                      ScreenBrightness.setBrightness(1.0);
                    },
                    child: const Text('Set Brightness'),
                  ),
                  TextButton(
                    onPressed: () {
                      ScreenBrightness.keepOn(true);
                    },
                    child: const Text('keepOn'),
                  ),
                ],
              )
            ],
          ),
        ),
      ),
    );
  }
}
