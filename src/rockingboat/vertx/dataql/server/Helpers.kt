package rockingboat.vertx.dataql.server

import io.vertx.core.json.JsonObject
import java.io.File

//fun parse(name: String): JsonObject? = JsonObject(File(name).readText())
fun <T> tryOrNull(block: () -> T?): T? = try {
    block()
} catch (e: Throwable) {
    null
}
