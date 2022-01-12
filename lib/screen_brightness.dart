import 'dart:async';

import 'package:flutter/services.dart';

class ScreenBrightness {
  static const MethodChannel _channel =
      const MethodChannel('screen_brightness');

  /// Get current brightness of device's
  /// 0.0 <= value <= 1.0
  static Future<double> get brightness async {
    return (await _channel.invokeMethod('getBrightness')) as double;
  }

  /// Set brightness
  static Future setBrightness(double brightness) async {
    double data = 0.0;

    if (brightness >= 1.0) data = 1.0;
    if (brightness <= 0.0) data = 0.0;
    if (brightness <= 1.0 && brightness >= 0.0) data = brightness;

    await _channel.invokeMethod(
      'setBrightness',
      {"brightness": data},
    );
  }


  /// Check if the screen is in keep-on mode
  static Future<bool> get isKeeptOn async {
    return (await _channel.invokeMethod('isKeptOn')) as bool;
  }

  /// Enable or disable keep-on screen mode
  static Future keepOn(bool isKeepOn) async {
    await _channel.invokeMethod(
      'keepOn',
      {"isKeepOn": isKeepOn},
    );
  }
}
