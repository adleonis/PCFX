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
                runAtomize(intent)
            }
            return START_STICKY
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private suspend fun runAtomize(intent: Intent?) {
        try {
            showRunningNotification()
            Log.d(TAG, "========== ATOMIZATION JOB START ==========")
            Log.d(TAG, "Starting atomization job")

            val maxBatches = intent?.getIntExtra(EXTRA_MAX_BATCHES, DEFAULT_MAX_BATCHES) ?: DEFAULT_MAX_BATCHES
            val isProcessAll = maxBatches == Int.MAX_VALUE
            Log.d(TAG, if (isProcessAll) "Mode: Process ALL events" else "Mode: Process max $maxBatches batches")

            val watermark = preferencesManager.getLastWatermark()
            var eventsCount = 0
            var atomsCount = 0
            var error: String? = null
            var batchCount = 0
            val startTime = System.currentTimeMillis()
            val maxExecutionTimeMs = 30 * 60 * 1000L  // 30 minute limit for Process All
            var emptyBatchCount = 0  // Track consecutive empty batches

            // Event type statistics
            val eventTypeStats = mutableMapOf<String, Int>()
            val contentKindStats = mutableMapOf<String, Int>()

            // Fetch events in batches
            var cursor = watermark
            Log.d(TAG, "Starting from watermark: $watermark")

            while (batchCount < maxBatches) {
                // Check execution time limit for Process All mode
                if (isProcessAll) {
                    val elapsedMs = System.currentTimeMillis() - startTime
                    if (elapsedMs > maxExecutionTimeMs) {
                        Log.i(TAG, "⚠️ Process All exceeded 30-minute time limit. Stopping to prevent infinite processing.")
                        error = "Execution time limit exceeded (30 minutes)"
                        break
                    }
                }

                val eventsResult = pdvRepository.getEventsSince(cursor, limit = 64)

                when {
                    eventsResult.isSuccess -> {
                        val events = eventsResult.getOrNull() ?: emptyList()
                        if (events.isEmpty()) {
                            emptyBatchCount++
                            if (isProcessAll && emptyBatchCount >= 2) {
                                Log.d(TAG, "✓ No more events to process (${emptyBatchCount} consecutive empty batches)")
                                break
                            } else if (!isProcessAll) {
                                Log.d(TAG, "✓ No more events to process")
                                break
                            }
                            // For Process All mode, skip this iteration if empty and continue checking
                            continue
                        }

                        emptyBatchCount = 0  // Reset empty batch counter on successful fetch

                        Log.d(TAG, "\n========== BATCH ${batchCount + 1}/$maxBatches ==========")
                        Log.d(TAG, "Processing ${events.size} events")

                        // Analyze event types
                        var accessibilityEventCount = 0
                        var ocrEventCount = 0
                        var otherEventCount = 0

                        events.forEach { event ->
                            val source = event.source?.surface ?: "unknown"
                            val contentKind = event.content?.kind ?: "unknown"

                            eventTypeStats[source] = eventTypeStats.getOrDefault(source, 0) + 1
                            contentKindStats[contentKind] = contentKindStats.getOrDefault(contentKind, 0) + 1

                            when (contentKind) {
                                "text" -> accessibilityEventCount++
                                "ocr-text" -> ocrEventCount++
                                else -> otherEventCount++
                            }
                        }

                        Log.d(TAG, "Raw event breakdown for this batch:")
                        Log.d(TAG, "  - Accessibility events (text): $accessibilityEventCount")
                        Log.d(TAG, "  - Screenshot events (ocr-text): $ocrEventCount")
                        Log.d(TAG, "  - Other events: $otherEventCount")
                        Log.d(TAG, "Event sources: $eventTypeStats")
                        Log.d(TAG, "Content kinds: $contentKindStats")

                        // Filter events by content kind based on user preferences
                        val preferences = getApplicationContext().getSharedPreferences("node_preferences", android.content.Context.MODE_PRIVATE)
                        val processAccessibilityEvents = preferences.getBoolean("process_event_type_text", false)
                        val processOcrEvents = preferences.getBoolean("process_event_type_ocr-text", true)

                        Log.d(TAG, "Processing filters applied:")
                        Log.d(TAG, "  - Process accessibility (text) events: $processAccessibilityEvents")
                        Log.d(TAG, "  - Process OCR (ocr-text) events: $processOcrEvents")

                        val filteredEvents = events.filter { event ->
                            when (event.content?.kind) {
                                "text" -> processAccessibilityEvents
                                "ocr-text" -> processOcrEvents
                                else -> false
                            }
                        }

                        if (filteredEvents.size < events.size) {
                            val filtered = events.size - filteredEvents.size
                            Log.d(
                                TAG,
                                "📊 Filtering summary: ${events.size} events → ${filteredEvents.size} kept, $filtered discarded"
                            )
                            Log.d(
                                TAG,
                                "  - Accessibility events filtered: ${if (processAccessibilityEvents) 0 else accessibilityEventCount} of $accessibilityEventCount"
                            )
                            Log.d(
                                TAG,
                                "  - OCR events filtered: ${if (processOcrEvents) 0 else ocrEventCount} of $ocrEventCount"
                            )
                        }

                        // Only count filtered events that are actually being processed
                        eventsCount += filteredEvents.size
                        Log.d(TAG, "✓ Processing ${filteredEvents.size} filtered events (discarded ${events.size - filteredEvents.size})")

                        // Atomize events
                        val atoms = atomizer.process(filteredEvents)
                        Log.d(TAG, "Generated ${atoms.size} atoms from ${filteredEvents.size} events")

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
                        // API returns events in ASCENDING timestamp order (oldest first, newest last)
                        // Use the LAST (newest) event's timestamp to advance the cursor for next fetch
                        val oldestEventTs = events.first().ts
                        val newestEventTs = events.last().ts
                        cursor = newestEventTs
                        preferencesManager.setLastWatermark(newestEventTs)
                        batchCount++
                        Log.d(TAG, "Updated watermark from $oldestEventTs to $newestEventTs (batch $batchCount/$maxBatches)")
                        Log.d(TAG, "Next fetch will start from: $newestEventTs")
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

            if (batchCount >= maxBatches && maxBatches != Int.MAX_VALUE) {
                Log.i(TAG, "⚠️ Reached maximum batches ($maxBatches). Run again to process remaining events.")
            } else if (maxBatches == Int.MAX_VALUE) {
                Log.i(TAG, "✓ Process All completed - all available events have been processed.")
            }

            preferencesManager.recordRunResult(
                status = if (error == null) "success" else "partial_failure",
                eventsCount = eventsCount,
                atomsCount = atomsCount,
                error = error
            )

            Log.d(TAG, "\n========== ATOMIZATION JOB SUMMARY ==========")
            Log.d(TAG, "📊 Events processed:")
            Log.d(TAG, "  - Total events fetched: $eventsCount")
            Log.d(TAG, "  - Total atoms published: $atomsCount")
            Log.d(TAG, "  - Batches processed: $batchCount/$maxBatches")
            Log.d(TAG, "  - Event success rate: ${if (eventsCount > 0) "${(atomsCount * 100 / eventsCount)}%" else "N/A"}")

            Log.d(TAG, "📈 Event breakdown by source: $eventTypeStats")
            Log.d(TAG, "📋 Event breakdown by kind: $contentKindStats")

            if (eventTypeStats.isNotEmpty()) {
                val accessibilityCount = contentKindStats["text"] ?: 0
                val ocrCount = contentKindStats["ocr-text"] ?: 0
                if (accessibilityCount > 0 || ocrCount > 0) {
                    Log.d(TAG, "🔍 Detailed breakdown:")
                    Log.d(TAG, "  - Accessibility events (text): $accessibilityCount")
                    Log.d(TAG, "  - Screenshot events (ocr-text): $ocrCount")
                }
            }

            if (error != null) {
                Log.w(TAG, "⚠️ Error: $error")
            } else {
                Log.d(TAG, "✓ Atomization completed successfully")
            }
            Log.d(TAG, "========== JOB COMPLETED ==========\n")

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
        const val EXTRA_MAX_BATCHES = "max_batches"
        private const val DEFAULT_MAX_BATCHES = 10
    }
}
