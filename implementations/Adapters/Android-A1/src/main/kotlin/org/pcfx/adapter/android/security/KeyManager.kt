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
import java.security.spec.ECGenParameterSpec
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
        return try {
            val existingPrivateKey = getPrivateKey()
            val existingPublicKey = getPublicKey()

            if (existingPrivateKey != null && existingPublicKey != null) {
                KeyPair(existingPrivateKey, existingPublicKey)
            } else {
                generateNewKeyPair()
            }
        } catch (e: Exception) {
            android.util.Log.e("KeyManager", "Error retrieving or generating key pair", e)
            throw e
        }
    }

    fun regenerateKeyPair() {
        deleteCurrentKeyPair()
        generateNewKeyPair()
    }


    private fun generateNewKeyPair(): KeyManager.KeyPair {
        try {
            val keyPairGen = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC,
                KEYSTORE_PROVIDER
            )

            val calendar = Calendar.getInstance().apply {
                add(Calendar.YEAR, 5)
            }

            val spec = KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS,
                KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
            ).apply {
                setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))

                setDigests(
                    KeyProperties.DIGEST_SHA256,
                    KeyProperties.DIGEST_SHA512
                )

                setKeyValidityEnd(calendar.time)

            }.build()

            keyPairGen.initialize(spec)
            val keyPair = keyPairGen.generateKeyPair()

            val publicKeyBase64 = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
            sharedPrefs.edit().putString(PREFS_PUBLIC_KEY, publicKeyBase64).apply()

            return KeyPair(keyPair.private, keyPair.public)
        } catch (e: Exception) {
            android.util.Log.e("KeyManager", "Error generating new key pair", e)
            throw e
        }
    }


    private fun getPrivateKey(): PrivateKey? {
        return try {
            (keyStore.getEntry(KEYSTORE_ALIAS, null) as? KeyStore.PrivateKeyEntry)?.privateKey
        } catch (e: Exception) {
            android.util.Log.w("KeyManager", "Could not retrieve private key: ${e.message}")
            null
        }
    }

    private fun getPublicKey(): PublicKey? {
        return try {
            val certificate = keyStore.getCertificate(KEYSTORE_ALIAS)
            certificate?.publicKey
        } catch (e: Exception) {
            android.util.Log.w("KeyManager", "Could not retrieve public key from certificate: ${e.message}")
            try {
                val publicKeyBase64 = sharedPrefs.getString(PREFS_PUBLIC_KEY, null) ?: return null
                val decodedKey = Base64.decode(publicKeyBase64, Base64.DEFAULT)
                java.security.KeyFactory.getInstance("EC").generatePublic(
                    java.security.spec.X509EncodedKeySpec(decodedKey)
                )
            } catch (ex: Exception) {
                android.util.Log.w("KeyManager", "Could not retrieve public key from preferences: ${ex.message}")
                null
            }
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
        val publicKey = getOrGenerateKeyPair().publicKey
        return Base64.encodeToString(publicKey.encoded, Base64.DEFAULT)
    }

    data class KeyPair(
        val privateKey: PrivateKey,
        val publicKey: PublicKey
    )
}
