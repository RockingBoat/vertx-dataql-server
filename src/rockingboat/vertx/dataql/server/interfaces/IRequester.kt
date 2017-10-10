package rockingboat.vertx.dataql.server.interfaces

import io.vertx.core.json.JsonObject
import rockingboat.vertx.dataql.server.RequesterResponse
import rockingboat.vertx.dataql.server.data.OneRequest

interface IRequester {
    fun go(request: OneRequest): JsonObject?
    fun goTest(): JsonObject?
    fun goTest2(): RequesterResponse
}