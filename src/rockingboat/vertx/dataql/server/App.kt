package rockingboat.vertx.dataql.server


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.salomonbrys.kodein.instance
import de.jupf.staticlog.Log
import io.vertx.core.json.JsonObject
import rockingboat.vertx.dataql.server.builder.DataQLDSL
import rockingboat.vertx.dataql.server.extension.toListOfJsonPathKey
import rockingboat.vertx.dataql.server.interfaces.IRequester
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance


fun DataQLDSL.toJsonObject() = JsonObject(this.toMap())

val queryNet22 = DataQLDSL {
    options {
        isDebug = true
        isTrace = true
    }
    query {
        add {
            service = "shopcart"
            version = "v1"
            method = "get"
            filter("list.<person_id, calculated_rank>")
            fields(mapOf(
                "list.person_id" to "id",
                "list.calculated_rank" to "id"
            ))
            extract(mapOf(
                "list.full_url" to "url",
                "list.calculated_rank" to "cr"
            ))
        }
        add {
            service = "account"
            method = "info"
            version = "v1"
            bindTo = "someAccount"
            subQueries {
                add {
                    service = "SubQuery"
                    version = "v1"
                    method = "get"
                    extract(mapOf(
                        "list.full_url" to "url",
                        "list.calculated_rank" to "cr"
                    ))
                }
            }
        }
    }
}.toJsonObject()


private val mapper = ObjectMapper().registerKotlinModule()

class OutputForm {
    var data: Any? = null
    var errors: Map<String, Any>? = null

    fun toJsonString(isPrettyPrinted: Boolean = false): String = when (isPrettyPrinted) {
        true  -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
        false -> mapper.writeValueAsString(this)
    }
}


fun MutableList<KeysTreeNode>.findByKey(key: String) = this.firstOrNull { it.key == key }


data class KeysTreeNode(val key: String?) {
    var opt = mutableListOf<TreeNodeType>()
    var children = mutableListOf<KeysTreeNode>()
    var parent: KeysTreeNode? = null

    fun addChild(node: KeysTreeNode) {
        children.add(node)
        node.parent = this
    }


    fun abc(currentNode: KeysTreeNode, key: String, type: TreeNodeType): KeysTreeNode {
        var foundNode = currentNode.children.findByKey(key)
        if (foundNode == null) {
            val newNode = KeysTreeNode(key)
            newNode.parent = currentNode
            currentNode.addChild(newNode)
            foundNode = newNode
        }

        if (!foundNode.opt.contains(type))
            foundNode.opt.add(type)

        return foundNode
    }

    fun addChild(key: String, type: TreeNodeType) {
        var currentNode = this
        val jsonPath = key.toListOfJsonPathKey()
        jsonPath.forEach { path ->
            currentNode = when (path) {
                is JsonPathKey.ObjectKey  -> abc(currentNode, path.key, type)
                is JsonPathKey.ObjectKeys -> path.set.toList().map { abc(currentNode, it, type) }.first()
                is JsonPathKey.FullArray  -> currentNode
                else                      -> throw IllegalArgumentException("kkk ${key} ${path}")
            }
        }
    }

    fun toString(identLevel: Int): String {
        var s = " ".repeat(identLevel * 4) + "($key)"
        s += "\t[ ${opt.joinToString(", ") { it.toString() }} ]"


        if (!children.isEmpty()) {
            s += "\n" + children.joinToString("\n") { it.toString(identLevel + 1)}
        }
        return s

    }

    override fun toString(): String {
        return toString(0)
    }
}


sealed class TreeNodeType {
    data class Filter(val key: String) : TreeNodeType()
    data class Extract(val key: String) : TreeNodeType()
    data class Field(val key: String) : TreeNodeType()
    object None : TreeNodeType()
    object NodeRoot : TreeNodeType()
}


fun main(args: Array<String>) {


    val mapper = ObjectMapper().registerKotlinModule()
    Log.newFormat {
        line(text("["), date("yyyy-MM-dd HH:mm:ss.SSS"), text("]"), space, occurrence, space, level, text(":"), space, message)
    }

    val requester = di.instance<IRequester>()
    val str = File("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test1.json").readText()
    val queryNet = mapper.readValue<DataQLForm>(str)


    val rootNode = KeysTreeNode(null)

    queryNet.queries?.forEach { query ->
        query.filter?.forEach {

            rootNode.addChild(it, TreeNodeType.Filter(it))

        }

        query.fields?.keys?.forEach {

            rootNode.addChild(it, TreeNodeType.Field(it))
        }
    }


//    val r = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode)
    Log.debug("\n" + rootNode.toString(0))


//    val outputData = OutputForm()
//
//    val queriesCount = queryNet.queries?.count() ?: 0
//
//    queryNet.queries?.map {
//        OneRequest(it.rootQuery(), queryNet.options, it.subQueries)
//    }?.forEach { request ->
//
//
//        requester.goTest2().let { obj ->
//
//            when (obj) {
//                is RequesterResponse.Object -> {
//                    val r = obj.filter("list.person_id")
//                    Log.debug(r.toString())
//                }
//
//                else                        -> Log.debug("Not Implemented")
//            }
//
//
//            if (obj is JsonObject) {
//                val outputObj = obj.let {
//                    request.query?.filter?.let { obj.filter(it) }
//                }.let {
//                    request.query?.extract?.let { obj.extract(it) }
//                }
//
//                if (outputData.data == null) {
//                    if (queriesCount > 1) {
//                        outputData.data = mutableMapOf<String, Any?>()
//                    } else {
//                        outputData.data = outputObj?.map
//                    }
//                }
//                if (queriesCount > 1) {
//                    request.query?.outputBindPath()?.let { name ->
//                        (outputData.data as? MutableMap<String, Any?>)?.let { it[name] = outputObj?.map }
//                    }
//                }
//            }
}


//    }
//
//
//    Log.debug(outputData.toJsonString(true))


//    val queryNet = DataQLDSL(jsonString = str).toJsonObject()


//    val obj = parse("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test2.json")


//    queryNet.let {
//        val queries = it.getOrNull<JsonArray>(DATA_QL_FIELD_QUERIES) ?: throw IllegalArgumentException("kkk") // TODO: EXCEPTION
//        val options = it.getOrNull("o") ?: JsonObject()
//        val request = it.getOrNull<JsonObject>("r")
//
//        val output = JsonObject(mapOf("data" to JsonObject()))
//        val data = output.getJsonObject("data")
//
//        val servicesQueries = queries.mapNotNull { it as? JsonObject }
//            .mapNotNull {
//
//                val serviceQuery = tryOrNull { ServiceQuery(it) }
//                if (serviceQuery != null) {
//                    val serviceOptions = Options(options)
//                    OneRequest(serviceQuery, serviceOptions)
//                } else {
//                    null
//                }
//            }
//
//        servicesQueries.forEach { oneRequest ->
//            val result = requester.goTest()
//            result?.let { obj ->
//
//                val subRequestData = oneRequest.query.extract?.let { obj.extract(it) }
//
//                val outputObject = obj.let {
//                    oneRequest.query.filter?.let { obj.filter(it) }
//                }?.let {
//                    oneRequest.query.fields?.let { obj.extract(it) }
//                }
//
//                data.map[oneRequest.query.name] = outputObject
//            }
//        }
//
//
//        Log.debug(output.prettyPrinted() ?: "No Output")
//
//    }


//}
