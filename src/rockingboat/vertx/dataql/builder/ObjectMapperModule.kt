package rockingboat.vertx.dataql.builder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule

fun ObjectMapper.registerDataQLDSL(): ObjectMapper {
    val sm = SimpleModule()
            .addSerializer(DataQLDSL::class.java, DataQLDSLJsonSerializer())
            .addSerializer(DataQLDSLOptions::class.java, DataQLDSLOptionsJsonSerializer())
            .addSerializer(DataQLDSLQueryItem::class.java, DataQLDSLQueryItemJsonSerializer())
            .addSerializer(DataQLDSLQuery::class.java, DataQLDSLQueryJsonSerializer())
            .addDeserializer(DataQLDSL::class.java, DataQLDSLJsonDeserializer())
            .addDeserializer(DataQLDSLOptions::class.java, DataQLDSLOptionsJsonDeserializer())
            .addDeserializer(DataQLDSLQueryItem::class.java, DataQLDSLQueryItemJsonDeserializer())



    registerModule(sm)

    return this
}