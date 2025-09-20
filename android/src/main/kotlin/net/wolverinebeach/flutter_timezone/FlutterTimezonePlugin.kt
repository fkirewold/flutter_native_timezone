package net.wolverinebeach.flutter_timezone

import android.os.Build
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import java.time.ZoneId
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.*
import kotlin.collections.ArrayList

class FlutterTimezonePlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, "flutter_timezone")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun parseLocale(localeIdentifier: String?): Locale? {
        return if (localeIdentifier != null) {
            try {
                val parts = localeIdentifier.split("_", "-")
                when (parts.size) {
                    1 -> Locale(parts[0])
                    2 -> Locale(parts[0], parts[1])
                    else -> null
                }
            } catch (e: Exception) {
                null
            }
        } else {
            Locale.getDefault()
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "getLocalTimezone" -> {
                val locale = parseLocale(call.arguments as? String)

                val timezoneId = getLocalTimezone()
                val timeZone = TimeZone.getTimeZone(timezoneId)
                val localizedName = locale?.let { timeZone.getDisplayName(it) }

                result.success(mapOf(
                    "identifier" to timezoneId,
                    "localizedName" to localizedName,
                    "locale" to locale?.toString()
                ))
            }

            "getAvailableTimezones" -> {
                val locale = parseLocale(call.arguments as? String)
                val timezones = getAvailableTimezonesWithLocalization(locale)
                result.success(timezones)
            }

            else -> result.notImplemented()
        }
    }

    private fun getLocalTimezone(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZoneId.systemDefault().id
        } else {
            TimeZone.getDefault().id
        }
    }

    private fun getAvailableTimezones(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZoneId.getAvailableZoneIds().toCollection(ArrayList())
        } else {
            TimeZone.getAvailableIDs().toCollection(ArrayList())
        }
    }

    private fun getAvailableTimezonesWithLocalization(locale: Locale?): List<Map<String, Any?>> {
        val availableTimezones = ArrayList<Map<String, Any?>>()
        val timezoneIds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZoneId.getAvailableZoneIds()
        } else {
            TimeZone.getAvailableIDs().toList()
        }
        
        for (timezoneId in timezoneIds) {
            val timeZone = TimeZone.getTimeZone(timezoneId)
            val localizedName = locale?.let { timeZone.getDisplayName(it) }
            
            availableTimezones.add(mapOf(
                "identifier" to timezoneId,
                "localizedName" to localizedName,
                "locale" to locale?.toString()
            ))
        }
        return availableTimezones
    }
}
