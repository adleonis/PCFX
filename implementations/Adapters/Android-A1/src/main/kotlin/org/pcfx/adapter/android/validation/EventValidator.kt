package org.pcfx.adapter.android.validation

import android.content.Context
import com.github.erosb.jsonsKema.Schema
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.ValidationException
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import org.json.JSONTokener
import java.io.InputStreamReader

class EventValidator(private val context: Context) {
    private val schema: Schema by lazy {
        loadSchema("pcfx_exposure_event-v0.1.schema.json")
    }

    fun validateEvent(eventJson: String): ValidationResult {
        return try {
            val jsonObject = JSONObject(eventJson)
            schema.validate(jsonObject)
            ValidationResult.Valid
        } catch (e: ValidationException) {
            ValidationResult.Invalid(e.message ?: "Unknown validation error")
        } catch (e: Exception) {
            ValidationResult.Invalid(e.message ?: "Invalid JSON")
        }
    }

    fun validateEventStructure(eventJson: String): ValidationResult {
        return try {
            val jsonObject = JsonParser.parseString(eventJson) as JsonObject

            // Check required fields
            val requiredFields = listOf(
                "schema", "id", "ts", "device", "adapter_id",
                "capabilities_used", "source", "content", "privacy", "signature"
            )

            for (field in requiredFields) {
                if (!jsonObject.has(field)) {
                    return ValidationResult.Invalid("Missing required field: $field")
                }
            }

            // Validate schema constant
            val schema = jsonObject.get("schema").asString
            if (schema != "pcfx.exposure_event/0.1") {
                return ValidationResult.Invalid("Invalid schema version: $schema")
            }

            // Validate source.surface
            val source = jsonObject.getAsJsonObject("source")
            val surface = source.get("surface").asString
            if (surface !in listOf("app", "browser", "audio", "tv", "wearable", "system")) {
                return ValidationResult.Invalid("Invalid surface: $surface")
            }

            // Validate content.kind
            val content = jsonObject.getAsJsonObject("content")
            val kind = content.get("kind").asString
            if (kind !in listOf("text", "audio", "image", "video", "ad", "system")) {
                return ValidationResult.Invalid("Invalid content kind: $kind")
            }

            // Validate privacy.retention_days
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

    private fun loadSchema(assetFileName: String): Schema {
        val schemaInputStream = context.assets.open(assetFileName)
        return SchemaLoader.load(JSONObject(schemaInputStream.bufferedReader().readText()))
    }

    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Invalid(val message: String) : ValidationResult()
    }
}
