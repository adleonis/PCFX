package org.pcfx.pdv.androidpdv1.data

import android.content.Context
import android.content.SharedPreferences

class PdvPreferences(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getPort(): Int {
        return prefs.getInt(KEY_PORT, DEFAULT_PORT)
    }

    fun setPort(port: Int) {
        val editor = prefs.edit()
        editor.putInt(KEY_PORT, port)
        editor.apply()
    }

    fun isAutoStartEnabled(): Boolean {
        return prefs.getBoolean(KEY_AUTO_START, true)
    }

    fun setAutoStartEnabled(enabled: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_AUTO_START, enabled)
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "pdv_preferences"
        private const val KEY_PORT = "pdv_port"
        private const val KEY_AUTO_START = "pdv_auto_start"
        private const val DEFAULT_PORT = 7777

        @Volatile
        private var instance: PdvPreferences? = null

        fun getInstance(context: Context): PdvPreferences {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = PdvPreferences(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }
}
