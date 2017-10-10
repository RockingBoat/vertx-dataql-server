package rockingboat.vertx.dataql.server.data

import rockingboat.vertx.dataql.server.DataQLFormOptions
import rockingboat.vertx.dataql.server.DataQLFormQuery

data class OneRequest(val query: DataQLFormQuery?,
                      val options: DataQLFormOptions?,
                      val subRequests: List<DataQLFormQuery>?)