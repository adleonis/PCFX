package org.pcfx.node.androidn1.work

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.pcfx.node.androidn1.util.PreferencesManager

class DailyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent?.action != ACTION_ATOMIZE_DAILY) {
            return
        }

        Log.d(TAG, "Daily alarm received, triggering atomization")

        val preferencesManager = PreferencesManager(context)
        if (!preferencesManager.isScheduleEnabled()) {
            Log.d(TAG, "Scheduling is disabled, skipping run")
            return
        }

        val atomizeIntent = Intent(context, AtomizeWorker::class.java).apply {
            action = AtomizeWorker.ACTION_RUN_ATOMIZE
        }
        context.startService(atomizeIntent)

        // Reschedule for next day
        AlarmScheduler.scheduleDaily(context)
    }

    companion object {
        private const val TAG = "DailyAlarmReceiver"
        const val ACTION_ATOMIZE_DAILY = "org.pcfx.node.androidn1.ATOMIZE_DAILY"
    }
}
