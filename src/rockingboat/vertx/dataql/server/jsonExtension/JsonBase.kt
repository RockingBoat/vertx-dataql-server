package rockingboat.vertx.dataql.server.jsonExtension

import io.vertx.core.shareddata.impl.ClusterSerializable

//import com.beust.klaxon.JsonArray
//import com.beust.klaxon.JsonBase
//import com.beust.klaxon.JsonObject


//fun JsonBase.setByPath(path: String, value: Any) {
//    var obj = this
//
//    path.toJsonPathKeyTuples().forEach {
//        var sValue = it.value(obj)
//        if (sValue == null) {
//            sValue = it.next?.newInstance() ?: value
//            when {
//                it.current is JsonPathKey.ObjectKey &&
//                    obj is JsonObject   -> (obj as JsonObject)[it.current.key] = sValue
//                it.current is JsonPathKey.ArrayIndex &&
//                    obj is JsonArray<*> -> (obj as JsonArray<Any>).add(it.current.idx, sValue)
//                else                    -> throw JsonValueTypeIsNotEqualsToKeyType()  // TODO: Make normal Exception
//            }
//        }
//
//        if (it.next != null)
//            obj = sValue as JsonObject
//    }
//}
//
//fun JsonBase.getByPath(path: String): Any? {
//    var obj = this
//    var output: Any? = null
//
//    path.toJsonPathKeyTuples().forEach {
//        val sValue = it.value(obj) ?: throw JsonValueIsNull()
//        if (it.next != null) obj = sValue as JsonObject
//        output = sValue
//    }
//
//    return output
//}
