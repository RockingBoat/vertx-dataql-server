package rockingboat.vertx.dataql.server

import com.fasterxml.jackson.module.kotlin.readValue
import rockingboat.vertx.dataql.server.interfaces.IRequester
import rockingboat.vertx.dataql.server.jjson.JJson
import java.io.File

class Requester : IRequester {
    override fun goTest3(): JJson {
        val str = File("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test4.json").readText()
        tryOrNull { mapper.readValue<JJson>(str) }?.let { return it }
        return JJson.Null
    }

//    override fun goTest2(): RequesterResponse {
//        val str = File("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test2.json").readText()
//
//        tryOrNull { mapper.readValue<JJson>(str) }?.let {
//
//            val o = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(it)
//            Log.debug(o)
//        }
//        tryOrNull { mapper.readValue<JArray>(str) }?.let { return RequesterResponse.Array(it) }
//        tryOrNull { mapper.readValue<JMap>(str) }?.let { return RequesterResponse.Object(it) }
//        return RequesterResponse.Error
//    }
//
//
//    override fun goTest(): JsonObject? {
//        return parse("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test4.json")
//    }
//
//    override fun go(request: OneRequest): JsonObject? {
//        return parse("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test4.json")
//    }


}