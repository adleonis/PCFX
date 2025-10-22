package org.pcfx.adapter.android.recording

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.io.File
import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

data class VideoChunk(
    val sequenceNumber: Int,
    val file: File,
    val startTimestamp: Long,
    var endTimestamp: Long = 0,
    var frameCount: Int = 0,
    var isFinalized: Boolean = false,
    var isUploaded: Boolean = false
) {
    val durationMs: Long
        get() = endTimestamp - startTimestamp

    fun toMetadata(): Map<String, Any> = mapOf(
        "sequenceNumber" to sequenceNumber,
        "startTimestamp" to startTimestamp,
        "endTimestamp" to endTimestamp,
        "durationMs" to durationMs,
        "frameCount" to frameCount,
        "filePath" to file.absolutePath,
        "fileName" to file.name,
        "fileSizeBytes" to file.length()
    )
}

class VideoChunkManager(
    private val context: Context,
    private val recordingDir: File,
    private val targetChunkDurationMs: Long = 5000 // 5 seconds
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "pcfx_video_chunks",
        Context.MODE_PRIVATE
    )

    private var currentChunk: VideoChunk? = null
    private val chunkSequence = AtomicLong(prefs.getLong(PREF_CHUNK_SEQUENCE, 0L))
    private val chunkList = mutableListOf<VideoChunk>()
    private val pendingUploadQueue = mutableListOf<VideoChunk>()

    init {
        ensureRecordingDir()
    }

    private fun ensureRecordingDir() {
        if (!recordingDir.exists()) {
            recordingDir.mkdirs()
        }
    }

    fun createNewChunk(): VideoChunk {
        val sequence = chunkSequence.getAndIncrement()
        val fileName = "chunk_${sequence.toString().padStart(6, '0')}.mp4"
        val file = File(recordingDir, fileName)

        val chunk = VideoChunk(
            sequenceNumber = sequence.toInt(),
            file = file,
            startTimestamp = System.currentTimeMillis()
        )

        currentChunk = chunk
        chunkList.add(chunk)
        prefs.edit().putLong(PREF_CHUNK_SEQUENCE, chunkSequence.get()).apply()

        Log.d(TAG, "Created chunk $sequence: ${file.absolutePath}")
        return chunk
    }

    fun getCurrentChunk(): VideoChunk? = currentChunk

    fun finalizeCurrentChunk(frameCount: Int = 0) {
        val chunk = currentChunk ?: return
        chunk.endTimestamp = System.currentTimeMillis()
        chunk.frameCount = frameCount
        chunk.isFinalized = true

        pendingUploadQueue.add(chunk)
        currentChunk = null

        Log.d(TAG, "Finalized chunk ${chunk.sequenceNumber}: ${chunk.file.name}, duration: ${chunk.durationMs}ms, frames: ${frameCount}")
    }

    fun getPendingChunks(): List<VideoChunk> = pendingUploadQueue.toList()

    fun markChunkAsUploaded(sequenceNumber: Int) {
        val chunk = chunkList.firstOrNull { it.sequenceNumber == sequenceNumber } ?: return
        chunk.isUploaded = true
        pendingUploadQueue.remove(chunk)

        if (chunk.file.exists()) {
            chunk.file.delete()
        }

        Log.d(TAG, "Marked chunk $sequenceNumber as uploaded and deleted local file")
    }

    fun deleteChunk(sequenceNumber: Int) {
        val chunk = chunkList.firstOrNull { it.sequenceNumber == sequenceNumber } ?: return
        if (chunk.file.exists()) {
            chunk.file.delete()
        }
        chunkList.remove(chunk)
        pendingUploadQueue.remove(chunk)

        Log.d(TAG, "Deleted chunk $sequenceNumber")
    }

    fun cleanupOldChunks(maxStorageBytes: Long) {
        var totalSize = getTotalChunkSize()
        val chunks = chunkList.filter { it.isUploaded || it.isFinalized }.sortedBy { it.sequenceNumber }

        for (chunk in chunks) {
            if (totalSize <= maxStorageBytes) break
            val fileSize = chunk.file.length()
            deleteChunk(chunk.sequenceNumber)
            totalSize -= fileSize
        }
    }

    fun getTotalChunkSize(): Long {
        return chunkList.sumOf { it.file.length() }
    }

    fun getAllChunks(): List<VideoChunk> = chunkList.toList()

    fun getChunkBySequence(sequenceNumber: Int): VideoChunk? {
        return chunkList.firstOrNull { it.sequenceNumber == sequenceNumber }
    }

    fun resetForNewRecording() {
        currentChunk = null
        chunkList.clear()
        pendingUploadQueue.clear()
    }

    companion object {
        private const val TAG = "VideoChunkManager"
        private const val PREF_CHUNK_SEQUENCE = "chunk_sequence"
    }
}
