package org.pcfx.adapter.android.security

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.util.Calendar
import java.security.spec.ECGenParameterSpec // <-- Add this import
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class KeyManager(context: Context) {
    companion object {
        private const val KEYSTORE_ALIAS = "pcfx_ed25519_key"
        private const val PREFS_NAME = "pcfx_keys"
        private const val PREFS_PUBLIC_KEY = "public_key_base64"
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
        load(null)
    }

    fun getOrGenerateKeyPair(): KeyManager.KeyPair {
        val existingPrivateKey = getPrivateKey()
        val existingPublicKey = getPublicKey()

        return if (existingPrivateKey != null && existingPublicKey != null) {
            KeyPair(existingPrivateKey, existingPublicKey)
        } else {
            generateNewKeyPair()
        }
    }

    fun regenerateKeyPair() {
        deleteCurrentKeyPair()
        generateNewKeyPair()
    }


    private fun generateNewKeyPair(): KeyManager.KeyPair {
        val keyPairGen = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC, // Use the constant for clarity
            KEYSTORE_PROVIDER
        )

        val spec = KeyGenParameterSpec.Builder(
            KEYSTORE_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).apply {
            // [FIX] Correctly specify the elliptic curve using the dedicated builder method.
            setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))

            setDigests(
                KeyProperties.DIGEST_SHA256,
                KeyProperties.DIGEST_SHA512
            )

            // [IMPROVEMENT] Use modern time API (available since API 26).
            // This is more readable and less error-prone than Calendar.
            val validityEnd = Instant.now().plus(5, ChronoUnit.YEARS)
            setKeyValidityEnd(Date.from(validityEnd))

        }.build()

        keyPairGen.initialize(spec)
        val keyPair = keyPairGen.generateKeyPair()

        val publicKeyBase64 = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
        sharedPrefs.edit().putString(PREFS_PUBLIC_KEY, publicKeyBase64).apply()

        // [IMPROVEMENT] Return the generated KeyPair object directly.
        // Creating a new one is unnecessary.
        return keyPair
    }


    private fun getPrivateKey(): PrivateKey? {
        return try {
            (keyStore.getEntry(KEYSTORE_ALIAS, null) as? KeyStore.PrivateKeyEntry)?.privateKey
        } catch (e: Exception) {
            null
        }
    }

    private fun getPublicKey(): PublicKey? {
        return try {
            val certificate = keyStore.getCertificate(KEYSTORE_ALIAS)
            certificate?.publicKey
        } catch (e: Exception) {
            val publicKeyBase64 = sharedPrefs.getString(PREFS_PUBLIC_KEY, null) ?: return null
            val decodedKey = Base64.decode(publicKeyBase64, Base64.DEFAULT)
            java.security.KeyFactory.getInstance("EC").generatePublic(
                java.security.spec.X509EncodedKeySpec(decodedKey)
            )
        }
    }

    private fun deleteCurrentKeyPair() {
        try {
            keyStore.deleteEntry(KEYSTORE_ALIAS)
            sharedPrefs.edit().remove(PREFS_PUBLIC_KEY).apply()
        } catch (e: Exception) {
            // Key doesn't exist or already deleted
        }
    }

    fun getPublicKeyBase64(): String {
        val publicKey = getOrGenerateKeyPair().public
        return Base64.encodeToString(publicKey.encoded, Base64.DEFAULT)
    }

    data class KeyPair(
        val privateKey: PrivateKey,
        val publicKey: PublicKey
    )
}
