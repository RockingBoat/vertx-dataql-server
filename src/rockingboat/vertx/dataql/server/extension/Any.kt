package rockingboat.vertx.dataql.server.extension

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

fun Any.prettyPrinted(): String? = when (this) {
    is JsonObject -> encodePrettily()
    is JsonArray  -> encodePrettily()
    else          -> null
}


