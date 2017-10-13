package rockingboat.vertx.dataql.server.extension

import rockingboat.vertx.dataql.server.JArray
import rockingboat.vertx.dataql.server.JsonPathKey

//fun JArray.get(key: JsonPathKey) = when (key) {
//    is JsonPathKey.ArrayIndex -> get(key.idx)
//    else                      -> null
//}
//
//
//fun JArray.leaveKeys(key: String) = leaveKeys(listOf(key))
//fun JArray.leaveKeys(keys: List<String>) = map { it.leaveKeys(keys) }
//fun JArray.leaveKeys(key: JsonPathKey) = when (key) {
//    is JsonPathKey.ObjectKey  -> leaveKeys(key.key)
//    is JsonPathKey.ObjectKeys -> leaveKeys(key.set.toList())
//    else                      -> throw java.lang.IllegalAccessError("aaa")
//}