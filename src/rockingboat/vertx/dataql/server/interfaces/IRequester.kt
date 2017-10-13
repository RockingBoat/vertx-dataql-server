package rockingboat.vertx.dataql.server.interfaces

import io.vertx.core.json.JsonObject
import rockingboat.vertx.dataql.server.RequesterResponse
import rockingboat.vertx.dataql.server.data.OneRequest
import rockingboat.vertx.dataql.server.jjson.JJson

interface IRequester {

    fun goTest3(): JJson
}