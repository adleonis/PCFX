package org.pcfx.pdv.androidpdv1.server

import android.content.Context
import android.util.Log
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.request.receiveText
import io.ktor.server.request.receive
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.pcfx.pdv.androidpdv1.data.PdvDatabase
import org.pcfx.pdv.androidpdv1.domain.EventRepository
import org.pcfx.pdv.androidpdv1.domain.AtomRepository
import org.pcfx.pdv.androidpdv1.domain.MetricRepository
import org.pcfx.pdv.androidpdv1.domain.BlobRepository
import org.pcfx.pdv.androidpdv1.domain.ConsentRepository
import org.pcfx.pdv.androidpdv1.domain.ConnectionTracker
import org.pcfx.pdv.androidpdv1.domain.HealthCheckRepository
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

class PdvServer(private val context: Context, private val port: Int = 7777) {
    private val db = PdvDatabase.getInstance(context)
    private val eventRepo = EventRepository(db.eventDao())
    private val atomRepo = AtomRepository(db.atomDao())
    private val metricRepo = MetricRepository(db.metricDao())
    private val blobRepo = BlobRepository(db.blobDao(), context)
    private val consentRepo = ConsentRepository(db.consentDao())
    private val healthCheckRepo = HealthCheckRepository(db.healthCheckDao())
    private val connectionTracker: ConnectionTracker

    init {
        ConnectionTracker.initialize(healthCheckRepo)
        connectionTracker = ConnectionTracker.getInstance()
    }

    private var server: io.ktor.server.engine.ApplicationEngine? = null

    fun start() {
        try {
            server = embeddedServer(CIO, port = port, module = { setupRoutes() })
                .start(wait = false)
            Log.d(TAG, "PDV Server started on port $port")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting server", e)
            throw e
        }
    }

    fun stop() {
        try {
            server?.stop(gracePeriodMillis = 3000, timeoutMillis = 5000)
            server = null
            Log.d(TAG, "PDV Server stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping server", e)
        }
    }

    fun isRunning(): Boolean = server != null

    private fun Application.setupRoutes() {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        val gson = Gson()

        routing {
            get("/health") {
                try {
                    val appId = call.request.headers["X-App-ID"] ?: "unknown"
                    val appType = call.request.headers["X-App-Type"] ?: "client"
                    val appName = call.request.headers["X-App-Name"] ?: ""
                    val appVersion = call.request.headers["X-App-Version"] ?: ""
                    val platformInfo = call.request.headers["X-Platform-Info"] ?: ""

                    connectionTracker.recordConnection(appId, appType, appName, appVersion, platformInfo)
                    call.respond(mapOf("status" to "healthy"))
                } catch (e: Exception) {
                    Log.e(TAG, "Error in health check", e)
                    call.respond(mapOf("status" to "error"))
                }
            }

            post("/events") {
                try {
                    val eventJson = call.receiveText()
                    val result = eventRepo.insertEvent(eventJson)
                    if (result.isSuccess) {
                        call.respond(mapOf("status" to "ok", "id" to result.getOrNull()))
                    } else {
                        val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                        call.respond(mapOf("status" to "error", "message" to errorMsg))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error posting event", e)
                    val errorMsg = e.message ?: "Unknown error"
                    call.respond(mapOf("status" to "error", "message" to errorMsg))
                }
            }

            get("/events") {
                try {
                    val since = call.request.queryParameters["since"] ?: "1970-01-01T00:00:00Z"
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 64
                    val events = eventRepo.getEventsSince(since, limit)
                    Log.d(TAG, "Fetched ${events.size} events, serializing response")

                    val eventsJson = gson.toJson(events)
                    Log.d(TAG, "Serialized events: ${eventsJson.take(100)}...")

                    val responseJson = """{"events":$eventsJson,"count":${events.size}}"""
                    Log.d(TAG, "Responding with JSON: ${responseJson.take(100)}...")

                    call.respondText(responseJson, contentType = ContentType.Application.Json)
                } catch (e: Exception) {
                    Log.e(TAG, "Error getting events (Ask Gemini)", e)
                    val errorMsg = e.message ?: "Unknown error"
                    call.respondText(
                        gson.toJson(mapOf("status" to "error", "message" to errorMsg)),
                        contentType = ContentType.Application.Json
                    )
                }
            }

            post("/atoms") {
                try {
                    val atomJson = call.receiveText()
                    val result = atomRepo.insertAtom(atomJson)
                    if (result.isSuccess) {
                        call.respond(mapOf("status" to "ok", "id" to result.getOrNull()))
                    } else {
                        val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                        call.respond(mapOf("status" to "error", "message" to errorMsg))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error posting atom", e)
                    val errorMsg = e.message ?: "Unknown error"
                    call.respond(mapOf("status" to "error", "message" to errorMsg))
                }
            }

            get("/atoms") {
                try {
                    val since = call.request.queryParameters["since"] ?: "1970-01-01T00:00:00Z"
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 64
                    val atoms = atomRepo.getAtomsSince(since, limit)
                    val atomsJson = gson.toJson(atoms)
                    val responseJson = """{"atoms":$atomsJson,"count":${atoms.size}}"""
                    call.respondText(responseJson, contentType = ContentType.Application.Json)
                } catch (e: Exception) {
                    Log.e(TAG, "Error getting atoms (Ask Gemini)", e)
                    val errorMsg = e.message ?: "Unknown error"
                    call.respondText(
                        gson.toJson(mapOf("status" to "error", "message" to errorMsg)),
                        contentType = ContentType.Application.Json
                    )
                }
            }

            post("/metrics") {
                try {
                    val metricJson = call.receiveText()
                    val result = metricRepo.insertMetric(metricJson)
                    if (result.isSuccess) {
                        call.respond(mapOf("status" to "ok", "id" to result.getOrNull()))
                    } else {
                        val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                        call.respond(mapOf("status" to "error", "message" to errorMsg))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error posting metric", e)
                    val errorMsg = e.message ?: "Unknown error"
                    call.respond(mapOf("status" to "error", "message" to errorMsg))
                }
            }

            get("/metrics") {
                try {
                    val since = call.request.queryParameters["since"] ?: "1970-01-01T00:00:00Z"
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 64
                    val metrics = metricRepo.getMetricsSince(since, limit)
                    val metricsJson = gson.toJson(metrics)
                    val responseJson = """{"metrics":$metricsJson,"count":${metrics.size}}"""
                    call.respondText(responseJson, contentType = ContentType.Application.Json)
                } catch (e: Exception) {
                    Log.e(TAG, "Error getting metrics (Ask Gemini)", e)
                    val errorMsg = e.message ?: "Unknown error"
                    call.respondText(
                        gson.toJson(mapOf("status" to "error", "message" to errorMsg)),
                        contentType = ContentType.Application.Json
                    )
                }
            }

            post("/blobs") {
                try {
                    val contentType = call.request.headers["Content-Type"] ?: "application/octet-stream"
                    val blobData = call.receive<ByteArray>()
                    val result = blobRepo.insertBlob(blobData, contentType)
                    if (result.isSuccess) {
                        call.respond(mapOf("status" to "ok", "hash" to result.getOrNull()))
                    } else {
                        val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                        call.respond(mapOf("status" to "error", "message" to errorMsg))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error posting blob", e)
                    val errorMsg = e.message ?: "Unknown error"
                    call.respond(mapOf("status" to "error", "message" to errorMsg))
                }
            }

            get("/blobs/{hash}") {
                try {
                    val hash = call.parameters["hash"] ?: ""
                    val result = blobRepo.getBlob(hash)
                    if (result.isSuccess) {
                        val blob = result.getOrNull()
                        if (blob != null) {
                            call.respondBytes(blob.first, contentType = ContentType.parse(blob.second))
                        } else {
                            call.respond(mapOf("status" to "error", "message" to "Blob not found"))
                        }
                    } else {
                        call.respond(mapOf("status" to "error", "message" to (result.exceptionOrNull()?.message ?: "Unknown error")))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error getting blob", e)
                    call.respond(mapOf("status" to "error", "message" to e.message))
                }
            }

            get("/stats") {
                try {
                    val eventCount = eventRepo.getEventCount()
                    val atomCount = atomRepo.getAtomCount()
                    val metricCount = metricRepo.getMetricCount()
                    val blobCount = blobRepo.getBlobCount()
                    val connectionStats = runBlocking {
                        connectionTracker.getStats()
                    }
                    val statsMap = mapOf(
                        "events" to eventCount,
                        "atoms" to atomCount,
                        "metrics" to metricCount,
                        "blobs" to blobCount,
                        "adapters" to mapOf(
                            "total" to connectionStats.totalAdapters,
                            "active_24h" to connectionStats.activeAdapters24h
                        ),
                        "nodes" to mapOf(
                            "total" to connectionStats.totalNodes,
                            "active_24h" to connectionStats.activeNodes24h
                        ),
                        "clients" to mapOf(
                            "total" to connectionStats.totalClients,
                            "active_24h" to connectionStats.activeClients24h
                        )
                    )
                    val statsJson = gson.toJson(statsMap)
                    call.respondText(statsJson, contentType = ContentType.Application.Json)
                } catch (e: Exception) {
                    Log.e(TAG, "Error getting stats", e)
                    val errorMsg = e.message ?: "Unknown error"
                    call.respondText(
                        gson.toJson(mapOf("status" to "error", "message" to errorMsg)),
                        contentType = ContentType.Application.Json
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "PdvServer"
    }
}
