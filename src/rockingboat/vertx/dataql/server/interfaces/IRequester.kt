package rockingboat.vertx.dataql.server.interfaces

import io.vertx.core.json.JsonObject
import rockingboat.vertx.dataql.server.data.OneRequest

interface IRequester {
    suspend fun go(request: OneRequest): JsonObject?
    suspend fun goTest(): JsonObject?
}