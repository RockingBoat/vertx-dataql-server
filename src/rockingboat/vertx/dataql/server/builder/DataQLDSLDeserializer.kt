package rockingboat.vertx.dataql.server.builder

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import rockingboat.vertx.dataql.server.mapper


class DataQLDSLJsonDeserializer : JsonDeserializer<DataQLDSL>() {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): DataQLDSL {
        val output = DataQLDSL()
        p?.let {

            val oc = p.codec
            val node = oc.readTree<JsonNode>(p)

            node.get(FIELD_OPTIONS)?.let {
                output.options = mapper.readValue(it.asText(), DataQLDSLOptions::class.java)
            }
        }



        return output
    }
}


class DataQLDSLOptionsJsonDeserializer : JsonDeserializer<DataQLDSLOptions>() {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): DataQLDSLOptions {
        val output = DataQLDSLOptions()
        p?.let {

            val oc = p.codec
            val node = oc.readTree<JsonNode>(p)
            output.isDebug = node.get(FIELD_OPTIONS_DEBUG).asBoolean(false)
            output.isTrace = node.get(FIELD_OPTIONS_TRACE).asBoolean(false)
        }

        return output
    }
}
