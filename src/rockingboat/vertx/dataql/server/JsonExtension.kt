package rockingboat.vertx.dataql.server

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

fun JsonArray.removeAnotherKeysThanThis(key: String): JsonArray = removeAnotherKeysThanThese(listOf(key))
fun JsonArray.removeAnotherKeysThanThese(keys: List<String>): JsonArray = apply {
    forEach { (it as? JsonObject)?.removeAnotherKeysThanThese(keys) }
}

fun JsonObject.removeAnotherKeysThanThis(key: String): JsonObject = removeAnotherKeysThanThese(listOf(key))
fun JsonObject.removeAnotherKeysThanThese(_keys: List<String>): JsonObject = apply {
    fieldNames().filterNot { _keys.contains(it) }.forEach { remove(it) }
}

fun Any.prettyPrinted(): String? = when (this) {
    is JsonObject -> encodePrettily()
    is JsonArray  -> encodePrettily()
    else          -> null
}