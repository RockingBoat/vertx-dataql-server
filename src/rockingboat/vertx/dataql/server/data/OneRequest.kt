package rockingboat.vertx.dataql.server.data

data class OneRequest(val query: ServiceQuery, val options: Options, val subRequest: List<OneRequest>? = null)