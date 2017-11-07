package rockingboat.vertx.dataql.jjson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import rockingboat.vertx.dataql.server.mapper

private fun process(node: JsonNode) = when (node.nodeType) {
    JsonNodeType.OBJECT  -> JJson.Object(node)
    JsonNodeType.ARRAY   -> JJson.Array(node)
    JsonNodeType.NUMBER  -> JJson.VNumber(node.numberValue())
    JsonNodeType.STRING  -> JJson.VString(node.textValue())
    JsonNodeType.BOOLEAN -> JJson.VBoolean(node.booleanValue())
    JsonNodeType.BINARY  -> JJson.VBinary(node.binaryValue())
    JsonNodeType.MISSING -> JJson.Missing
    else                 -> JJson.Null
}

sealed class JJson {

    object Null : JJson()
    object Missing : JJson()

    class VString(val value: String) : JJson()
    class VNumber(val value: Number) : JJson()
    class VBoolean(val value: Boolean) : JJson()
    class VBinary(val value: ByteArray) : JJson()

    class Array() : JJson() {

        @Suppress("unused")
        val value
            get() = list.toList()

        private val list = mutableListOf<JJson>()

        constructor(node: JsonNode) : this() {
            node.elements().forEach { list.add(process(it)) }
        }

        constructor(items: List<JJson>) : this() {
            list.addAll(items)
        }

        @Suppress("unused")
        fun get(idx: Int) = list.getOrNull(idx)

        @Suppress("unused")
        fun leaveKeys(key: String) = leaveKeys(listOf(key))

        @Suppress("unused", "MemberVisibilityCanPrivate")
        fun leaveKeys(keys: List<String>) = list.forEach {
            (it as? JJson.Object)?.leaveKeys(keys)
        }

        @Suppress("unused")
        fun add(item: JJson) = list.add(item)
    }

    class Object() : JJson() {
        @Suppress("unused")
        val keys
            get() = map.keys.toList()

        @Suppress("unused")
        val values
            get() = map.values.toList()

        @Suppress("unused")
        val value
            get() = map.toMap()

        private val map = mutableMapOf<String, JJson>()

        constructor(node: JsonNode) : this() {
            node.fieldNames().forEach {
                map[it] = process(node.get(it))
            }

        }

        @Suppress("unused")
        fun get(key: String) = map[key]

        @Suppress("unused")
        fun leaveKeys(keys: List<String>) = map.keys.toTypedArray().filterNot { keys.contains(it) }.forEach { map.remove(it) }

        @Suppress("unused")
        operator fun set(key: String, value: JJson) = map.set(key, value)
    }


    fun getValueByKey(key: String) = when (this) {
        is JJson.Object -> this.get(key) ?: JJson.Null
        else            -> JJson.Missing
    }

    override fun toString(): String {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
    }

}
