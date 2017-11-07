package rockingboat.vertx.dataql.server

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

val FIELD_RESPONSE_DATA = "data"

class ResponseSerializer : JsonSerializer<Response>() {
    override fun serialize(value: Response?, gen: JsonGenerator?, serializers: SerializerProvider?) {

    }

}

class ResponseDataSerializer : JsonSerializer<ResponseData>() {
    override fun serialize(value: ResponseData?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value == null)
            gen?.writeNullField(FIELD_RESPONSE_DATA)
    }

}