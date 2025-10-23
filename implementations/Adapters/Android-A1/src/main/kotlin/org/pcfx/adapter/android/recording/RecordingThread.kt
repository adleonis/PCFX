package org.pcfx.adapter.android.recording

import android.content.Context
import android.media.projection.MediaProjection
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

class RecordingThread(
    private val recordingDir: java.io.File,
    private val mediaProjection: MediaProjection,
    private val config: RecordingConfig,
    private val stateManager: RecordingStateManager,
    private val context: Context
) : Thread() {
    private val isRunning = AtomicBoolean(false)
    private val isPaused = AtomicBoolean(false)
    private val pauseLock = Object()
    private lateinit var chunkManager: VideoChunkManager
    
    private val targetFramesPerChunk = (config.frameRate * 5) // 5 seconds at target frame rate

    override fun run() {
        isRunning.set(true)
        chunkManager = VideoChunkManager(context, recordingDir, targetChunkDurationMs = 5000)
        chunkManager.resetForNewRecording()

        var screenCapture: ScreenCaptureManager? = null
        var encoder: VideoEncoder? = null

        try {
            screenCapture = ScreenCaptureManager(
                context,
                mediaProjection,
                config
            )

            val primaryChunk = chunkManager.createNewChunk()

            encoder = VideoEncoder(primaryChunk.file, config)
            if (!encoder.initialize()) {
                stateManager.setState(RecordingState.Error("Failed to initialize encoder"))
                encoder.stop()
                return
            }

            val inputSurface = encoder.getInputSurface()
            if (inputSurface == null) {
                stateManager.setState(RecordingState.Error("Failed to get encoder input surface"))
                encoder.stop()
                return
            }

            val virtualDisplay = screenCapture.createVirtualDisplay(inputSurface)
            if (virtualDisplay == null) {
                stateManager.setState(RecordingState.Error("Failed to create virtual display"))
                encoder.stop()
                return
            }

            var frameCount = 0

            while (isRunning.get()) {
                if (isPaused.get()) {
                    synchronized(pauseLock) {
                        while (isPaused.get() && isRunning.get()) {
                            pauseLock.wait()
                        }
                    }
                }

                encoder?.let { enc ->
                    frameCount += enc.drainEncoderForChunk(endOfChunk = false)
                } ?: run {
                    stateManager.setState(RecordingState.Error("Encoder not initialized"))
                    return
                }

                sleep(33)
            }

            encoder?.let { enc ->
                enc.drainEncoderForChunk(endOfChunk = true)
                enc.stop()
                chunkManager.finalizeCurrentChunk(frameCount)
                Log.d(TAG, "Finalized recording with $frameCount total frames")
            }

            screenCapture?.releaseVirtualDisplay()

            Log.d(TAG, "Recording completed successfully. Total chunks: ${chunkManager.getAllChunks().size}")
            stateManager.setState(RecordingState.Idle)
        } catch (e: Exception) {
            Log.e(TAG, "Error during recording", e)
            stateManager.setState(RecordingState.Error("Recording failed: ${e.message}", e))
        } finally {
            encoder?.stop()
            screenCapture?.releaseVirtualDisplay()
            isRunning.set(false)
        }
    }

    private fun finalizeChunkAndStartNew(encoder: VideoEncoder, chunkManager: VideoChunkManager) {
        try {
            val frameCount = encoder.finalizeChunk()
            chunkManager.finalizeCurrentChunk(frameCount)
            Log.d(TAG, "Finalized chunk with $frameCount frames")
        } catch (e: Exception) {
            Log.e(TAG, "Error finalizing chunk", e)
        }
    }

    fun stopRecording() {
        isRunning.set(false)
        resumeRecording()
        join(5000)
    }

    fun pauseRecording() {
        isPaused.set(true)
        stateManager.setState(RecordingState.Paused)
    }

    fun resumeRecording() {
        synchronized(pauseLock) {
            isPaused.set(false)
            stateManager.setState(RecordingState.Recording)
            pauseLock.notifyAll()
        }
    }

    fun getChunkManager(): VideoChunkManager = chunkManager

    companion object {
        private const val TAG = "RecordingThread"
    }
}
