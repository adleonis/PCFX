package org.pcfx.adapter.android.screenshot

import android.graphics.Bitmap
import android.util.Log
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.ceil

class ScreenshotQueue(private val maxMemoryMB: Int = 100) {
    
    private data class QueuedScreenshot(
        val bitmap: Bitmap,
        val timestamp: Long,
        val estimatedSizeBytes: Long
    )

    private val queue = LinkedBlockingQueue<QueuedScreenshot>()
    private var currentMemoryUsageBytes = 0L
    private val maxMemoryBytes = maxMemoryMB * 1024L * 1024L
    private val lock = Any()

    companion object {
        private const val TAG = "ScreenshotQueue"
        private const val BYTES_PER_PIXEL = 4 // ARGB_8888
    }

    fun enqueue(bitmap: Bitmap, timestamp: Long): Boolean {
        val estimatedSize = (bitmap.width * bitmap.height * BYTES_PER_PIXEL).toLong()

        synchronized(lock) {
            if (currentMemoryUsageBytes + estimatedSize > maxMemoryBytes) {
                // Memory limit would be exceeded
                Log.w(TAG, "Queue memory limit exceeded ($currentMemoryUsageBytes + $estimatedSize > $maxMemoryBytes), purging oldest frame")
                dequeueAndDispose()

                if (currentMemoryUsageBytes + estimatedSize > maxMemoryBytes) {
                    Log.w(TAG, "Still exceeding memory after purge, rejecting new frame")
                    return false
                }
            }

            val screenshot = QueuedScreenshot(
                bitmap = bitmap,
                timestamp = timestamp,
                estimatedSizeBytes = estimatedSize
            )

            queue.offer(screenshot)
            currentMemoryUsageBytes += estimatedSize
            Log.d(TAG, "Enqueued screenshot (${queue.size} in queue, ${formatBytes(currentMemoryUsageBytes)} memory)")
            return true
        }
    }

    fun dequeue(): Pair<Bitmap, Long>? {
        return synchronized(lock) {
            val screenshot = queue.poll()
            if (screenshot != null) {
                currentMemoryUsageBytes -= screenshot.estimatedSizeBytes
                Log.d(TAG, "Dequeued screenshot (${queue.size} in queue, ${formatBytes(currentMemoryUsageBytes)} memory)")
                Pair(screenshot.bitmap, screenshot.timestamp)
            } else {
                null
            }
        }
    }

    private fun dequeueAndDispose(): Pair<Bitmap, Long>? {
        val screenshot = queue.poll()
        if (screenshot != null) {
            currentMemoryUsageBytes -= screenshot.estimatedSizeBytes
            if (!screenshot.bitmap.isRecycled) {
                screenshot.bitmap.recycle()
            }
            Log.d(TAG, "Disposed oldest screenshot (${queue.size} remaining)")
        }
        return if (screenshot != null) Pair(screenshot.bitmap, screenshot.timestamp) else null
    }

    fun clear() {
        synchronized(lock) {
            while (true) {
                val screenshot = queue.poll() ?: break
                if (!screenshot.bitmap.isRecycled) {
                    screenshot.bitmap.recycle()
                }
            }
            currentMemoryUsageBytes = 0
            Log.d(TAG, "Queue cleared")
        }
    }

    fun size(): Int = queue.size

    fun currentMemoryUsage(): Long = synchronized(lock) { currentMemoryUsageBytes }

    fun getMemoryPercentage(): Float {
        return synchronized(lock) {
            (currentMemoryUsageBytes.toFloat() / maxMemoryBytes) * 100f
        }
    }

    private fun formatBytes(bytes: Long): String {
        val mb = bytes / (1024.0 * 1024.0)
        return "%.1f MB".format(mb)
    }
}
