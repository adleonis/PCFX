package org.pcfx.node.androidn1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.pcfx.node.androidn1.R
import org.pcfx.node.androidn1.data.PdvRepository
import org.pcfx.node.androidn1.util.PreferencesManager
import org.pcfx.node.androidn1.work.AlarmScheduler
import org.pcfx.node.androidn1.work.AtomizeWorker

class MainActivity : AppCompatActivity() {
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var pdvRepository: PdvRepository
    private lateinit var runNowButton: Button
    private lateinit var runAllButton: Button
    private lateinit var statusText: TextView
    private lateinit var settingsButton: Button
    private lateinit var scheduleButton: Button
    private lateinit var pdvStatusIcon: ImageView
    private lateinit var pdvStatusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesManager = PreferencesManager(this)
        pdvRepository = PdvRepository(this)
        initializeViews()
        setupListeners()
        updateStatus()
        checkPdvConnectivity()
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
        checkPdvConnectivity()
    }

    private fun initializeViews() {
        runNowButton = findViewById(R.id.btn_run_now)
        runAllButton = findViewById(R.id.btn_run_all)
        statusText = findViewById(R.id.tv_status)
        settingsButton = findViewById(R.id.btn_settings)
        scheduleButton = findViewById(R.id.btn_schedule)
        pdvStatusIcon = findViewById(R.id.pdv_status_icon)
        pdvStatusText = findViewById(R.id.pdv_status_text)
    }

    private fun setupListeners() {
        runNowButton.setOnClickListener {
            runAtomizeNow(maxBatches = 10)
        }

        runAllButton.setOnClickListener {
            runAtomizeNow(maxBatches = Int.MAX_VALUE)
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        scheduleButton.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }
    }

    private fun runAtomizeNow(maxBatches: Int = 10) {
        runNowButton.isEnabled = false
        runAllButton.isEnabled = false

        val message = if (maxBatches == Int.MAX_VALUE) {
            "Processing all events..."
        } else {
            "Starting atomization (10 batches)..."
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, AtomizeWorker::class.java).apply {
            action = AtomizeWorker.ACTION_RUN_ATOMIZE
            putExtra(AtomizeWorker.EXTRA_MAX_BATCHES, maxBatches)
        }
        startService(intent)

        lifecycleScope.launch {
            kotlinx.coroutines.delay(1000)
            runNowButton.isEnabled = true
            runAllButton.isEnabled = true
        }
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

    private fun checkPdvConnectivity() {
        lifecycleScope.launch {
            pdvStatusText.text = "Checking PDV connection..."
            pdvStatusIcon.setImageResource(android.R.drawable.ic_dialog_info)
            pdvStatusIcon.setColorFilter(android.graphics.Color.GRAY)

            val result = withContext(Dispatchers.IO) {
                pdvRepository.testConnectivity()
            }

            if (result.isSuccess) {
                pdvStatusIcon.setImageResource(android.R.drawable.ic_input_get)
                pdvStatusIcon.setColorFilter(android.graphics.Color.GREEN)
                pdvStatusText.text = "✓ PDV Server Connected"
                pdvStatusText.setTextColor(android.graphics.Color.GREEN)
            } else {
                pdvStatusIcon.setImageResource(android.R.drawable.ic_dialog_alert)
                pdvStatusIcon.setColorFilter(android.graphics.Color.RED)
                val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                pdvStatusText.text = "✗ PDV Server Disconnected\n$errorMsg"
                pdvStatusText.setTextColor(android.graphics.Color.RED)
            }
        }
    }
}
