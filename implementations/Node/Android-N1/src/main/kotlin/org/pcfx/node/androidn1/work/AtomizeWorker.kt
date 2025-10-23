package org.pcfx.node.androidn1.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.pcfx.node.androidn1.data.PdvRepository
import org.pcfx.node.androidn1.data.RateLimitException
import org.pcfx.node.androidn1.domain.DefaultAtomizer
import org.pcfx.node.androidn1.util.PreferencesManager
import org.pcfx.node.androidn1.util.SchemaValidator

class AtomizeWorker : Service() {
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private lateinit var pdvRepository: PdvRepository
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var schemaValidator: SchemaValidator
    private val atomizer = DefaultAtomizer()

    override fun onCreate() {
        super.onCreate()
        pdvRepository = PdvRepository(this)
        preferencesManager = PreferencesManager(this)
        schemaValidator = SchemaValidator(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_RUN_ATOMIZE) {
            scope.launch {
                runAtomize()
            }
            return START_STICKY
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private suspend fun runAtomize() {
        try {
            showRunningNotification()
            Log.d(TAG, "Starting atomization job")

            val watermark = preferencesManager.getLastWatermark()
            var eventsCount = 0
            var atomsCount = 0
            var error: String? = null
            var batchCount = 0
            val maxBatches = 10  // Process max 10 batches (640 events) per run

            // Fetch events in batches
            var cursor = watermark
            while (batchCount < maxBatches) {
                val eventsResult = pdvRepository.getEventsSince(cursor, limit = 64)

                when {
                    eventsResult.isSuccess -> {
                        val events = eventsResult.getOrNull() ?: emptyList()
                        if (events.isEmpty()) {
                            Log.d(TAG, "No more events to process")
                            break
                        }

                        eventsCount += events.size
                        Log.d(TAG, "Processing ${events.size} events")

                        // Atomize events
                        val atoms = atomizer.process(events)
                        Log.d(TAG, "Generated ${atoms.size} atoms")

                        // Publish atoms
                        for (atom in atoms) {
                            val validationResult = schemaValidator.validateKnowledgeAtom(atom)
                            if (!validationResult.isValid()) {
                                Log.w(TAG, "Atom validation failed, skipping atom ${atom.id}")
                                continue
                            }

                            val postResult = pdvRepository.postAtom(atom)
                            if (postResult.isSuccess) {
                                atomsCount++
                            } else {
                                Log.e(TAG, "Failed to post atom ${atom.id}: ${postResult.exceptionOrNull()?.message}")
                            }
                        }

                        // Update watermark once per batch after all events processed
                        // Use the last event's timestamp to advance the cursor for next fetch
                        val lastEventTs = events.last().ts
                        cursor = lastEventTs
                        preferencesManager.setLastWatermark(lastEventTs)
                        batchCount++
                        Log.d(TAG, "Updated watermark to $lastEventTs (batch $batchCount/$maxBatches)")
                    }
                    eventsResult.exceptionOrNull() is RateLimitException -> {
                        val retryAfter = (eventsResult.exceptionOrNull() as? RateLimitException)?.retryAfterSeconds ?: 30
                        Log.w(TAG, "Rate limited, will retry after $retryAfter seconds")
                        error = "Rate limited (retry after $retryAfter seconds)"
                        // Don't advance watermark on rate limit
                        break
                    }
                    else -> {
                        val exception = eventsResult.exceptionOrNull()
                        Log.e(TAG, "Error fetching events", exception)
                        error = exception?.message ?: "Unknown error"
                        break
                    }
                }
            }

            if (batchCount >= maxBatches) {
                Log.i(TAG, "Reached maximum batches ($maxBatches). Run again to process remaining events.")
            }

            preferencesManager.recordRunResult(
                status = if (error == null) "success" else "partial_failure",
                eventsCount = eventsCount,
                atomsCount = atomsCount,
                error = error
            )

            Log.d(TAG, "Atomization job completed: processed $eventsCount events, published $atomsCount atoms in $batchCount batches")

        } catch (e: Exception) {
            Log.e(TAG, "Atomization job failed", e)
            preferencesManager.recordRunResult(
                status = "failed",
                eventsCount = 0,
                atomsCount = 0,
                error = e.message ?: "Unknown error"
            )
        } finally {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Atomization Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications for data atomization operations"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showRunningNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Atomizing Data")
            .setContentText("Processing exposure events...")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setProgress(0, 0, true)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "AtomizeWorker"
        private const val CHANNEL_ID = "pcfx_atomizer_channel"
        private const val NOTIFICATION_ID = 1
        const val ACTION_RUN_ATOMIZE = "org.pcfx.node.androidn1.RUN_ATOMIZE"
    }
}
