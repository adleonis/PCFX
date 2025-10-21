package org.pcfx.node.androidn1.work

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import org.pcfx.node.androidn1.util.PreferencesManager
import java.util.Calendar

object AlarmScheduler {
    private const val TAG = "AlarmScheduler"

    fun scheduleDaily(context: Context) {
        val preferencesManager = PreferencesManager(context)
        if (!preferencesManager.isScheduleEnabled()) {
            Log.d(TAG, "Scheduling is disabled, not scheduling alarm")
            return
        }

        val (hourOfDay, minuteOfHour) = preferencesManager.getScheduleTime()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, DailyAlarmReceiver::class.java).apply {
            action = DailyAlarmReceiver.ACTION_ATOMIZE_DAILY
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minuteOfHour)
            set(Calendar.SECOND, 0)

            // If the scheduled time has already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Use setExactAndAllowWhileIdle for exact scheduling on newer Android
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                // Fallback for older Android versions
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d(TAG, "Scheduled daily atomization at ${String.format("%02d:%02d", hourOfDay, minuteOfHour)}")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to schedule alarm (missing SCHEDULE_EXACT_ALARM permission?)", e)
        }
    }

    fun cancelDaily(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyAlarmReceiver::class.java).apply {
            action = DailyAlarmReceiver.ACTION_ATOMIZE_DAILY
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.cancel(pendingIntent)
            Log.d(TAG, "Cancelled daily atomization alarm")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to cancel alarm", e)
        }
    }

    private const val ALARM_REQUEST_CODE = 42
}
