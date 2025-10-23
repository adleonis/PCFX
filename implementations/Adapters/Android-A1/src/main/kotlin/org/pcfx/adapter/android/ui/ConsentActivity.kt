package org.pcfx.adapter.android.ui

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.pcfx.adapter.android.R
import org.pcfx.adapter.android.consent.ConsentManager
import org.pcfx.adapter.android.model.ConsentManifestBuilder
import org.pcfx.adapter.android.network.PDVClient
import org.pcfx.adapter.android.recording.VideoRecordingHelper
import org.pcfx.adapter.android.screenshot.ScreenshotCaptureManager
import org.pcfx.adapter.android.security.KeyManager
import org.pcfx.adapter.android.service.EventPublisherService

class ConsentActivity : AppCompatActivity() {
    private lateinit var consentManager: ConsentManager
    private lateinit var keyManager: KeyManager
    private lateinit var recordingHelper: VideoRecordingHelper
    private lateinit var screenshotCaptureManager: ScreenshotCaptureManager
    private lateinit var pdvClient: PDVClient
    private lateinit var pdvStatusIcon: ImageView
    private lateinit var pdvStatusText: TextView
    private var isRecording = false
    private var isScreenshotCapturing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_consent)

            consentManager = ConsentManager(this)
            keyManager = KeyManager(this)
            recordingHelper = VideoRecordingHelper(this)
            screenshotCaptureManager = ScreenshotCaptureManager(this)
            pdvClient = PDVClient(this)

            setupUI()
            checkAccessibilityServiceStatus()
            checkPdvConnectivity()

            // Generate or retrieve keypair on first launch
            try {
                keyManager.getOrGenerateKeyPair()
            } catch (e: Exception) {
                Log.e("ConsentActivity", "Error during key pair generation", e)
                Toast.makeText(
                    this,
                    "Error initializing security: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Log.e("ConsentActivity", "Fatal error in onCreate", e)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        checkAccessibilityServiceStatus()
        checkPdvConnectivity()
        updateButtonStates()
    }

    private fun setupUI() {
        val titleTextView = findViewById<TextView>(R.id.consent_title) ?: run {
            Log.e("ConsentActivity", "Missing required view: consent_title")
            return
        }
        val descriptionTextView = findViewById<TextView>(R.id.consent_description) ?: run {
            Log.e("ConsentActivity", "Missing required view: consent_description")
            return
        }
        val grantsTextView = findViewById<TextView>(R.id.consent_grants) ?: run {
            Log.e("ConsentActivity", "Missing required view: consent_grants")
            return
        }
        val acceptButton = findViewById<Button>(R.id.consent_accept_btn) ?: run {
            Log.e("ConsentActivity", "Missing required view: consent_accept_btn")
            return
        }
        val declineButton = findViewById<Button>(R.id.consent_decline_btn) ?: run {
            Log.e("ConsentActivity", "Missing required view: consent_decline_btn")
            return
        }
        val statusButton = findViewById<Button>(R.id.consent_status_btn) ?: run {
            Log.e("ConsentActivity", "Missing required view: consent_status_btn")
            return
        }
        val regenerateKeyButton = findViewById<Button>(R.id.regenerate_key_btn) ?: run {
            Log.e("ConsentActivity", "Missing required view: regenerate_key_btn")
            return
        }
        val accessibilityStatusText = findViewById<TextView>(R.id.accessibility_status_text)
        val enableAccessibilityButton = findViewById<Button>(R.id.enable_accessibility_button)

        titleTextView.text = "PCF-X Adapter: Privacy & Consent Request"

        val description = """
            |The PCF-X Adapter monitors your device activity to track exposure to information.
            |This data is stored locally and only shared with your explicit consent.
            |
            |What will be captured:
        """.trimMargin()
        descriptionTextView.text = description

        // Build the default consent manifest to show user
        val defaultConsent = ConsentManifestBuilder.default(
            adapterId = "org.pcfx.adapter.android/0.1.0"
        )

        val grantsText = defaultConsent.grants.joinToString("\n\n") { grant ->
            """
            |📋 ${grant.cap}
            |Purpose: ${grant.purpose}
            |Data retention: ${grant.retentionDays} days
            """.trimMargin()
        }
        grantsTextView.text = grantsText

        acceptButton.setOnClickListener {
            acceptConsent()
        }

        declineButton.setOnClickListener {
            declineConsent()
        }

        statusButton.setOnClickListener {
            showConsentStatus()
        }

        regenerateKeyButton.setOnClickListener {
            regenerateKeyPair()
        }

        enableAccessibilityButton?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        val debugButton = findViewById<Button>(R.id.debug_button)
        debugButton?.setOnClickListener {
            val intent = Intent(this, DebugExposureEventsActivity::class.java)
            startActivity(intent)
        }

        val videoRecordingButton = findViewById<Button>(R.id.video_recording_button)
        videoRecordingButton?.setOnClickListener {
            if (isScreenshotCapturing) {
                Toast.makeText(
                    this,
                    "Stop screenshot capture first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                toggleVideoRecording()
            }
        }

        setupScreenshotCaptureUI()

        pdvStatusIcon = findViewById(R.id.pdv_status_icon)
        pdvStatusText = findViewById(R.id.pdv_status_text)

        // Update button states based on current consent
        updateButtonStates()
    }

    private fun setupScreenshotCaptureUI() {
        val screenshotIntervalSpinner = findViewById<android.widget.Spinner>(R.id.screenshot_interval_spinner)
        val screenshotToggle = findViewById<android.widget.Switch>(R.id.screenshot_toggle)

        if (screenshotIntervalSpinner == null || screenshotToggle == null) {
            Log.w("ConsentActivity", "Screenshot UI views not found in layout")
            return
        }

        val intervalOptions = arrayOf("1 sec", "2 sec", "3 sec", "4 sec", "5 sec")
        val adapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            intervalOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        screenshotIntervalSpinner.adapter = adapter

        val currentInterval = screenshotCaptureManager.getInterval()
        screenshotIntervalSpinner.setSelection(currentInterval - 1)

        screenshotIntervalSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val interval = position + 1
                screenshotCaptureManager.setInterval(interval)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        isScreenshotCapturing = screenshotCaptureManager.isCapturing()
        updateScreenshotToggle(screenshotToggle)

        screenshotToggle.setOnCheckedChangeListener { _, isChecked ->
            if (!consentManager.isConsentActive()) {
                Toast.makeText(
                    this,
                    "Please grant consent before capturing screenshots",
                    Toast.LENGTH_SHORT
                ).show()
                screenshotToggle.isChecked = false
                return@setOnCheckedChangeListener
            }

            if (isChecked && isRecording) {
                Toast.makeText(
                    this,
                    "Stop video recording first",
                    Toast.LENGTH_SHORT
                ).show()
                screenshotToggle.isChecked = false
                return@setOnCheckedChangeListener
            }

            if (isChecked) {
                startScreenshotCapture()
            } else {
                stopScreenshotCapture()
            }
        }
    }

    private fun startScreenshotCapture() {
        val activeConsent = consentManager.getActiveConsent() ?: run {
            Toast.makeText(this, "No active consent found", Toast.LENGTH_SHORT).show()
            return
        }

        val interval = screenshotCaptureManager.getInterval()

        try {
            screenshotCaptureManager.startCapture(
                intervalSeconds = interval,
                consentId = activeConsent.consentId,
                retentionDays = activeConsent.grants.firstOrNull()?.retentionDays ?: 30
            )
            isScreenshotCapturing = true
            updateScreenshotToggle(findViewById(R.id.screenshot_toggle))
            Toast.makeText(this, "Screenshot capture started", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("ConsentActivity", "Error starting screenshot capture", e)
            Toast.makeText(this, "Error starting screenshot capture: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopScreenshotCapture() {
        try {
            screenshotCaptureManager.stopCapture()
            isScreenshotCapturing = false
            updateScreenshotToggle(findViewById(R.id.screenshot_toggle))
            Toast.makeText(this, "Screenshot capture stopped", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("ConsentActivity", "Error stopping screenshot capture", e)
            Toast.makeText(this, "Error stopping screenshot capture: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateScreenshotToggle(toggle: android.widget.Switch?) {
        toggle?.isChecked = isScreenshotCapturing
    }

    private fun checkAccessibilityServiceStatus() {
        val accessibilityManager = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        val accessibilityServiceEnabled = enabledServices.any {
            it.resolveInfo.serviceInfo.packageName == packageName
        }

        val statusText = findViewById<TextView>(R.id.accessibility_status_text)
        val enableButton = findViewById<Button>(R.id.enable_accessibility_button)

        if (accessibilityServiceEnabled) {
            statusText?.visibility = android.view.View.GONE
            enableButton?.visibility = android.view.View.GONE
        } else {
            val statusMessage = "⚠️ To capture app usage events, please enable the Accessibility Service:\n1. Tap 'Enable Accessibility Service'\n2. Find 'PCF-X Adapter' in the list\n3. Toggle it ON"
            statusText?.text = statusMessage
            statusText?.visibility = android.view.View.VISIBLE
            enableButton?.visibility = android.view.View.VISIBLE
        }
    }

    private fun updateButtonStates() {
        val acceptButton = findViewById<Button>(R.id.consent_accept_btn)
        val declineButton = findViewById<Button>(R.id.consent_decline_btn)
        val statusButton = findViewById<Button>(R.id.consent_status_btn)
        val videoRecordingButton = findViewById<Button>(R.id.video_recording_button)
        val screenshotIntervalSpinner = findViewById<android.widget.Spinner>(R.id.screenshot_interval_spinner)
        val screenshotToggle = findViewById<android.widget.Switch>(R.id.screenshot_toggle)

        if (consentManager.isConsentActive()) {
            acceptButton.text = "Consent Granted"
            acceptButton.isEnabled = false
            declineButton.text = "Revoke Consent"
            declineButton.isEnabled = true
            statusButton.isEnabled = true
            videoRecordingButton?.isEnabled = true
            screenshotIntervalSpinner?.isEnabled = true
            screenshotToggle?.isEnabled = true
        } else {
            acceptButton.text = "Accept Consent"
            acceptButton.isEnabled = true
            declineButton.text = "Decline"
            declineButton.isEnabled = false
            statusButton.isEnabled = false
            videoRecordingButton?.isEnabled = false
            videoRecordingButton?.text = "Start Recording (Require Consent)"
            isRecording = false
            screenshotIntervalSpinner?.isEnabled = false
            screenshotToggle?.isEnabled = false
            screenshotToggle?.isChecked = false
            isScreenshotCapturing = false
        }

        updateVideoRecordingButtonText()
    }

    private fun toggleVideoRecording() {
        if (!consentManager.isConsentActive()) {
            Toast.makeText(
                this,
                "Please grant consent before recording",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!isRecording) {
            recordingHelper.requestScreenCapturePermission(this)
        } else {
            recordingHelper.stopRecording()
            isRecording = false
            updateVideoRecordingButtonText()
        }
    }

    private fun updateVideoRecordingButtonText() {
        val videoRecordingButton = findViewById<Button>(R.id.video_recording_button)
        videoRecordingButton?.text = if (isRecording) "Stop Recording" else "Start Recording"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VideoRecordingHelper.REQUEST_MEDIA_PROJECTION) {
            recordingHelper.handleScreenCaptureResult(resultCode, data)
            isRecording = true
            updateVideoRecordingButtonText()
        }
    }

    private fun acceptConsent() {
        try {
            val defaultConsent = ConsentManifestBuilder.default(
                adapterId = "org.pcfx.adapter.android/0.1.0"
            )

            consentManager.saveConsent(defaultConsent)

            Toast.makeText(
                this,
                "Consent granted. Event publishing started.",
                Toast.LENGTH_SHORT
            ).show()

            // Start event publisher
            val intent = Intent(this, EventPublisherService::class.java)
            intent.action = EventPublisherService.ACTION_PUBLISH_QUEUED_EVENTS
            startService(intent)

            updateButtonStates()
        } catch (e: Exception) {
            Toast.makeText(this, "Error granting consent: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun declineConsent() {
        consentManager.revokeConsent()
        Toast.makeText(
            this,
            "Consent revoked. Event publishing stopped.",
            Toast.LENGTH_SHORT
        ).show()
        updateButtonStates()
    }

    private fun showConsentStatus() {
        val statusText = consentManager.formatConsentForDisplay()
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Current Consent Status")
            .setMessage(statusText)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }

    private fun regenerateKeyPair() {
        try {
            keyManager.regenerateKeyPair()
            Toast.makeText(
                this,
                "Key pair regenerated successfully.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error regenerating key pair: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkPdvConnectivity() {
        lifecycleScope.launch {
            pdvStatusText.text = "Checking PDV connection..."
            pdvStatusIcon.setImageResource(android.R.drawable.ic_dialog_info)
            pdvStatusIcon.setColorFilter(Color.GRAY)

            val result = withContext(Dispatchers.IO) {
                pdvClient.testConnectivity()
            }

            when (result) {
                is PDVClient.Result.Success -> {
                    pdvStatusIcon.setImageResource(android.R.drawable.ic_input_get)
                    pdvStatusIcon.setColorFilter(Color.GREEN)
                    pdvStatusText.text = "✓ PDV Server Connected"
                    pdvStatusText.setTextColor(Color.GREEN)
                }
                is PDVClient.Result.Failure -> {
                    pdvStatusIcon.setImageResource(android.R.drawable.ic_dialog_alert)
                    pdvStatusIcon.setColorFilter(Color.RED)
                    pdvStatusText.text = "✗ PDV Server Disconnected\n${result.message}"
                    pdvStatusText.setTextColor(Color.RED)
                }
            }
        }
    }
}
