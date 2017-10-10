package rockingboat.vertx.dataql.server

sealed class JsonPathKey {
    data class ObjectKey(val key: String) : JsonPathKey()
    data class ArrayIndex(val idx: Int) : JsonPathKey()
    data class ObjectKeys(val set: Set<String>) : JsonPathKey()
    object FullArray : JsonPathKey()
}