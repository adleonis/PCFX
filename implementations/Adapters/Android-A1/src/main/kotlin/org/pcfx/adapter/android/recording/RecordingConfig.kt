package org.pcfx.adapter.android.recording

data class RecordingConfig(
    val width: Int = 1280,
    val height: Int = 720,
    val bitrate: Int = 5000000,
    val frameRate: Int = 15,
    val maxStorageSizeBytes: Long = 50L * 1024 * 1024 * 1024,
    val codec: String = "video/avc",
    val mimeType: String = "video/mp4"
)
