package org.pcfx.pdv.androidpdv1.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.pcfx.pdv.androidpdv1.R
import org.pcfx.pdv.androidpdv1.data.PdvPreferences
import org.pcfx.pdv.androidpdv1.service.PdvServerService

class SetupFragment : Fragment() {
    private lateinit var buttonStart: Button
    private lateinit var buttonStop: Button
    private lateinit var checkboxAutoStart: CheckBox
    private lateinit var seekbarPort: SeekBar
    private lateinit var textPort: TextView
    private lateinit var textStatus: TextView
    private lateinit var textServerUrl: TextView
    private lateinit var textServerStatus: TextView

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonStart = view.findViewById(R.id.buttonStart)
        buttonStop = view.findViewById(R.id.buttonStop)
        checkboxAutoStart = view.findViewById(R.id.checkboxAutoStart)
        seekbarPort = view.findViewById(R.id.seekbarPort)
        textPort = view.findViewById(R.id.textPort)
        textStatus = view.findViewById(R.id.textStatus)
        textServerUrl = view.findViewById(R.id.textServerUrl)
        textServerStatus = view.findViewById(R.id.textServerStatus)

        setupUI()
        updateStatus()
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
                    textServerUrl.text = "http://127.0.0.1:$port"
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
        val prefs = PdvPreferences.getInstance(requireContext())
        try {
            val port = prefs.getPort()
            textPort.text = port.toString()
            textServerUrl.text = "http://127.0.0.1:$port"
            seekbarPort.progress = port - 7000

            val autoStart = prefs.isAutoStartEnabled()
            checkboxAutoStart.isChecked = autoStart

            textStatus.text = "PDV Server Status: Unknown"
            textServerStatus.text = "Stopped"
            
            scope.launch {
                checkServerStatus(port)
            }
        } catch (e: Exception) {
            textStatus.text = "Error: ${e.message}"
        }
    }

    private suspend fun checkServerStatus(port: Int) {
        try {
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                val url = java.net.URL("http://127.0.0.1:$port/health")
                val connection = url.openConnection()
                connection.connectTimeout = 1000
                connection.connect()
            }
            textStatus.text = "PDV Server Status: Running"
            textServerStatus.text = "Running"
            buttonStart.isEnabled = false
            buttonStop.isEnabled = true
        } catch (e: Exception) {
            textStatus.text = "PDV Server Status: Stopped"
            textServerStatus.text = "Stopped"
            buttonStart.isEnabled = true
            buttonStop.isEnabled = false
        }
    }

    private suspend fun startPdvServer() {
        val intent = Intent(requireContext(), PdvServerService::class.java).apply {
            action = PdvServerService.ACTION_START_SERVER
        }
        requireContext().startService(intent)
        textStatus.text = "PDV Server starting..."
        buttonStart.isEnabled = false
        buttonStop.isEnabled = true
        delay(2000)
        val port = PdvPreferences.getInstance(requireContext()).getPort()
        checkServerStatus(port)
    }

    private suspend fun stopPdvServer() {
        val intent = Intent(requireContext(), PdvServerService::class.java).apply {
            action = PdvServerService.ACTION_STOP_SERVER
        }
        requireContext().startService(intent)
        textStatus.text = "PDV Server stopped"
        textServerStatus.text = "Stopped"
        buttonStart.isEnabled = true
        buttonStop.isEnabled = false
    }

    private suspend fun setAutoStart(enabled: Boolean) {
        val prefs = PdvPreferences.getInstance(requireContext())
        prefs.setAutoStartEnabled(enabled)
    }

    private suspend fun setPort(port: Int) {
        val prefs = PdvPreferences.getInstance(requireContext())
        prefs.setPort(port)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.launch { }
    }
}
