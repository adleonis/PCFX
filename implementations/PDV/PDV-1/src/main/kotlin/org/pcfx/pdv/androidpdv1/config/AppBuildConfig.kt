package org.pcfx.pdv.androidpdv1.config

import java.util.UUID

object AppBuildConfig {
    const val APP_NAME = "PDV-1"
    const val APP_TYPE = "pdv"
    const val APP_VERSION = "1.0.0"

    val UNIQUE_APP_ID: String = run {
        UUID.randomUUID().toString()
    }
}
