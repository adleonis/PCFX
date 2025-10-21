package org.pcfx.adapter.android.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.pcfx.adapter.android.R
import org.pcfx.adapter.android.consent.ConsentManager
import org.pcfx.adapter.android.model.ConsentManifestBuilder
import org.pcfx.adapter.android.security.KeyManager
import org.pcfx.adapter.android.service.EventPublisherService

class ConsentActivity : AppCompatActivity() {
    private lateinit var consentManager: ConsentManager
    private lateinit var keyManager: KeyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_consent)

            consentManager = ConsentManager(this)
            keyManager = KeyManager(this)

            setupUI()

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

        // Update button states based on current consent
        updateButtonStates()
    }

    private fun updateButtonStates() {
        val acceptButton = findViewById<Button>(R.id.consent_accept_btn)
        val declineButton = findViewById<Button>(R.id.consent_decline_btn)
        val statusButton = findViewById<Button>(R.id.consent_status_btn)

        if (consentManager.isConsentActive()) {
            acceptButton.text = "Consent Granted"
            acceptButton.isEnabled = false
            declineButton.text = "Revoke Consent"
            declineButton.isEnabled = true
            statusButton.isEnabled = true
        } else {
            acceptButton.text = "Accept Consent"
            acceptButton.isEnabled = true
            declineButton.text = "Decline"
            declineButton.isEnabled = false
            statusButton.isEnabled = false
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
}
