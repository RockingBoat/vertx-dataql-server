package rockingboat.vertx.dataql.jjson

sealed class JJsonPathKey {
    data class ObjectKey(val key: String) : JJsonPathKey()
    data class ArrayIndex(val idx: Int) : JJsonPathKey()
    data class ObjectKeys(val set: Set<String>) : JJsonPathKey()
    object FullArray : JJsonPathKey()
}