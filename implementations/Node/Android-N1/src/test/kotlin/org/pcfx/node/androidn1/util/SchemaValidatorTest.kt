package org.pcfx.node.androidn1.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.pcfx.node.androidn1.model.KnowledgeAtom
import java.time.Instant
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
class SchemaValidatorTest {
    @Mock
    private lateinit var context: Context
    private lateinit var validator: SchemaValidator
    private val gson = Gson()

    @Before
    fun setUp() {
        validator = SchemaValidator(context)
    }

    @Test
    fun testValidKnowledgeAtom() {
        val atom = KnowledgeAtom.builder()
            .id(UUID.randomUUID().toString())
            .ts(Instant.now().toString())
            .provenance(
                KnowledgeAtom.Provenance(
                    eventId = "event-1",
                    adapterId = "adapter-1",
                    analysisNodeId = "pcfx.atomizer.android-n1/0.1.0"
                )
            )
            .text("Test atom text")
            .tone(mapOf("aspiration" to 0.8))
            .vectorRef("local://vectors/hash:abc123")
            .confidence(mapOf("extraction" to 0.9))
            .signature("test-sig")
            .build()

        val result = validator.validateKnowledgeAtom(atom)
        assertTrue("Valid atom should pass validation", result.isValid())
    }

    @Test
    fun testMissingRequiredField() {
        val atom = KnowledgeAtom(
            id = "",
            ts = Instant.now().toString(),
            provenance = KnowledgeAtom.Provenance("e1", "a1", "n1"),
            text = "text",
            tone = mapOf("aspiration" to 0.5),
            vectorRef = "vec",
            confidence = mapOf("extraction" to 0.5),
            signature = "sig"
        )

        val result = validator.validateKnowledgeAtom(atom)
        assertFalse("Missing ID should fail validation", result.isValid())
    }

    @Test
    fun testMissingProvenance() {
        val json = JsonObject().apply {
            addProperty("schema", "pcfx.knowledge_atom/0.1")
            addProperty("id", "test-id")
            addProperty("ts", Instant.now().toString())
            addProperty("text", "test")
            add("tone", JsonObject())
            addProperty("vectorRef", "vec")
            add("confidence", JsonObject())
            addProperty("signature", "sig")
        }

        val atom = gson.fromJson(json, KnowledgeAtom::class.java)
        val result = validator.validateKnowledgeAtom(atom)
        assertFalse("Missing provenance should fail validation", result.isValid())
    }

    @Test
    fun testInvalidSchema() {
        val atom = KnowledgeAtom(
            schema = "pcfx.invalid_schema/0.1",
            id = "test-id",
            ts = Instant.now().toString(),
            provenance = KnowledgeAtom.Provenance("e1", "a1", "n1"),
            text = "text",
            tone = mapOf("aspiration" to 0.5),
            vectorRef = "vec",
            confidence = mapOf("extraction" to 0.5),
            signature = "sig"
        )

        val result = validator.validateKnowledgeAtom(atom)
        assertFalse("Invalid schema should fail validation", result.isValid())
    }
}
