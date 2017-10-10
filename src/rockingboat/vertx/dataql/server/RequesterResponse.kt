package rockingboat.vertx.dataql.server

import rockingboat.vertx.dataql.server.extension.get
import rockingboat.vertx.dataql.server.extension.leaveKeys
import rockingboat.vertx.dataql.server.extension.toListOfJsonPathKey

sealed class RequesterResponse {
    data class Array(val list: List<Any>) : RequesterResponse()
    data class Object(val map: MutableMap<String, Any>) : RequesterResponse()
    object Error : RequesterResponse()
}


fun RequesterResponse.Object.filter(key: String): JMap? {
    var result = map as Any?
    key.toListOfJsonPathKey().forEach { k ->
        if (result != null) {
            result = when (result) {
                is JMapErased   -> (result as? JMap)?.leaveKeys(k)?.get(k)
                is JArrayErased -> (result as? JArray)?.leaveKeys(k)
                else            -> null
            }
        }
    }

    return map
}

@Suppress("UNCHECKED_CAST")
fun RequesterResponse.Object.lookup(key: String): Any? {
    var result = this.map as Any?
    key.toListOfJsonPathKey().forEach { k ->
        if (result != null)
            result = when (result) {
                is JMapErased   -> (result as? JMap)?.get(k)
                is JArrayErased -> (result as? JArray)?.get(k)
                else            -> throw IllegalAccessError("aaa ${result?.javaClass}")
            }
    }

    return result
}
