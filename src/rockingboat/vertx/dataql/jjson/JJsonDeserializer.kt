package rockingboat.vertx.dataql.jjson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType


class JJsonDeserializer : JsonDeserializer<JJson>() {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): JJson {
        if (p == null)
            return JJson.Null

        val oc = p.codec
        val node = oc.readTree<JsonNode>(p)

        return when (node.nodeType) {
            JsonNodeType.OBJECT -> JJson.Object(node)
            JsonNodeType.ARRAY  -> JJson.Array(node)
            else                -> JJson.Null
        }
    }
}
