package org.pcfx.pdv.androidpdv1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.pcfx.pdv.androidpdv1.R
import org.pcfx.pdv.androidpdv1.data.PdvPreferences
import org.pcfx.pdv.androidpdv1.service.PdvServerService

class MainActivity : AppCompatActivity() {
    private lateinit var buttonStart: Button
    private lateinit var buttonStop: Button
    private lateinit var checkboxAutoStart: CheckBox
    private lateinit var seekbarPort: SeekBar
    private lateinit var textPort: TextView
    private lateinit var textStatus: TextView

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart = findViewById(R.id.buttonStart)
        buttonStop = findViewById(R.id.buttonStop)
        checkboxAutoStart = findViewById(R.id.checkboxAutoStart)
        seekbarPort = findViewById(R.id.seekbarPort)
        textPort = findViewById(R.id.textPort)
        textStatus = findViewById(R.id.textStatus)

        setupUI()
        updateStatus()
        checkAndStartServerIfNeeded()
    }

    private fun setupUI() {
        buttonStart.setOnClickListener {
            scope.launch {
                startPdvServer()
            }
        }

        buttonStop.setOnClickListener {
            scope.launch {
                stopPdvServer()
            }
        }

        checkboxAutoStart.setOnCheckedChangeListener { _, isChecked ->
            scope.launch {
                setAutoStart(isChecked)
            }
        }

        seekbarPort.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val port = 7000 + progress
                    textPort.text = port.toString()
                    scope.launch {
                        setPort(port)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun updateStatus() {
        val prefs = PdvPreferences.getInstance(this)
        try {
            val port = prefs.getPort()
            textPort.text = port.toString()
            seekbarPort.progress = port - 7000

            val autoStart = prefs.isAutoStartEnabled()
            checkboxAutoStart.isChecked = autoStart
        } catch (e: Exception) {
            textStatus.text = "Error: ${e.message}"
        }
    }

    private suspend fun startPdvServer() {
        val intent = Intent(this, PdvServerService::class.java).apply {
            action = PdvServerService.ACTION_START_SERVER
        }
        startService(intent)
        textStatus.text = "PDV Server starting..."
        buttonStart.isEnabled = false
        buttonStop.isEnabled = true
    }

    private suspend fun stopPdvServer() {
        val intent = Intent(this, PdvServerService::class.java).apply {
            action = PdvServerService.ACTION_STOP_SERVER
        }
        startService(intent)
        textStatus.text = "PDV Server stopped"
        buttonStart.isEnabled = true
        buttonStop.isEnabled = false
    }

    private suspend fun setAutoStart(enabled: Boolean) {
        val prefs = PdvPreferences.getInstance(this)
        prefs.setAutoStartEnabled(enabled)
    }

    private suspend fun setPort(port: Int) {
        val prefs = PdvPreferences.getInstance(this)
        prefs.setPort(port)
    }

    private fun checkAndStartServerIfNeeded() {
        val prefs = PdvPreferences.getInstance(this)
        if (prefs.isAutoStartEnabled()) {
            scope.launch {
                startPdvServer()
            }
        }
    }
}
