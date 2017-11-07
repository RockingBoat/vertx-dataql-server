package rockingboat.vertx.dataql.builder

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.convertValue
import rockingboat.vertx.dataql.server.mapper

class DataQLDSLJsonDeserializer : JsonDeserializer<DataQLDSL>() {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?) = DataQLDSL().apply {
        p?.let {
            val oc = p.codec
            val node = oc.readTree<JsonNode>(p)

            node.get(FIELD_OPTIONS)?.let {
                options = mapper.convertValue(it)
            }

            node.get(FIELD_QUERIES)?.let { item ->
                if (item.isArray) {
                    queries = DataQLDSLQuery()
                    item.forEach {
                        queries?.queries?.add(mapper.convertValue(it))
                    }
                }
            }
        }
    }
}


class DataQLDSLQueryItemJsonDeserializer : JsonDeserializer<DataQLDSLQueryItem>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?) = DataQLDSLQueryItem().apply {
        p?.let {
            val oc = p.codec
            val node = oc.readTree<JsonNode>(p)
            node.get(FIELD_FIELDS)?.let {
                fields = mapper.convertValue(it)
            }

            node.get(FIELD_EXTRACT)?.let {
                extract = mapper.convertValue(it)
            }

            node.get(FIELD_FILTER)?.let {
                filter = mapper.convertValue(it)
            }

            node.get(FIELD_SUB_QUERIES)?.let {
                if (it.isArray) {
                    subQueries = DataQLDSLQuery()
                    it.forEach {
                        subQueries?.queries?.add(mapper.convertValue(it))
                    }
                }
            }

            service = node.get(FIELD_SERVICE)?.textValue()
            version = node.get(FIELD_VERSION)?.textValue()
            method = node.get(FIELD_METHOD)?.textValue()
            bindTo = node.get(FIELD_BIND_TO)?.textValue()
            node.get(FIELD_REQUEST)?.let {
                request = if (it.isObject)
                    mapper.convertValue<Map<String, Any?>>(it)
                else
                    mapper.convertValue<List<Any>>(it)
            }

            filterOnService = node.get(FIELD_FILTER_ON_SERVICE)?.booleanValue() ?: filterOnService
        }
    }
}

class DataQLDSLOptionsJsonDeserializer : JsonDeserializer<DataQLDSLOptions>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?) = DataQLDSLOptions().apply {
        p?.let {

            val oc = p.codec
            val node = oc.readTree<JsonNode>(p)
            isDebug = node.get(FIELD_OPTIONS_DEBUG)?.booleanValue() ?: false
            isTrace = node.get(FIELD_OPTIONS_TRACE)?.booleanValue() ?: false
        }
    }
}
