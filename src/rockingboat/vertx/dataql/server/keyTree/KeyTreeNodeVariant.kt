package rockingboat.vertx.dataql.server.keyTree

import io.vertx.kotlin.core.json.JsonObject
import rockingboat.vertx.dataql.server.jjson.JJson

sealed class KeyTreeNodeVariant {
    data class Filter(val key: String) : KeyTreeNodeVariant()
    data class Extract(val key: String) : KeyTreeNodeVariant() {
        var result = KeyTreeNodeVariantValue()
    }

    data class Field(val key: String) : KeyTreeNodeVariant() {
        var result = KeyTreeNodeVariantValue()
    }
}

class KeyTreeNodeVariantValue {
    private var count = 0
    private var value: JJson = JJson.Null
    fun getValue(key: String) = when (count) {
        0    -> JJson.Missing
        1    -> value
        else -> {
            val cValue = value as? JJson.Array

            if (cValue == null)
                JJson.Missing
            else
                JJson.Array(cValue.value.map { JJson.Object().apply { set(key, it) } })
        }
    }

    fun addValue(value: JJson) {
        when (count) {
            0    -> this.value = value
            1    -> this.value = JJson.Array(listOf(this.value, value))
            else -> (this.value as? JJson.Array)?.add(value)
        }
        count++
    }
}