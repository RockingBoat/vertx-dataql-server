package rockingboat.vertx.dataql.data

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
//import rockingboat.vertx.dataql.server.extension.getOrNull

sealed class ServiceQueryException : Exception() {
    object ServiceNameIsMissing : ServiceQueryException()
    object VersionIsMissing : ServiceQueryException()
    object MethodIsMissing : ServiceQueryException()
}

//data class ServiceQuery(private val json: JsonObject) {
//    val name = json.getString("service") ?: throw ServiceQueryException.ServiceNameIsMissing
//    val version = json.getString("version") ?: throw ServiceQueryException.VersionIsMissing
//    val method = json.getString("method") ?: throw ServiceQueryException.MethodIsMissing
//    val request = json.getJsonObject("request") ?: JsonObject()
//    val filter = json.getOrNull<JsonArray>("filter")
//    val fields = json.getOrNull<JsonObject>("fields")
//    val extract = json.getOrNull<JsonObject>("extract")
//    val bindTo = json.getString("bindTo") ?: this.name
//
//
//}
