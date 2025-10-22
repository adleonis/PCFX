package org.pcfx.node.androidn1.config

import java.util.UUID

object NodeBuildConfig {
    const val APP_NAME = "Node-N1"
    const val APP_TYPE = "node"
    const val APP_VERSION = "0.1.0"
    
    val UNIQUE_APP_ID: String = UUID.randomUUID().toString()
}
