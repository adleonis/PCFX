package org.pcfx.adapter.android.validation

import android.content.Context
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class EventValidator(private val context: Context) {

    fun validateEvent(eventJson: String): ValidationResult {
        return validateEventStructure(eventJson)
    }

    fun validateEventStructure(eventJson: String): ValidationResult {
        return try {
            val jsonObject = JsonParser.parseString(eventJson) as JsonObject

            val requiredFields = listOf(
                "schema", "id", "ts", "device", "adapter_id",
                "capabilities_used", "source", "content", "privacy", "signature"
            )

            for (field in requiredFields) {
                if (!jsonObject.has(field)) {
                    return ValidationResult.Invalid("Missing required field: $field")
                }
            }

            val schema = jsonObject.get("schema").asString
            if (schema != "pcfx.exposure_event/0.1") {
                return ValidationResult.Invalid("Invalid schema version: $schema")
            }

            val source = jsonObject.getAsJsonObject("source")
            val surface = source.get("surface").asString
            if (surface !in listOf("app", "browser", "audio", "tv", "wearable", "system")) {
                return ValidationResult.Invalid("Invalid surface: $surface")
            }

            val content = jsonObject.getAsJsonObject("content")
            val kind = content.get("kind").asString
            if (kind !in listOf("text", "audio", "image", "video", "ad", "system")) {
                return ValidationResult.Invalid("Invalid content kind: $kind")
            }

            val privacy = jsonObject.getAsJsonObject("privacy")
            val retentionDays = privacy.get("retention_days").asInt
            if (retentionDays < 0) {
                return ValidationResult.Invalid("retention_days must be >= 0")
            }

            ValidationResult.Valid
        } catch (e: Exception) {
            ValidationResult.Invalid(e.message ?: "Validation failed")
        }
    }

    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Invalid(val message: String) : ValidationResult()
    }
}
