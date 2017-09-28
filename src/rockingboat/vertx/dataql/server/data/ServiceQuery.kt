package rockingboat.vertx.dataql.server.data

import io.vertx.core.json.JsonObject

sealed class ServiceQueryException : Exception() {
    object VersionIsMissing : ServiceQueryException()
    object MethodIsMissing : ServiceQueryException()
}

data class ServiceQuery(private val json: JsonObject) {
    val version = json.getString("version") ?: throw ServiceQueryException.VersionIsMissing
    val method = json.getString("method") ?: throw ServiceQueryException.MethodIsMissing
    val request = json.getJsonObject("request") ?: JsonObject()
}
