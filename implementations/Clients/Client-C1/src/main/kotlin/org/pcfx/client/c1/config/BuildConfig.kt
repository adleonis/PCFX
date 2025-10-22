package org.pcfx.client.c1.config

import java.util.UUID

object ClientBuildConfig {
    val UNIQUE_APP_ID: String = UUID.randomUUID().toString()
    const val APP_NAME = "Client-C1"
    const val APP_TYPE = "client"
    const val APP_VERSION = "0.1.0"
    const val PLATFORM_INFO = "Android"
    
    const val DEFAULT_PDV_HOST = "localhost"
    const val DEFAULT_PDV_PORT = 7777
}
