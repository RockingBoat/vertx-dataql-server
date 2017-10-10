package rockingboat.vertx.dataql.server.builder

class DataQLDSLOptions {
    var isDebug = false
    var isTrace = false

    @Suppress("unused")
    fun toMap() = mapOf(
        "isDebug" to isDebug,
        "isTrace" to isTrace
    )

}
