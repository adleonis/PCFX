package org.pcfx.node.androidn1.work

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.pcfx.node.androidn1.util.PreferencesManager

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        Log.d(TAG, "Device boot completed, rescheduling alarms")

        val preferencesManager = PreferencesManager(context)
        if (preferencesManager.isScheduleEnabled()) {
            AlarmScheduler.scheduleDaily(context)
            Log.d(TAG, "Daily atomization alarm rescheduled")
        }
    }

    companion object {
        private const val TAG = "BootCompletedReceiver"
    }
}
