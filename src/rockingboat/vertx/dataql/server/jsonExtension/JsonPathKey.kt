package rockingboat.vertx.dataql.server.jsonExtension

import io.vertx.core.json.JsonArray
import io.vertx.core.shareddata.impl.ClusterSerializable
import io.vertx.kotlin.core.json.Json
import io.vertx.kotlin.core.json.JsonArray
import io.vertx.kotlin.core.json.JsonObject


sealed class JsonPathKey {
    data class ObjectKey(val key: String) : JsonPathKey()
    data class ArrayIndex(val idx: Int) : JsonPathKey()
    data class ObjectKeys(val set: Set<String>) : JsonPathKey()
    object FullArray : JsonPathKey()

//    fun newInstance(): JsonBase = when (this) {
//        is ObjectKey  -> JsonObject()
//        is ArrayIndex -> JsonArray()
//        else                                                                    -> throw IllegalArgumentException("unsupported type ")
//    }

}

//data class JsonPathKeyTuple(val current: JsonPathKey, val next: JsonPathKey?) {
//    fun value(obj: Json): Any? = when {
//        this.current is JsonPathKey.ObjectKey &&
//            obj is JsonObject   -> obj [this.current.key]
//        this.current is JsonPathKey.ArrayIndex &&
//            obj is JsonArray -> obj.getOrNull(this.current.idx)
//        else                    -> throw IllegalArgumentException("unsupported type") // TODO: Make normal Exception
//    }
//}
//
//fun List<JsonPathKeyTuple>.isArray(): Boolean = this.count { it.current is JsonPathKey.FullArray } > 0
////fun List<