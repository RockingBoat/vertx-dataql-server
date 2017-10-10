package rockingboat.vertx.dataql.server.builder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

private val mapper = ObjectMapper().registerKotlinModule()

const val DATA_QL_FIELD_QUERIES = "q"
const val DATA_QL_FIELD_OPTIONS = "o"
const val DATA_QL_FIELD_SERVICE = "s"
const val DATA_QL_FIELD_VERSION = "v"
const val DATA_QL_FIELD_METHOD = "m"
const val DATA_QL_FIELD_BIND_TO = "b"
const val DATA_QL_FIELD_FIELDS = "fs"
const val DATA_QL_FIELD_EXTRACT = "e"
const val DATA_QL_FIELD_FILTER = "fr"
const val DATA_QL_FIELD_SUB_QUERIES = "sq"
const val DATA_QL_FIELD_REQUEST = "r"


class DataQLDSL(init: DataQLDSL.() -> Unit) {

    private var options: DataQLDSLOptions? = null
    private var queries: DataQLDSLQuery? = null

    init {
        this.init()
    }

    @Suppress("unused")
    fun options(init: DataQLDSLOptions.() -> Unit): DataQLDSLOptions {
        if (options == null)
            options = DataQLDSLOptions()
        options!!.init()
        return options!!
    }

    @Suppress("unused")
    fun query(init: DataQLDSLQuery.() -> Unit): DataQLDSLQuery {
        if (queries == null)
            queries = DataQLDSLQuery()
        queries!!.init()
        return queries!!
    }

    @Suppress("unused")
    fun toJsonString(isPrettyPrinted: Boolean = false): String = toMap().let {
        val map = this.toMap()
        if (isPrettyPrinted)
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)
        else
            mapper.writeValueAsString(map)
    }

    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        options?.let { map[DATA_QL_FIELD_OPTIONS] = it.toMap() }
        queries?.let { map[DATA_QL_FIELD_QUERIES] = it.toList() }
        return map
    }

}