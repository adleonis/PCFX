package org.pcfx.adapter.android.recording

import android.content.Context
import android.media.projection.MediaProjection
import java.util.concurrent.atomic.AtomicBoolean

class RecordingThread(
    private val outputFile: java.io.File,
    private val mediaProjection: MediaProjection,
    private val config: RecordingConfig,
    private val stateManager: RecordingStateManager,
    private val context: Context
) : Thread() {
    private val isRunning = AtomicBoolean(false)
    private val isPaused = AtomicBoolean(false)
    private val pauseLock = Object()

    override fun run() {
        isRunning.set(true)

        try {
            val encoder = VideoEncoder(outputFile, config)
            if (!encoder.initialize()) {
                stateManager.setState(RecordingState.Error("Failed to initialize encoder"))
                return
            }

            val screenCapture = ScreenCaptureManager(
                context,
                mediaProjection,
                config
            )

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

            while (isRunning.get()) {
                if (isPaused.get()) {
                    synchronized(pauseLock) {
                        while (isPaused.get() && isRunning.get()) {
                            pauseLock.wait()
                        }
                    }
                }

                encoder.drainEncoder(false)
                sleep(33)
            }

            encoder.drainEncoder(true)
            screenCapture.releaseVirtualDisplay()
            encoder.stop()

            android.util.Log.d("RecordingThread", "Recording completed successfully")
            stateManager.setState(RecordingState.Idle)
        } catch (e: Exception) {
            android.util.Log.e("RecordingThread", "Error during recording", e)
            stateManager.setState(RecordingState.Error("Recording failed: ${e.message}", e))
        } finally {
            isRunning.set(false)
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
}
