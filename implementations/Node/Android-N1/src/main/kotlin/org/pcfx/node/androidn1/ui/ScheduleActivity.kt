package org.pcfx.node.androidn1.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.pcfx.node.androidn1.R
import org.pcfx.node.androidn1.util.PreferencesManager
import org.pcfx.node.androidn1.work.AlarmScheduler

class ScheduleActivity : AppCompatActivity() {
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var scheduleSwitch: Switch
    private lateinit var timeButton: Button
    private lateinit var timeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        preferencesManager = PreferencesManager(this)

        initializeViews()
        loadSettings()
        setupListeners()
    }

    private fun initializeViews() {
        scheduleSwitch = findViewById(R.id.switch_schedule_enabled)
        timeButton = findViewById(R.id.btn_set_time)
        timeText = findViewById(R.id.tv_schedule_time)
    }

    private fun loadSettings() {
        lifecycleScope.launch {
            preferencesManager.isScheduleEnabledFlow().collect { enabled ->
                scheduleSwitch.isChecked = enabled
                timeButton.isEnabled = enabled
            }

            preferencesManager.getScheduleTimeFlow().collect { timeString ->
                timeText.text = "Scheduled time: $timeString"
            }
        }
    }

    private fun setupListeners() {
        scheduleSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                preferencesManager.setScheduleEnabled(isChecked)
                timeButton.isEnabled = isChecked

                if (isChecked) {
                    AlarmScheduler.scheduleDaily(this@ScheduleActivity)
                    Toast.makeText(this@ScheduleActivity, "Daily schedule enabled", Toast.LENGTH_SHORT).show()
                } else {
                    AlarmScheduler.cancelDaily(this@ScheduleActivity)
                    Toast.makeText(this@ScheduleActivity, "Daily schedule disabled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        timeButton.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun showTimePickerDialog() {
        val (currentHour, currentMinute) = preferencesManager.getScheduleTime()

        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                lifecycleScope.launch {
                    preferencesManager.setScheduleTime(hourOfDay, minute)
                    timeText.text = String.format("Scheduled time: %02d:%02d", hourOfDay, minute)
                    AlarmScheduler.scheduleDaily(this@ScheduleActivity)
                    Toast.makeText(
                        this@ScheduleActivity,
                        "Schedule updated to ${String.format("%02d:%02d", hourOfDay, minute)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }
}
