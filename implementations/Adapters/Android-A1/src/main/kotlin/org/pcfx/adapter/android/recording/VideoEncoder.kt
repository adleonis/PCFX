package org.pcfx.adapter.android.recording

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.view.Surface
import java.io.File
import java.nio.ByteBuffer

class VideoEncoder(
    private val outputFile: File,
    private val config: RecordingConfig
) {
    private lateinit var mediaCodec: MediaCodec
    private lateinit var mediaMuxer: MediaMuxer
    private lateinit var encoderSurface: Surface
    private var isInitialized = false
    private var isFormatSet = false
    private var videoTrackIndex = -1
    private var frameCount = 0

    fun initialize(): Boolean {
        return try {
            val mediaFormat = MediaFormat.createVideoFormat(
                config.codec,
                config.width,
                config.height
            ).apply {
                setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
                setInteger(MediaFormat.KEY_BIT_RATE, config.bitrate)
                setInteger(MediaFormat.KEY_FRAME_RATE, config.frameRate)
                setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
            }

            mediaCodec = MediaCodec.createEncoderByType(config.codec)
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            encoderSurface = mediaCodec.createInputSurface()
            mediaCodec.start()

            mediaMuxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            isInitialized = true

            android.util.Log.d(TAG, "Encoder initialized for chunk: ${outputFile.absolutePath}")
            true
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error initializing encoder", e)
            false
        }
    }

    fun getInputSurface(): Surface? {
        return if (isInitialized) encoderSurface else null
    }

    fun drainEncoderForChunk(endOfChunk: Boolean = false): Int {
        if (!isInitialized) return frameCount

        if (endOfChunk) {
            mediaCodec.signalEndOfInputStream()
        }

        val bufferInfo = MediaCodec.BufferInfo()
        var outputBufferIndex: Int

        while (true) {
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000)

            if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                continue
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                val outputFormat = mediaCodec.outputFormat
                if (!isFormatSet) {
                    videoTrackIndex = mediaMuxer.addTrack(outputFormat)
                    mediaMuxer.start()
                    isFormatSet = true
                    android.util.Log.d(TAG, "Output format set for chunk: $outputFormat")
                }
                continue
            } else if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                if (!endOfChunk) break
            } else if (outputBufferIndex >= 0) {
                val outputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex)
                if (outputBuffer != null && isFormatSet) {
                    outputBuffer.position(bufferInfo.offset)
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size)

                    if (bufferInfo.size > 0) {
                        mediaMuxer.writeSampleData(videoTrackIndex, outputBuffer, bufferInfo)
                        frameCount++
                    }
                }

                mediaCodec.releaseOutputBuffer(outputBufferIndex, false)

                if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break
                }
            }
        }

        return frameCount
    }

    fun finalizeChunk(): Int {
        if (!isInitialized) return frameCount

        try {
            drainEncoderForChunk(endOfChunk = true)
            
            if (isFormatSet) {
                mediaMuxer.stop()
                mediaMuxer.release()
            }

            isInitialized = false
            isFormatSet = false
            videoTrackIndex = -1

            val finalFrameCount = frameCount
            frameCount = 0

            android.util.Log.d(TAG, "Chunk finalized. Frames in chunk: $finalFrameCount, File: ${outputFile.absolutePath}")
            return finalFrameCount
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error finalizing chunk", e)
            return frameCount
        }
    }

    fun stop() {
        if (isInitialized) {
            try {
                mediaCodec.stop()
                mediaCodec.release()
                if (isFormatSet) {
                    mediaMuxer.stop()
                    mediaMuxer.release()
                }
                isInitialized = false
                isFormatSet = false
                android.util.Log.d(TAG, "Encoder stopped completely")
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error stopping encoder", e)
            }
        }
    }

    fun getFrameCount(): Int = frameCount

    companion object {
        private const val TAG = "VideoEncoder"
    }
}
