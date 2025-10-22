package org.pcfx.adapter.android.config

import java.util.UUID

object AdapterBuildConfig {
    const val APP_NAME = "Android-A1"
    const val APP_TYPE = "adapter"
    const val APP_VERSION = "0.1.0"
    
    val UNIQUE_APP_ID: String = UUID.randomUUID().toString()
}
