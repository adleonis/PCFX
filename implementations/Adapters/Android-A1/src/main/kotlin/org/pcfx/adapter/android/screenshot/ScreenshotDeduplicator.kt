package org.pcfx.adapter.android.screenshot

import android.graphics.Bitmap
import android.util.Log
import java.nio.ByteBuffer
import java.security.MessageDigest

class ScreenshotDeduplicator {

    private var lastScreenshotHash: String? = null
    private val lock = Any()

    companion object {
        private const val TAG = "ScreenshotDeduplicator"
    }

    fun computeHash(bitmap: Bitmap): String {
        val buffer = ByteBuffer.allocate(bitmap.byteCount)
        bitmap.copyPixelsToBuffer(buffer)
        val bytes = buffer.array()

        val digest = MessageDigest.getInstance("MD5")
        val hashBytes = digest.digest(bytes)
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun isDuplicate(bitmap: Bitmap): Boolean {
        val currentHash = computeHash(bitmap)
        
        return synchronized(lock) {
            val isDup = currentHash == lastScreenshotHash
            lastScreenshotHash = currentHash
            isDup
        }
    }

    fun updateLastHash(hash: String) {
        synchronized(lock) {
            lastScreenshotHash = hash
        }
    }

    fun getLastHash(): String? = synchronized(lock) { lastScreenshotHash }

    fun reset() {
        synchronized(lock) {
            lastScreenshotHash = null
            Log.d(TAG, "Deduplicator reset")
        }
    }
}
