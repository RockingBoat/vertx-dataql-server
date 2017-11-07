package rockingboat.vertx.dataql.builder

class DataQLDSLQuery {
    var queries: MutableList<DataQLDSLQueryItem> = mutableListOf()

    @Suppress("unused")
    fun add(init: DataQLDSLQueryItem.() -> Unit): DataQLDSLQueryItem {
        val item = DataQLDSLQueryItem()
        item.init()
        queries.add(item)
        return item
    }
}
