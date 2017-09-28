package rockingboat.vertx.dataql.server


import com.github.salomonbrys.kodein.instance
import rockingboat.vertx.dataql.server.jsonExtension.JsonPathKey
import rockingboat.vertx.dataql.server.jsonExtension.regexArrayIndex
import rockingboat.vertx.dataql.server.jsonExtension.regexFullArray
import rockingboat.vertx.dataql.server.jsonExtension.regexSet
import rockingboat.vertx.dataql.server.jsonExtension.regexSplitByComma
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.Json
import io.vertx.kotlin.core.json.get
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import rockingboat.vertx.dataql.server.data.OneRequest
import rockingboat.vertx.dataql.server.data.Options
import rockingboat.vertx.dataql.server.data.ServiceQuery
import rockingboat.vertx.dataql.server.interfaces.IRequester


private val regexSplitByDot = Regex("[/\\.]")

//fun List<List<JsonPathKey>>.toStringList(): List<String> {
//    return this.map {
//        it.joinToString(".") {
//            when (it) {
//
//                is JsonPathKey.ObjectKey  -> it.key
//                is JsonPathKey.ArrayIndex -> it.idx.toString()
//                JsonPathKey.FullArray     -> "*"
//                else                      -> throw java.lang.IllegalArgumentException("WTF MAN")
//            }
//        }
//
//    }
//}
//
//fun JsonBase.expandJsonPathKey(key: String): List<List<JsonPathKey>> {
//    var list = mutableListOf<MutableList<JsonPathKey>>()
//    list.add(mutableListOf())
//
//    key.toJsonPathKeyList().map { key ->
//        when (key) {
//            is JsonPathKey.ObjectKeys -> {
//                list = list.map { l ->
//                    key.set.map {
//                        l.map { it }.toMutableList().apply {
//                            add(JsonPathKey.ObjectKey(it))
//                        }
//                    }
//                }.flatten().toMutableList()
//
//            }
//
//            else                      -> list.forEach { it.add(key) }
//        }
//    }
//
//    return list
//}
//
//
////}
//
//


class JsonKeyPath(private val key: String) {
    //    val isOutputArray: Boolean by lazy { items.count { it is JsonPathKey.FullArray } > 0 }
    private val items: List<JsonPathKey> by lazy {
        key.split("[/\\.]".toRegex()).map {
            this.keyToJsonPathKey(it)
        }
    }

    fun t1(obj: Any) {
        var objCopy: Any = when (obj) {
            is JsonArray  -> obj.copy()
            is JsonObject -> obj.copy()
            else          -> throw IllegalAccessError("aaa")
        }

        val objCopyOrig = objCopy

        items.filterNot { it is JsonPathKey.FullArray }
            .forEach { objCopy = t2(objCopy, it) }

        println(objCopy.prettyPrinted())
//        println(objCopyOrig.toJsonString(true))


    }

    fun t2(objCopy: Any, key: JsonPathKey): Any = when (key) {

        is JsonPathKey.ObjectKey  -> when (objCopy) {
            is JsonObject -> objCopy.get<Any>(key.key)
            is JsonArray  -> objCopy.removeAnotherKeysThanThis(key.key)
            else          -> throw IllegalAccessError("aaa")
        }

        is JsonPathKey.ObjectKeys -> when (objCopy) {
            is JsonArray  -> objCopy.removeAnotherKeysThanThese(key.set.toList())
            is JsonObject -> objCopy.removeAnotherKeysThanThese(key.set.toList())
            else          -> throw IllegalAccessError("aaa")
        }

        is JsonPathKey.ArrayIndex -> (objCopy as JsonArray)[key.idx]
        else                      -> throw IllegalAccessError("aaa")
    }


    private fun keyToJsonPathKey(key: String): JsonPathKey {
        regexArrayIndex.find(key)
            ?.groups
            ?.elementAtOrNull(1)
            ?.value
            ?.let { return JsonPathKey.ArrayIndex(it.toInt()) }


        regexFullArray.find(key)
            ?.groups
            ?.elementAtOrNull(0)
            ?.value
            ?.let { return JsonPathKey.FullArray }

        regexSet.find(key)
            ?.groups
            ?.elementAtOrNull(1)
            ?.value
            ?.let { return JsonPathKey.ObjectKeys(it.split(regexSplitByComma).toSet()) }

        return JsonPathKey.ObjectKey(key)
    }
}


fun main(args: Array<String>) {

    val requester = di.instance<IRequester>()


//    val obj = parse("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test2.json")
    val query = parse("/Work/projects/vertx/api-gateway/src/rockingboat/vertx/dataql/server/test1.json")
//
//
    query?.let {
        val queries = it.get<JsonArray?>("q") ?: throw IllegalArgumentException("kkk") // TODO: EXCEPTION
        val options = it.get<JsonObject?>("o")
        val request = it.get<JsonObject?>("r")

        val servicesQueries = queries.map { query ->

            if (options != null && query is JsonObject) {
                val serviceQuery = tryOrNull { ServiceQuery(query) }
                val serviceOptions = Options(options)

                if (serviceQuery != null) {
                    val omeR = OneRequest(serviceQuery, serviceOptions)
                    println(JsonObject.mapFrom(omeR).prettyPrinted())

                }
            }

        }

        // HERE Requests

        runBlocking {
            val result = requester.goTest()

            val c = JsonKeyPath("list.*.<person_id, calculated_rank>")
//            c.t1(result!!)
        }


        println("QQ")

    } ?: throw IllegalArgumentException("kkk") // TODO: EXCEPTION
//
//

//    val a = obj?.expandJsonPathKey("list.*.<person_id, calculated_rank>")?.toStringList()
//    val b = obj?.lookup<Any>("list.person_id")
//    println(b)

//


//    println(obj?.toJsonString(true))

//    o.setPath("it.da.[0].buguggi", "Hello")
//    o.setPath("it.da.buguggi", JsonArray(listOf("qqqq", "kkkk")))
//    println(o.getByPath("it.da.bug"))
//    o.setPath("it.da.[2].buguggi", "Hello")
//    o.setForKeyPath("it.da.buggi", "Hello")
//    o.setForKeyPath("it.da.buggi2", )
//    println(o.toJsonString(true))
//    query?.obj("blend")?.let {
//        val result = JsonObject()
//        it.string("from")?.let {
//
//            val aq = obj?.keyType2("list.[0].dd.qq")
//            result["data"] = obj?.keyType<Any>(it)
//        }
//        print(result.toJsonString(prettyPrint = true))
//    }


//    val extr = parse("/Work/projects/vertx/api-gateway/src/backend/mobile/apiGateway/test3.json")
//    extr?.obj("extract")?.let {
//        val res = ArrayList<Pair<String, JsonArray<out Any>>>()
//        it.forEach { key, valueAny ->
//            (valueAny as? String)?.let {
//                res.add(Pair(it, obj?.lookup<Any>(key) ?: JsonArray<JsonObject>()))
//            }
//        }
//
//        mm(res)
//    }
}


//@Suppress("UNCHECKED_CAST")
//private fun <T> Any?.ensureObject() : JsonObject<T> = JsonO
//    if (this is JsonArray<*>) this as JsonArray<T>
//    else JsonArray(this as T)


//
//
//    val lastKey = way.last()
//
//    val wayArray = way.dropLast(1)
//
//    wayArray.forEachIndexed { index, part ->
//        val arrayIdx = part.jsonArrayIndex()
//        val nextPart = if (index + 1 < wayArray.count()) {
//            wayArray[index + 1].jsonArrayIndex()
//        } else {
//            lastKey.jsonArrayIndex()
//        }
//
//        val emptyValue = if (nextPart == null) {
//            JsonObject()
//        } else {
//            JsonArray<Any>()
//        }
//
//
//        if (arrayIdx != null) {
//            when (obj) {
//                is JsonArray<*> -> {
//
//                    var sObj = (obj as JsonArray<*>).getOrNull(arrayIdx)
//                    if (sObj == null) {
//                        sObj = emptyValue
//                        (obj as JsonArray<Any>).add(arrayIdx, sObj)
//                    }
//                    obj = sObj as JsonBase
//                }
//                else            -> throw IllegalArgumentException("unsupported type of it ")
//            }
//        } else {
//            when (obj) {
//                is JsonObject -> {
//                    var sObj = (obj as JsonObject)[part]
//
//                    if (sObj == null) {
//                        sObj = emptyValue
//                        (obj as JsonObject)[part] = sObj
//                    }
//                    obj = sObj as JsonBase
//                }
//                else          -> throw IllegalArgumentException("unsupported type of it ")
//            }
//        }
//    }
//
//    val lastKeyIndex = lastKey.jsonArrayIndex()
//
//    if (lastKeyIndex != null) {
//        when (obj) {
//            is JsonArray<*> -> {
//                (obj as JsonArray<Any>).add(lastKeyIndex, value)
//            }
//            else            -> throw IllegalArgumentException("unsupported type of it ")
//        }
//    } else {
//        when (obj) {
//            is JsonObject -> {
//                (obj as JsonObject)[lastKey] = value
//            }
//            else          -> throw IllegalArgumentException("unsupported type of it ")
//        }
//    }
//
//}

//fun JsonBase.setForKeyPath(key: String, value: Any) {
//    val way = key.split("[/\\.]".toRegex()).filter { it != "" }
//    var obj = this as JsonObject
//    val lastKey = way.last()
//
//
//    way.dropLast(1).forEach {
//        val arrayIdx = it.jsonArrayIndex()
//        if (arrayIdx != null) {
//            var sObj = o
//        } else {
//
//        }
//
//        var sObj = obj[it]
//        if (sObj == null) {
//
//
//            if (arrayIdx != null ) {
//                sObj = JsonArray<Any>()
//                obj
//            } else {
//                sObj = JsonObject()
//            }
//
//            obj[it] = sObj
//
//        }
//
//        obj = sObj as JsonObject
//    }
//
//    obj[lastKey] = value
//
//
//}
//

//fun JsonBase.keyType2(key: String): Any =
//    key.split("[/\\.]".toRegex())
//        .filter { it != "" }
//        .fold(this) { j, part ->
//            when (j) {
//                is JsonArray<*> -> {
//                    val idx = Regex("^\\[(\\d+)\\]$").find(part)
//                    idx?.let {
//                        if (it.groups.count() == 2) {
//                            it.groups[1]?.value?.let {
//                                return j[it.toInt()] as JsonBase
//                            }
//                        }
//                    }
//                    return j[part]
//                }
//                is JsonObject   -> j[part] as JsonBase
//                else            -> throw IllegalArgumentException("unsupported type of j = $j")
//            }
//        }
//

/*
//
////fun buildAnswer(): JsonObject {
////    val result = JsonObject()
////
////}
//
//fun mm(lists: List<Pair<String, JsonArray<out Any>>>) {
//
//    val tIdx = lists.first().second.size
//    val results = ArrayList<JsonObject>()
//    results.ensureCapacity(tIdx)
//
//    (0 until tIdx).forEach { results.add(it, JsonObject()) }
//
//    lists.forEachIndexed { index, r ->
//        r.second.forEachIndexed { idx, any ->
//            results[idx][r.first] = any
//        }
//    }
//
//    println(results)
//
//
//}
////    println(obj?.lookup<Any>(extract))
////    obj?.set("person_id", JsonObject())
////
////    obj?.lookup<Any>(extract)?.forEachIndexed { index, any ->
////        results.add(index, JsonObject(mapOf(Pair("personId", any))))
////    }
////
////    obj?.lookup<Any>(extract2)?.forEachIndexed { index, any ->
////        var item = results[index].set("cr", any)
////    }
////
////
////    println(results)
//
////    println(obj?.lookup<Any>(extract2))
////    for (item in extractList) {
////        println(item)
////        obj.is
////        println (obj.obj(item))
////    }
////    print(extractList)
////    val max = 1_000_000
//////    val time = measureNanoTime {
////        for (i in 0..max) {
////            val obj = parse("/Work/projects/vertx/api-gateway/src/backend/mobile/apiGateway/test1.json")
////        }
////
////    }
//
////    print(time/max)
////    simpleMeasureTest {
////
////    }
////}
//
//
//
//fun simpleMeasureTest(
//    ITERATIONS: Int = 1000,
//    TEST_COUNT: Int = 100,
//    WARM_COUNT: Int = 100,
//    callback: () -> Unit
//) {
//    val results = ArrayList<Long>()
//    var totalTime = 0L
//    var t = 0
//
//    println("$PRINT_REFIX -> go")
//
//    while (++t <= TEST_COUNT + WARM_COUNT) {
//        val startTime = System.currentTimeMillis()
//
//        var i = 0
//        while (i++ < ITERATIONS)
//            callback()
//
//        if (t <= WARM_COUNT) {
////            println("$PRINT_REFIX Warming $t of $WARM_COUNT")
//            continue
//        }
//
//        val time = System.currentTimeMillis() - startTime
////        println(PRINT_REFIX + " " + time.toString() + "ms")
//
//        results.add(time)
//        totalTime += time
//    }
//
//    results.sort()
//
//    val average = totalTime / TEST_COUNT
//    val median = results[results.size / 2]
//
//    println("$PRINT_REFIX -> average=${average}ms / median=${median}ms")
//}
//
///**
// * Used to filter console messages easily
// */
//private val PRINT_REFIX = "[TimeTest]"
//
////import io.vertx.core.AbstractVerticle
////import io.vertx.core.Future
////import io.vertx.core.http.HttpServerResponse
////import io.vertx.core.json.Json
////import io.vertx.core.json.JsonArray
////import io.vertx.core.json.JsonObject
////import io.vertx.ext.web.Router
////import io.vertx.kotlin.core.http.HttpServerOptions
////import io.vertx.kotlin.core.json.JsonArray
////import mymoney.controller.ControllerMonthStorage
////import mymoney.controller.ControllerRoot
////import mymoney.controller.ControllerTransactions
////import mymoney.model.ModelTransaction
////import vertx.helpers.web.controllers
////import java.util.Calendar
////import java.util.Date
////
////
/////**
//// * Created by s.suslov on 12.06.17.
//// */
////
////@VerticleClass class App : AbstractVerticle() {
////    private val router = Router.router(vertx)
////
////    override fun start(startFuture: Future<Void>?) {
////
////        vertx.createHttpServer(
////            HttpServerOptions(
////                port = Integer.getInteger("http.port", 8080),
////                acceptBacklog = 500
////            ))
////            .controllers(
////                ControllerMonthStorage::class,
////                ControllerTransactions::class,
////                ControllerRoot::class
////            )
////            .listen { result ->
////                if (result.succeeded()) {
////                    startFuture?.complete()
////                } else {
////                    startFuture?.fail(result.cause())
////                }
////            }
////    }
////}
////
////
////val Date.beginningOfMonth: Date
////    get() {
////        val calendar = Calendar.getInstance()
////        calendar.time = this
////        calendar.set(Calendar.DATE, 1)
////        calendar.set(Calendar.HOUR_OF_DAY, 0)
////        calendar.set(Calendar.MINUTE, 0)
////        calendar.set(Calendar.SECOND, 0)
////        calendar.set(Calendar.MILLISECOND, 0)
////        calendar.set(Calendar.ZONE_OFFSET, 0)
////        return calendar.time
////    }
////
////val Date.endOfMonth: Date
////    get() {
////        val calendar = Calendar.getInstance()
////        val lastDay = calendar.getActualMaximum(Calendar.DATE)
////        calendar.time = this
////        calendar.set(Calendar.DATE, lastDay)
////        calendar.set(Calendar.HOUR_OF_DAY, 0)
////        calendar.set(Calendar.MINUTE, 0)
////        calendar.set(Calendar.SECOND, 0)
////        calendar.set(Calendar.MILLISECOND, 0)
////        calendar.set(Calendar.ZONE_OFFSET, 0)
////        return calendar.time
////    }
////
////
//////fun HttpServerResponse.endJSend(data: Any?, code: Int = 0) {
//////    putHeader("Content-Type", "application/json; charset=utf-8")
//////        .end(Json.encodePrettily(JsonObject().also {
//////            it.putObject("data", data)
//////            it.put("code", code)
//////        }))
//////}
//////
//////fun JsonObject.putObject(key: String, data: Any?) {
//////    if (data == null)
//////        this.putNull(key)
//////    else
//////        this.put(key, data)
//////}
    */