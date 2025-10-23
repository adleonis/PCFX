package org.pcfx.node.androidn1.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
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

class SettingsActivity : AppCompatActivity() {
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var pdvRepository: PdvRepository
    private lateinit var urlEditText: EditText
    private lateinit var tokenEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var testButton: Button
    private lateinit var statusText: TextView
    private lateinit var processAccessibilityToggle: Switch
    private lateinit var processOcrToggle: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferencesManager = PreferencesManager(this)
        pdvRepository = PdvRepository(this)

        initializeViews()
        loadSettings()
        setupListeners()
    }

    private fun initializeViews() {
        urlEditText = findViewById(R.id.et_pdv_url)
        tokenEditText = findViewById(R.id.et_capability_token)
        saveButton = findViewById(R.id.btn_save)
        testButton = findViewById(R.id.btn_test_connectivity)
        statusText = findViewById(R.id.tv_connection_status)

        // Event type filter toggles
        processAccessibilityToggle = findViewById(R.id.toggle_process_accessibility_events)
        processOcrToggle = findViewById(R.id.toggle_process_ocr_events)
    }

    private fun loadSettings() {
        urlEditText.setText(preferencesManager.getPdvBaseUrl())
        tokenEditText.setText(preferencesManager.getPdvCapabilityToken())

        // Load event type filter preferences
        val sharedPreferences = getSharedPreferences("node_preferences", MODE_PRIVATE)
        processAccessibilityToggle.isChecked = sharedPreferences.getBoolean("process_event_type_text", false)
        processOcrToggle.isChecked = sharedPreferences.getBoolean("process_event_type_ocr-text", true)
    }

    private fun setupListeners() {
        saveButton.setOnClickListener {
            saveSettings()
        }

        testButton.setOnClickListener {
            testConnectivity()
        }

        // Event type filter listeners
        processAccessibilityToggle.setOnCheckedChangeListener { _, isChecked ->
            val sharedPreferences = getSharedPreferences("node_preferences", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("process_event_type_text", isChecked).apply()
            Toast.makeText(
                this,
                if (isChecked) "Accessibility events will be processed" else "Accessibility events will be skipped",
                Toast.LENGTH_SHORT
            ).show()
        }

        processOcrToggle.setOnCheckedChangeListener { _, isChecked ->
            val sharedPreferences = getSharedPreferences("node_preferences", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("process_event_type_ocr-text", isChecked).apply()
            Toast.makeText(
                this,
                if (isChecked) "OCR events will be processed" else "OCR events will be skipped",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveSettings() {
        val url = urlEditText.text.toString().trim()
        val token = tokenEditText.text.toString().trim()

        if (url.isBlank()) {
            Toast.makeText(this, "PDV URL cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            preferencesManager.setPdvBaseUrl(url)
            if (token.isNotBlank()) {
                preferencesManager.setPdvCapabilityToken(token)
            }
            Toast.makeText(this@SettingsActivity, "Settings saved", Toast.LENGTH_SHORT).show()
            statusText.text = "Saved at ${java.time.Instant.now()}"
        }
    }

    private fun testConnectivity() {
        val url = urlEditText.text.toString().trim()
        if (url.isBlank()) {
            Toast.makeText(this, "Please enter PDV URL first", Toast.LENGTH_SHORT).show()
            return
        }

        testButton.isEnabled = false
        statusText.text = "Testing connectivity..."

        lifecycleScope.launch {
            preferencesManager.setPdvBaseUrl(url)
            val result = withContext(Dispatchers.IO) {
                pdvRepository.testConnectivity()
            }
            result.onSuccess {
                statusText.text = "✓ Connected successfully"
                Toast.makeText(this@SettingsActivity, "Connection successful", Toast.LENGTH_SHORT).show()
            }
            result.onFailure { error ->
                statusText.text = "✗ Connection failed: ${error.message}"
                Toast.makeText(this@SettingsActivity, "Connection failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
            testButton.isEnabled = true
        }
    }
}
