package rockingboat.vertx.dataql.server.data

import io.vertx.core.json.JsonObject

data class Options(private val json: JsonObject) {
    val isDebug = json.getBoolean("isDebug") ?: false
}