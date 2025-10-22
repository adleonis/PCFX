package org.pcfx.node.androidn1.data

import okhttp3.ResponseBody
import org.pcfx.node.androidn1.model.ExposureEvent
import org.pcfx.node.androidn1.model.KnowledgeAtom
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PdvService {
    @GET("/health")
    suspend fun checkHealth(
        @Header("X-App-ID") appId: String,
        @Header("X-App-Type") appType: String,
        @Header("X-App-Name") appName: String,
        @Header("X-App-Version") appVersion: String,
        @Header("X-Platform-Info") platformInfo: String
    ): Response<HealthResponse>

    @GET("/events")
    suspend fun getEvents(
        @Query("since") since: String,
        @Query("limit") limit: Int = 64,
        @Header("X-PCFX-Cap") capabilities: String = "pdv.read.events"
    ): Response<ResponseBody>

    @POST("/atoms")
    suspend fun postAtom(
        @Body atom: KnowledgeAtom,
        @Header("X-PCFX-Cap") capabilities: String = "pdv.write.atoms"
    ): Response<AtomResponse>

    @POST("/atoms/batch")
    suspend fun postAtomsBatch(
        @Body request: BatchAtomRequest,
        @Header("X-PCFX-Cap") capabilities: String = "pdv.write.atoms"
    ): Response<BatchAtomResponse>

    data class HealthResponse(
        val status: String
    )

    data class AtomResponse(
        val id: String,
        val ts: String,
        val status: String
    )

    data class BatchAtomRequest(
        val atoms: List<KnowledgeAtom>
    )

    data class BatchAtomResponse(
        val succeeded: Int,
        val failed: Int,
        val errors: List<String>? = null
    )
}
