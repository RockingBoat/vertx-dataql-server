package rockingboat.vertx.dataql.server

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_BIND_TO
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_EXTRACT
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_FIELDS
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_FILTER
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_METHOD
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_OPTIONS
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_QUERIES
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_REQUEST
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_SERVICE
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_SUB_QUERIES
import rockingboat.vertx.dataql.server.builder.DATA_QL_FIELD_VERSION


open class DataQLFormJson {
    fun toJsonString(isPrettyPrinted: Boolean = false): String = when (isPrettyPrinted) {
        true  -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
        false -> mapper.writeValueAsString(this)
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class DataQLForm(
    @JsonProperty(DATA_QL_FIELD_OPTIONS)
    val options: DataQLFormOptions?,

    @JsonProperty(DATA_QL_FIELD_QUERIES)
    val queries: List<DataQLFormQuery>?
) : DataQLFormJson()

@Suppress("MemberVisibilityCanPrivate")
@JsonIgnoreProperties(ignoreUnknown = true)
data class DataQLFormQuery(
    @JsonProperty(DATA_QL_FIELD_SERVICE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val service: String?,

    @JsonProperty(DATA_QL_FIELD_BIND_TO)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val bindTo: String?,

    @JsonProperty(DATA_QL_FIELD_METHOD)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val method: String?,

    @JsonProperty(DATA_QL_FIELD_VERSION)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val version: String?,

    @JsonProperty(DATA_QL_FIELD_FIELDS)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val fields: Map<String, String>?,

    @JsonProperty(DATA_QL_FIELD_EXTRACT)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val extract: Map<String, String>?,

    @JsonProperty(DATA_QL_FIELD_FILTER)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val filter: List<String>?,

    @JsonProperty(DATA_QL_FIELD_SUB_QUERIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val subQueries: List<DataQLFormQuery>?,

    @JsonProperty(DATA_QL_FIELD_REQUEST)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val request: Any?
) : DataQLFormJson() {
    val hasSubRequests get() = (subQueries?.count() ?: 0) > 0
    val outputBindPath get() = bindTo ?: service
    fun rootQuery() = this.copy(subQueries = null)

    fun remote() = DataQLOneRemoteQuery(method, version, request)
    fun remoteWithProcessing() = DataQLOneRemoteQueryWithProcessing(method, version, request, fields, filter)

}

@JsonIgnoreProperties(ignoreUnknown = true)
data class DataQLFormOptions(val isDebug: Boolean?, val isTrace: Boolean?) : DataQLFormJson()


data class DataQLOneRemoteQuery(
    @JsonProperty(DATA_QL_FIELD_METHOD)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val method: String?,

    @JsonProperty(DATA_QL_FIELD_VERSION)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val version: String?,

    @JsonProperty(DATA_QL_FIELD_REQUEST)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val request: Any?
)

data class DataQLOneRemoteQueryWithProcessing(
    @JsonProperty(DATA_QL_FIELD_METHOD)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val method: String?,

    @JsonProperty(DATA_QL_FIELD_VERSION)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val version: String?,

    @JsonProperty(DATA_QL_FIELD_REQUEST)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val request: Any?,

    @JsonProperty(DATA_QL_FIELD_FIELDS)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val fields: Map<String, String>?,

    @JsonProperty(DATA_QL_FIELD_FILTER)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val filter: List<String>?
)