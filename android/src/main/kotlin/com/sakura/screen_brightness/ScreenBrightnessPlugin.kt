package com.sakura.screen_brightness


import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.view.WindowManager
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** ScreenBrightnessPlugin */
class ScreenBrightnessPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity

    /** FlutterPlugin */
    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "screen_brightness")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    /** ActivityAware */
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        println("ScreenBrightnessPlugin: onAttachedToActivity")
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }

    /** MethodCallHandler */
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getBrightness" -> result.success(getBrightness())

            "setBrightness" -> {
                setBrightness(call)
                result.success(null)
            }

            "isKeptOn" -> result.success(isKeptOn())

            "keepOn" -> {
                keepOn(call)
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

    /** Brightness */
    private fun getBrightness(): Float {
        var result: Float = activity.window.attributes.screenBrightness

        if (result < 0) { // the application is using the system brightness
            try {
                result = Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS) / 255.toFloat()
            } catch (e: Settings.SettingNotFoundException) {
                result = 1.0f
                e.printStackTrace()
            }
        }

        return result
    }

    private fun setBrightness(call: MethodCall) {
        val brightness: Double = call.argument("brightness")!!
        val layoutParams: WindowManager.LayoutParams = activity.window.attributes
        layoutParams.screenBrightness = brightness.toFloat()
        activity.window.attributes = layoutParams
        println("setBrightness done: $brightness")
    }

    private fun isKeptOn(): Boolean {
        val flags: Int = activity.window.attributes.flags
        return flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON != 0
    }

    private fun keepOn(call: MethodCall) {
        val isKeepOn: Boolean = call.argument("isKeepOn")!!
        if (isKeepOn) {
            println("Keeping screen on ")
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            println("Not keeping screen on")
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
