package rockingboat.vertx.dataql.builder

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import rockingboat.vertx.dataql.mapper

const val FIELD_QUERIES = "q"
const val FIELD_OPTIONS = "o"
const val FIELD_SERVICE = "s"
const val FIELD_VERSION = "v"
const val FIELD_METHOD = "m"
const val FIELD_BIND_TO = "b"
const val FIELD_FIELDS = "fs"
const val FIELD_EXTRACT = "e"
const val FIELD_FILTER = "fr"
const val FIELD_SUB_QUERIES = "sq"
const val FIELD_REQUEST = "r"
const val FIELD_FILTER_ON_SERVICE = "fos"
const val FIELD_OPTIONS_DEBUG = "isDebug"
const val FIELD_OPTIONS_TRACE = "isTrace"



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = DataQLDSLJsonDeserializer::class)
class DataQLDSL() {
    constructor(init: DataQLDSL.() -> Unit) : this() {
        this.init()
    }

    var options: DataQLDSLOptions? = null
    var queries: DataQLDSLQuery? = null
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

    override fun toString(): String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
}