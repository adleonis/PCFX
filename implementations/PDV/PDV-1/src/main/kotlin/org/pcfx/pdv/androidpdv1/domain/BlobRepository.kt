package org.pcfx.pdv.androidpdv1.domain

import android.content.Context
import android.util.Log
import org.pcfx.pdv.androidpdv1.data.dao.BlobDao
import org.pcfx.pdv.androidpdv1.data.entity.BlobEntity
import java.io.File
import java.security.MessageDigest

class BlobRepository(private val blobDao: BlobDao, private val context: Context) {

    suspend fun insertBlob(blobData: ByteArray, contentType: String, retentionDays: Int = 30): Result<String> {
        return try {
            val hash = sha256(blobData)
            val fileName = "$hash.blob"
            val blobFile = File(getBlobDir(), fileName)

            blobFile.writeBytes(blobData)

            val blob = BlobEntity(
                hash = hash,
                contentType = contentType,
                size = blobData.size.toLong(),
                fileName = fileName,
                retentionDays = retentionDays
            )
            blobDao.insertBlob(blob)
            Log.d(TAG, "Inserted blob: $hash (${blobData.size} bytes)")
            Result.success(hash)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting blob", e)
            Result.failure(e)
        }
    }

    suspend fun getBlob(hash: String): Result<Pair<ByteArray, String>?> {
        return try {
            val blob = blobDao.getBlobByHash(hash)
            if (blob != null) {
                val blobFile = File(getBlobDir(), blob.fileName)
                if (blobFile.exists()) {
                    Result.success(blobFile.readBytes() to blob.contentType)
                } else {
                    Log.w(TAG, "Blob file not found: $hash")
                    Result.success(null)
                }
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting blob", e)
            Result.failure(e)
        }
    }

    suspend fun getBlobCount(): Int {
        return try {
            blobDao.getBlobCount()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting blob count", e)
            0
        }
    }

    suspend fun deleteBlob(hash: String) {
        try {
            val blob = blobDao.getBlobByHash(hash)
            if (blob != null) {
                File(getBlobDir(), blob.fileName).delete()
                blobDao.deleteBlob(hash)
                Log.d(TAG, "Deleted blob: $hash")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting blob", e)
        }
    }

    private fun getBlobDir(): File {
        val dir = File(context.filesDir, "blobs")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun sha256(data: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(data)
        return "sha256:${hashBytes.joinToString("") { "%02x".format(it) }}"
    }

    companion object {
        private const val TAG = "BlobRepository"
    }
}
