package rockingboat.vertx.dataql.server.builder

class DataQLDSLQuery {
    private var queries: MutableList<DataQLDSLQueryItem> = mutableListOf()

    @Suppress("unused")
    fun add(init: DataQLDSLQueryItem.() -> Unit): DataQLDSLQueryItem {
        val item = DataQLDSLQueryItem()
        item.init()
        queries.add(item)
        return item
    }

    @Suppress("unused")
    fun toList() = queries.map { it.toMap() }
}
