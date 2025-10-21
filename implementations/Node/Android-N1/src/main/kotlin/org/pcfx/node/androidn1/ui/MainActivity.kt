package org.pcfx.node.androidn1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.pcfx.node.androidn1.R
import org.pcfx.node.androidn1.util.PreferencesManager
import org.pcfx.node.androidn1.work.AlarmScheduler
import org.pcfx.node.androidn1.work.AtomizeWorker

class MainActivity : AppCompatActivity() {
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var runNowButton: Button
    private lateinit var statusText: TextView
    private lateinit var settingsButton: Button
    private lateinit var scheduleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesManager = PreferencesManager(this)
        initializeViews()
        setupListeners()
        updateStatus()
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun initializeViews() {
        runNowButton = findViewById(R.id.btn_run_now)
        statusText = findViewById(R.id.tv_status)
        settingsButton = findViewById(R.id.btn_settings)
        scheduleButton = findViewById(R.id.btn_schedule)
    }

    private fun setupListeners() {
        runNowButton.setOnClickListener {
            runAtomizeNow()
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        scheduleButton.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }
    }

    private fun runAtomizeNow() {
        runNowButton.isEnabled = false
        Toast.makeText(this, "Starting atomization...", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, AtomizeWorker::class.java).apply {
            action = AtomizeWorker.ACTION_RUN_ATOMIZE
        }
        startService(intent)
    }

    private fun updateStatus() {
        lifecycleScope.launch {
            preferencesManager.getLastRunInfoFlow().collect { runInfo ->
                val statusMessage = buildString {
                    append("Last Run: ${runInfo.status.replace("_", " ").uppercase()}\n")
                    if (runInfo.lastRunTime != null) {
                        append("Time: ${runInfo.lastRunTime}\n")
                    }
                    append("Events Processed: ${runInfo.eventsCount}\n")
                    append("Atoms Published: ${runInfo.atomsCount}\n")
                    if (runInfo.error != null) {
                        append("Error: ${runInfo.error}\n")
                    }

                    if (preferencesManager.isScheduleEnabled()) {
                        val (hour, minute) = preferencesManager.getScheduleTime()
                        append("\nDaily Run: ${String.format("%02d:%02d", hour, minute)}")
                    } else {
                        append("\nDaily Run: Disabled")
                    }
                }
                statusText.text = statusMessage
                runNowButton.isEnabled = true
            }
        }
    }
}
