package org.pcfx.adapter.android.recording

import android.content.Context
import android.os.StatFs
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

class StorageManager(
    private val context: Context,
    private val config: RecordingConfig
) {
    private val recordingDir: File
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)

    init {
        recordingDir = File(context.cacheDir, "recordings").apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    fun getNextOutputFile(): File {
        val timestamp = dateFormat.format(Date())
        return File(recordingDir, "recording_${timestamp}.mp4")
    }

    fun getTotalUsedSpace(): Long {
        return recordingDir.walkTopDown()
            .filter { it.isFile }
            .sumOf { it.length() }
    }

    fun getAvailableSpace(): Long {
        val stat = StatFs(recordingDir.absolutePath)
        return stat.availableBytes
    }

    fun ensureSpace(): Boolean {
        val usedSpace = getTotalUsedSpace()
        val availableSpace = getAvailableSpace()
        val totalRequired = usedSpace + availableSpace

        if (usedSpace >= config.maxStorageSizeBytes) {
            cleanupOldestFiles(usedSpace - (config.maxStorageSizeBytes / 2))
            return true
        }

        return true
    }

    fun cleanup() {
        ensureSpace()
    }

    fun deleteFile(file: File): Boolean {
        return if (file.exists() && file.isFile) {
            file.delete()
        } else {
            false
        }
    }

    fun getAllRecordingFiles(): List<File> {
        return recordingDir.walkTopDown()
            .filter { it.isFile && it.extension == "mp4" }
            .sortedByDescending { it.lastModified() }
            .toList()
    }

    private fun cleanupOldestFiles(targetSize: Long) {
        val files = getAllRecordingFiles()
        var freedSpace = 0L

        for (file in files.reversed()) {
            if (freedSpace >= targetSize) break
            val fileSize = file.length()
            if (deleteFile(file)) {
                freedSpace += fileSize
            }
        }
    }

    fun getRecordingDirPath(): String = recordingDir.absolutePath
}
