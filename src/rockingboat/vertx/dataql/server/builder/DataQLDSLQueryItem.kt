package rockingboat.vertx.dataql.server.builder

class DataQLDSLQueryItem {
    var fields: MutableMap<String, String>? = null
    var extract: MutableMap<String, String>? = null
    var filter: MutableList<String>? = null
    var subQueries: DataQLDSLQuery? = null

    var service: String? = null
    var version: String? = null
    var method: String? = null
    var bindTo: String? = null
    var request: Any? = null
    var filterOnService: Boolean = false


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

    fun extract(vararg value: Pair<String, String>) = extract(value.toMap())

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
    fun fields(vararg value: Pair<String, String>) = fields(value.toMap())


    @Suppress("unused")
    fun field(key: String, value: String) {
        if (fields == null)
            fields = mutableMapOf()

        fields?.let { it[key] = value }
    }
}
