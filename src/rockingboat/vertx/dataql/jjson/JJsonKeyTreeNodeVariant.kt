package rockingboat.vertx.dataql.jjson

sealed class JJsonKeyTreeNodeVariant {
    data class Filter(val key: String) : JJsonKeyTreeNodeVariant()
    data class Extract(val key: String) : JJsonKeyTreeNodeVariant() {
        var result = JJsonKeyTreeNodeVariantValue()
    }

    data class Field(val key: String) : JJsonKeyTreeNodeVariant() {
        var result = JJsonKeyTreeNodeVariantValue()
    }
}

class JJsonKeyTreeNodeVariantValue {
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