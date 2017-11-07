package rockingboat.vertx.dataql.jjson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule


fun ObjectMapper.registerJJson(): ObjectMapper {
    val sm = SimpleModule()
        .addDeserializer(JJson::class.java, JJsonDeserializer())
        .addSerializer(JJson::class.java, JJsonSerializer())
    registerModule(sm)

    return this
}