package rockingboat.vertx.dataql.jjson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider


private fun process(value: JJson, gen: JsonGenerator) {
    when (value) {
        is JJson.Array    -> {
            gen.writeStartArray()
            value.value.forEach { process(it, gen) }
            gen.writeEndArray()
        }

        is JJson.Object   -> {
            gen.writeStartObject()
            value.value.forEach { t, u ->
                gen.writeFieldName(t)
                process(u, gen)
            }

            gen.writeEndObject()
        }

        is JJson.VBinary  -> gen.writeBinary(value.value)
        is JJson.VBoolean -> gen.writeBoolean(value.value)
        is JJson.Null     -> gen.writeNull()
        is JJson.VString  -> gen.writeString(value.value)
        is JJson.VNumber  -> {
            when (value.value::class) {
                Integer::class -> gen.writeNumber(value.value.toInt())
                Double::class  -> gen.writeNumber(value.value.toDouble())
                Float::class   -> gen.writeNumber(value.value.toFloat())
                Long::class    -> gen.writeNumber(value.value.toLong())
                Short::class   -> gen.writeNumber(value.value.toShort())
            }
        }
    }
}


class JJsonSerializer : JsonSerializer<JJson>() {
    override fun serialize(value: JJson?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value != null && gen != null)
            process(value, gen)
        else {
            gen?.writeStartObject()
            gen?.writeEndObject()
        }
    }
}