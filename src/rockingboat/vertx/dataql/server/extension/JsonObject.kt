package rockingboat.vertx.dataql.server.extension

import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.shareddata.impl.ClusterSerializable
import io.vertx.kotlin.core.json.get
import rockingboat.vertx.dataql.server.JsonPathKey

//fun JsonObject.createStructForKey(key: List<JsonPathKey>): JsonObject { // TODO: Make More Cute
//    var obj = this
//
//    key.forEach {
//        obj = when (it) {
//            is JsonPathKey.ObjectKey -> obj.apply { obj.map[it.key] = JsonObject() }
//            else                     -> throw IllegalAccessError("aaa")
//        }
//    }
//
//    return obj
//}
//
//fun JsonObject.removeAnotherKeysThanThis(key: String): JsonObject = removeAnotherKeysThanThese(listOf(key))
//fun JsonObject.removeAnotherKeysThanThese(_keys: List<String>): JsonObject = apply {
//    fieldNames().filterNot { _keys.contains(it) }.forEach { remove(it) }
//}
//
//fun JsonObject.filter(keys: JsonArray): JsonObject = filter(keys.toList() as? List<String> ?: emptyList<String>())
//fun JsonObject.filter(keys: List<String>): JsonObject = keys.map { lookup(it, true) }
//    .fold(JsonObject()) { acc, r ->
//        acc.mergeIn(r as JsonObject, true)
//    }
//
//
//fun JsonObject.lookup(key: String, needOrig: Boolean = false): Any? {
//    var objCopy = this.copy() as Any?
//    val objCopyOrig = objCopy
//    key.toListOfJsonPathKey()
//        .filterNot { it is JsonPathKey.FullArray }
//        .forEach {
//            // TODO: Make Normal
//            if (objCopy != null) {
//                objCopy = filterExecutor(objCopy, it)
//            }
//        }
//
//    return if (needOrig) {
//        objCopyOrig
//    } else {
//        objCopy
//    }
//}
//
//
//private fun filterExecutor(objCopy: Any?, key: JsonPathKey): Any? = when (key) {
//    is JsonPathKey.ObjectKey  -> when (objCopy) {
//        is JsonObject -> objCopy.removeAnotherKeysThanThis(key.key).get<Any>(key.key)
//        is JsonArray  -> objCopy.removeAnotherKeysThanThis(key.key)
//        else          -> throw IllegalAccessError("aaa ${objCopy?.javaClass}")
//    }
//
//    is JsonPathKey.ObjectKeys -> when (objCopy) {
//        is JsonArray  -> objCopy.removeAnotherKeysThanThese(key.set.toList())
//        is JsonObject -> objCopy.removeAnotherKeysThanThese(key.set.toList())
//        else          -> throw IllegalAccessError("aaa ${objCopy?.javaClass}")
//    }
//
//    is JsonPathKey.ArrayIndex -> (objCopy as JsonArray)[key.idx]
//    else                      -> throw IllegalAccessError("aaa")
//}
//
//fun JsonObject.extract(map: Map<String, String>): JsonObject {
//    val result = JsonObject()
//
//    map.forEach { from, to ->
//        (to as? String)?.let {
//            val toKey = it.toListOfJsonPathKey()
//            val lKey = toKey.last()
//            val toKeyWithoutLast = toKey.dropLast(1)
//            when (lKey) {
//                is JsonPathKey.ObjectKey -> result.createStructForKey(toKeyWithoutLast).map[lKey.key] = lookup(from)
//                else                     -> throw IllegalAccessError("aaa") // TODO: Make Own Exception Classes
//            }
//        }
//    }
//
//    return result
//}
//
//@Suppress("UNCHECKED_CAST")
//fun JsonObject.extract(rules: JsonObject) = extract((rules.map as? Map<String, String>) ?: mapOf())
//
//@Suppress("UNCHECKED_CAST")
//inline fun <reified T : ClusterSerializable> JsonObject.getOrNull(key: String): T? {
//    val result = map[key]
//
//    return when (T::class) {
//        JsonObject::class -> when (result) {
//            is Map<*, *> -> JsonObject(result as Map<String, Any>) as? T
//            else         -> result as? T
//        }
//
//        JsonArray::class  -> when (result) {
//            is List<*> -> JsonArray(result as List<Any?>) as? T
//            else       -> result as? T
//        }
//
//        else              -> null
//    }
//
//}
//
