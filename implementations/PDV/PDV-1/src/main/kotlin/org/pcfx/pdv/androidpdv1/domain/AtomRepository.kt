package org.pcfx.pdv.androidpdv1.domain

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.pcfx.pdv.androidpdv1.data.dao.AtomDao
import org.pcfx.pdv.androidpdv1.data.entity.AtomEntity
import java.util.UUID

@Suppress("UNCHECKED_CAST")

class AtomRepository(private val atomDao: AtomDao) {
    private val gson = Gson()

    suspend fun insertAtom(atomJson: String): Result<String> {
        return try {
            val jsonObj = gson.fromJson(atomJson, JsonObject::class.java)
            val id = jsonObj.get("id")?.asString ?: UUID.randomUUID().toString()
            val ts = jsonObj.get("ts")?.asString ?: ""
            val nodeId = jsonObj.get("node_id")?.asString ?: "unknown"
            val signature = jsonObj.get("signature")?.asString ?: ""

            val atom = AtomEntity(
                id = id,
                ts = ts,
                nodeId = nodeId,
                atomJson = atomJson,
                signature = signature
            )
            atomDao.insertAtom(atom)
            Log.d(TAG, "Inserted atom: $id")
            Result.success(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting atom", e)
            Result.failure(e)
        }
    }

    suspend fun getAtomsSince(since: String, limit: Int): List<Map<String, Any>> {
        return try {
            val atoms = atomDao.getAtomsSince(since, limit)
            atoms.map { atom ->
                val atomJson = gson.fromJson(atom.atomJson, Map::class.java) as? Map<String, Any> ?: emptyMap()
                mapOf(
                    "id" to atom.id,
                    "ts" to atom.ts,
                    "node_id" to atom.nodeId,
                    "atom" to atomJson
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting atoms", e)
            emptyList()
        }
    }

    suspend fun getAtomById(id: String): AtomEntity? {
        return try {
            atomDao.getAtomById(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting atom", e)
            null
        }
    }

    suspend fun getAtomCount(): Int {
        return try {
            atomDao.getAtomCount()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting atom count", e)
            0
        }
    }

    suspend fun deleteAtomsBefore(before: String) {
        try {
            atomDao.deleteAtomsBefore(before)
            Log.d(TAG, "Deleted atoms before $before")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting atoms", e)
        }
    }

    companion object {
        private const val TAG = "AtomRepository"
    }
}
