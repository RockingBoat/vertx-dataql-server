package rockingboat.vertx.dataql.server.builder

class DataQLDSLQueryItem {
    private var fields: MutableMap<String, String>? = null
    private var extract: MutableMap<String, String>? = null
    private var filter: MutableList<String>? = null
    private var subQueries: DataQLDSLQuery? = null

    var service: String? = null
    var version: String? = null
    var method: String? = null
    var bindTo: String? = null


    @Suppress("unused")
    fun subQueries(init: DataQLDSLQuery.() -> Unit): DataQLDSLQuery {
        if (subQueries == null) {
            subQueries = DataQLDSLQuery()
        }

        subQueries!!.init()
        return subQueries!!
    }

    @Suppress("unused")
    fun extract(value: Map<String, String>) {
        extract = value.toMutableMap()
    }

    @Suppress("unused")
    fun extract(key: String, value: String) {
        if (extract == null)
            extract = mutableMapOf()

        extract?.let { it[key] = value }
    }

    @Suppress("unused")
    fun filters(value: List<String>) {
        filter = value.toMutableList()
    }

    @Suppress("unused")
    fun filter(value: String) {
        if (filter == null)
            filter = mutableListOf()

        filter?.add(value)
    }

    @Suppress("unused")
    fun fields(value: Map<String, String>) {
        fields = value.toMutableMap()
    }

    @Suppress("unused")
    fun field(key: String, value: String) {
        if (fields == null)
            fields = mutableMapOf()

        fields?.let { it[key] = value }
    }

    @Suppress("unused")
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        service?.let { map[DATA_QL_FIELD_SERVICE] = it }
        version?.let { map[DATA_QL_FIELD_VERSION] = it }
        method?.let { map[DATA_QL_FIELD_METHOD] = it }
        bindTo?.let { map[DATA_QL_FIELD_BIND_TO] = it }
        fields?.let { map[DATA_QL_FIELD_FIELDS] = it }
        extract?.let { map[DATA_QL_FIELD_EXTRACT] = it }
        filter?.let { map[DATA_QL_FIELD_FILTER] = it }
        subQueries?.let { map[DATA_QL_FIELD_SUB_QUERIES] = it.toList() }
        return map
    }

}
