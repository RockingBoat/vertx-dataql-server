package rockingboat.vertx.dataql

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.salomonbrys.kodein.instance
import de.jupf.staticlog.Log
import rockingboat.vertx.dataql.builder.DataQLDSL
import rockingboat.vertx.dataql.builder.registerDataQLDSL
import rockingboat.vertx.dataql.interfaces.IRequester
import rockingboat.vertx.dataql.jjson.JJson
import rockingboat.vertx.dataql.jjson.JJsonKeyTreeNode
import rockingboat.vertx.dataql.jjson.JJsonKeyTreeNodeVariant
import rockingboat.vertx.dataql.jjson.registerJJson
import java.io.File

val queryNet22 = DataQLDSL {
    options {
        isDebug = false
        isTrace = true
    }
    query {
        add {
            service = "shopcart"
            version = "v1"
            method = "get"
            filter("list.<person_id, calculated_rank>")
            fields("list.person_id" to "id", "list.calculated_rank" to "id")
            extract("list.full_url" to "url", "list.calculated_rank" to "cr")
            request = mapOf("a" to 1, "b" to 2)

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
                    extract("list.full_url" to "url", "list.calculated_rank" to "cr")
                }
            }
        }
    }
}

val mapper = ObjectMapper().registerKotlinModule().registerJJson().registerDataQLDSL()

class OutputForm {
    var data: Any? = null
    var errors: Map<String, Any>? = null

    fun toJsonString(isPrettyPrinted: Boolean = false): String = when (isPrettyPrinted) {
        true  -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
        false -> mapper.writeValueAsString(this)
    }
}

class OneOutput {
    //    @JsonProperty(DATA_QL_FIELD_BIND_TO)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    var filter: JJson? = null

    //    @JsonProperty(DATA_QL_FIELD_BIND_TO)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    var fields: JJson? = null

    //    @JsonProperty(DATA_QL_FIELD_BIND_TO)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    var extracted: JJson? = null
}

fun main(args: Array<String>) {

//    Log.newFormat {
//        line(text("["), date("yyyy-MM-dd HH:mm:ss.SSS"), text("]"), space, occurrence, space, level, text(":"), space, message)
//    }
//
//    val a = queryNet22.toString()
//    val b = mapper.readValue<DataQLDSL>(a)
//    Log.debug(queryNet22.toString())
//
    val requester = di.instance<IRequester>()
    val str = File("/Work/Backend/DataQL/api-gateway/src/rockingboat/vertx/dataql/test1.json").readText()
    val queryNet = mapper.readValue<DataQLDSL>(str)

    val mList = mutableListOf<OneOutput>()
    Log.debug("Start")

    queryNet.queries?.queries?.forEach { query ->

        requester.goTest3().let { obj ->
            val rootNode = JJsonKeyTreeNode(query, obj)
            val oneOutput = OneOutput()
            Log.debug(rootNode.toString())

//            if (query.hasSubRequests && query.extract != null) {
//                oneOutput.extracted = JJson.Object().apply {
//                    rootNode.extracted.value.forEach { item ->
//                        query.extract[item.key]?.let {
//                            set(it, item.value)
//                        }
//                    }
//                }
//            }
//
            if (query.filter != null) {
                oneOutput.filter = obj
            } else if (query.fields != null) {
                oneOutput.fields = JJson.Object().apply {
                    rootNode.fields.value.forEach { item ->
                        Log.debug(item.key)
                        Log.debug(item.value.toString())
//                        query.fields[item.key]?.let {
//                            set(it, item.value)
//                        }
                    }
                }
            }
//
//
//
//            Log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oneOutput))
//
//
////        fields.forEach { key, value ->
////            query.fields?.get(key)?.let {
////                outputFields[it] = value
////            }
////        }
//
////// TODO: Need Make^ Not Delete
////            fields.values.first().let {
////                if (it is JArrayErased) {
////                    val r = mutableListOf<Map<String, Any?>>()
////                    val cnt = it.count()
////                    val keys =  fields.keys.mapNotNull {query.fields?.get(it)}
////
////
////
////
////                    fields.forEach { key, value ->
////                        query.fields?.get(key)?.let {
////                            outputFields[it] = value
////                        }
////                    }
////
////                    for (i in 0..cnt) {
////
////                        val mm = mutableMapOf<String, Any>()
////                        keys.forEach {
////                            mm[it] =
////                        }
////
////                        r.add(mm)
////                    }
////                }
////            }
//
//
////        val extractedMap = rootNode.extracted
////        val extractedFields = mutableMapOf<String, Any?>()
////
////
////        extractedMap.forEach { key, value ->
////            query.extract?.get(key)?.let {
////                outputFields[it] = value
////            }
////        }
//
//
//        }
//    }
//
//    Log.debug("End")
        }
    }
}










