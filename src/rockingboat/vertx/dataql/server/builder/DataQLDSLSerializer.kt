package rockingboat.vertx.dataql.server.builder

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class DataQLDSLOptionsJsonSerializer : JsonSerializer<DataQLDSLOptions>() {
    override fun serialize(value: DataQLDSLOptions?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        value?.let { opt ->
            gen?.let { gen ->
                gen.writeStartObject()
                with(opt) {
                    if (isDebug)
                        gen.writeBooleanField(FIELD_OPTIONS_DEBUG, true)
                    if (isTrace)
                        gen.writeBooleanField(FIELD_OPTIONS_TRACE, true)
                }
                gen.writeEndObject()
            }
        }
    }
}

class DataQLDSLQueryJsonSerializer : JsonSerializer<DataQLDSLQuery>() {
    override fun serialize(value: DataQLDSLQuery?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        value?.let { query ->
            gen?.let { gen ->
                gen.writeStartArray(query.queries.count())
                query.queries.forEach { gen.writeObject(it) }
                gen.writeEndArray()
            }
        }
    }
}

class DataQLDSLQueryItemJsonSerializer : JsonSerializer<DataQLDSLQueryItem>() {
    override fun serialize(value: DataQLDSLQueryItem?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        value?.let { queryItem ->
            gen?.let { gen ->
                gen.writeStartObject()

                with(queryItem) {
                    fields?.let { gen.writeObjectField(FIELD_FIELDS, it) }
                    extract?.let { gen.writeObjectField(FIELD_EXTRACT, it) }
                    filter?.let { gen.writeObjectField(FIELD_FILTER, it) }
                    subQueries?.let { gen.writeObjectField(FIELD_SUB_QUERIES, it) }
                    service?.let { gen.writeStringField(FIELD_SERVICE, it) }
                    version?.let { gen.writeStringField(FIELD_VERSION, it) }
                    method?.let { gen.writeStringField(FIELD_METHOD, it) }
                    bindTo?.let { gen.writeStringField(FIELD_BIND_TO, it) }
                    request?.let { gen.writeObjectField(FIELD_REQUEST, it) }

                    if (filterOnService) {
                        gen.writeBooleanField(FIELD_FILTER_ON_SERVICE, filterOnService)
                    }
                }
                gen.writeEndObject()
            }
        }
    }
}

class DataQLDSLJsonSerializer : JsonSerializer<DataQLDSL>() {
    override fun serialize(value: DataQLDSL?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value != null && gen != null) {
            gen.writeStartObject()
            value.options?.let {
                gen.writeFieldName(FIELD_OPTIONS)
                gen.writeObject(it)
            }
            value.queries?.let {
                gen.writeFieldName(FIELD_QUERIES)
                gen.writeObject(it)
            }
            gen.writeEndObject()

        } else {
            gen?.writeStartObject()
            gen?.writeEndObject()
        }
    }
}