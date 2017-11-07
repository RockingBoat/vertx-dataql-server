package rockingboat.vertx.dataql.extension

//import rockingboat.vertx.dataql.server.JMap
import rockingboat.vertx.dataql.server.JsonPathKey

//fun JMap.get(key: JsonPathKey) = when (key) {
//    is JsonPathKey.ObjectKey -> get(key.key)
//    else                     -> null
//}
//
//
//fun JMap.leaveKeys(key: JsonPathKey) = when (key) {
//    is JsonPathKey.ObjectKey  -> leaveKeys(key.key)
//    is JsonPathKey.ObjectKeys -> leaveKeys(key.set.toList())
//    else                      -> throw IllegalAccessError("aaa")
//}
//
//fun JMap.leaveKeys(key: String) = leaveKeys(listOf(key))
//fun JMap.leaveKeys(keys: List<String>) = apply {
//    this.keys.toTypedArray().filterNot { keys.contains(it) }.forEach { remove(it) }
//}
