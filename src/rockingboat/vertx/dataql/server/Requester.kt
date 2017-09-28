package rockingboat.vertx.dataql.server

import io.vertx.core.json.JsonObject
import rockingboat.vertx.dataql.server.data.OneRequest
import rockingboat.vertx.dataql.server.interfaces.IRequester


class Requester : IRequester {
    suspend override fun goTest(): JsonObject? {
        return parse("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test4.json")
    }

    suspend override fun go(request: OneRequest): JsonObject? {
        return parse("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test4.json")
    }

}