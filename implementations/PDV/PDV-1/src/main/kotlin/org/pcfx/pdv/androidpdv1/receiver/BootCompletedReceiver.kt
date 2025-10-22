package org.pcfx.pdv.androidpdv1.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.pcfx.pdv.androidpdv1.service.PdvServerService

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        Log.d(TAG, "Boot completed, checking if PDV auto-start is enabled")

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val autoStartKey = booleanPreferencesKey("pdv_auto_start")
                val preferences = context.dataStore.data.first()
                val autoStart = preferences[autoStartKey] ?: false

                if (autoStart) {
                    Log.d(TAG, "Starting PDV server on boot")
                    val startIntent = Intent(context, PdvServerService::class.java).apply {
                        action = PdvServerService.ACTION_START_SERVER
                    }
                    context.startService(startIntent)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error checking auto-start preference", e)
            }
        }
    }

    companion object {
        private const val TAG = "BootCompletedReceiver"
    }
}

private val android.content.Context.dataStore by preferencesDataStore(name = "pdv_preferences")
