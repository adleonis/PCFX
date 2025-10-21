package org.pcfx.node.androidn1.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.pcfx.node.androidn1.model.KnowledgeAtom

class SchemaValidator(context: Context) {
    private val gson = Gson()
    private val assetManager = context.assets

    fun validateKnowledgeAtom(atom: KnowledgeAtom): ValidationResult {
        return try {
            val requiredFields = listOf(
                "schema" to "pcfx.knowledge_atom/0.1",
                "id",
                "ts",
                "provenance.event_id",
                "provenance.adapter_id",
                "provenance.analysis_node_id",
                "text",
                "tone",
                "confidence",
                "signature"
            )

            val atomJson = gson.toJsonTree(atom) as JsonObject
            var valid = true
            val errors = mutableListOf<String>()

            // Check schema value
            if (atomJson.get("schema")?.asString != "pcfx.knowledge_atom/0.1") {
                valid = false
                errors.add("Invalid schema: expected 'pcfx.knowledge_atom/0.1'")
            }

            // Check required fields
            if (atomJson.get("id")?.asString.isNullOrBlank()) {
                valid = false
                errors.add("Missing required field: id")
            }

            if (atomJson.get("ts")?.asString.isNullOrBlank()) {
                valid = false
                errors.add("Missing required field: ts")
            }

            if (atomJson.get("text")?.asString.isNullOrBlank()) {
                valid = false
                errors.add("Missing required field: text")
            }

            val provenance = atomJson.getAsJsonObject("provenance")
            if (provenance == null) {
                valid = false
                errors.add("Missing required field: provenance")
            } else {
                if (provenance.get("event_id")?.asString.isNullOrBlank()) {
                    valid = false
                    errors.add("Missing required field: provenance.event_id")
                }
                if (provenance.get("adapter_id")?.asString.isNullOrBlank()) {
                    valid = false
                    errors.add("Missing required field: provenance.adapter_id")
                }
                if (provenance.get("analysis_node_id")?.asString.isNullOrBlank()) {
                    valid = false
                    errors.add("Missing required field: provenance.analysis_node_id")
                }
            }

            val tone = atomJson.get("tone")
            if (tone == null || !tone.isJsonObject) {
                valid = false
                errors.add("Missing required field: tone (must be object)")
            }

            val confidence = atomJson.get("confidence")
            if (confidence == null || !confidence.isJsonObject) {
                valid = false
                errors.add("Missing required field: confidence (must be object)")
            }

            if (atomJson.get("signature")?.asString.isNullOrBlank()) {
                valid = false
                errors.add("Missing required field: signature")
            }

            if (valid) {
                Log.d(TAG, "KnowledgeAtom ${atom.id} validated successfully")
                ValidationResult.Success()
            } else {
                Log.w(TAG, "KnowledgeAtom validation failed: ${errors.joinToString(", ")}")
                ValidationResult.Failure(errors)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error validating KnowledgeAtom", e)
            ValidationResult.Failure(listOf(e.message ?: "Unknown error"))
        }
    }

    fun validateExposureEvent(eventJson: JsonElement): ValidationResult {
        return try {
            if (!eventJson.isJsonObject) {
                return ValidationResult.Failure(listOf("Event must be a JSON object"))
            }

            val obj = eventJson.asJsonObject
            val requiredFields = listOf(
                "schema", "id", "ts", "device", "adapter_id", "capabilities_used",
                "source", "content", "privacy", "signature"
            )

            var valid = true
            val errors = mutableListOf<String>()

            for (field in requiredFields) {
                if (obj.get(field) == null) {
                    valid = false
                    errors.add("Missing required field: $field")
                }
            }

            if (obj.get("schema")?.asString != "pcfx.exposure_event/0.1") {
                valid = false
                errors.add("Invalid schema: expected 'pcfx.exposure_event/0.1'")
            }

            if (valid) {
                Log.d(TAG, "ExposureEvent ${obj.get("id")?.asString} validated successfully")
                ValidationResult.Success()
            } else {
                Log.w(TAG, "ExposureEvent validation failed: ${errors.joinToString(", ")}")
                ValidationResult.Failure(errors)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error validating ExposureEvent", e)
            ValidationResult.Failure(listOf(e.message ?: "Unknown error"))
        }
    }

    companion object {
        private const val TAG = "SchemaValidator"
    }
}

sealed class ValidationResult {
    class Success : ValidationResult()
    data class Failure(val errors: List<String>) : ValidationResult()

    fun isValid() = this is Success
}
