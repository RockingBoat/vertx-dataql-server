package rockingboat.vertx.dataql.vertx

import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.handler.BodyHandler
import rockingboat.vertx.dataql.builder.DataQLDSLQueryItem
import rockingboat.vertx.helpers.web.body
import rockingboat.vertx.helpers.web.defaultRouter
import rockingboat.vertx.helpers.web.jsonResponse
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

data class DataQLRoutingItem(
        val method: String,
        val version: String,
        val function: KFunction<*>,
        val instance: Any
)

@Suppress("unused")
fun HttpServer.dataQLControllers(vararg args: KClass<*>): HttpServer {
    val router = this.defaultRouter()
    router.route().handler(BodyHandler.create())

    val routes = mutableSetOf<DataQLRoutingItem>()
    args.forEach { kClass ->

        val instance = kClass.companionObjectInstance ?: kClass.createInstance()

        instance::class.functions.map { function ->
            function.findAnnotation<DataQLMethod>()?.let {
                routes.add(DataQLRoutingItem(
                        method = it.method,
                        version = it.version,
                        function = function,
                        instance = instance
                ))
            }
        }
    }

    router.route(HttpMethod.POST, "/dataql")
            .handler { ctx ->
                val body = ctx.body<DataQLDSLQueryItem>()
                val r = routes.firstOrNull {
                    it.method == body.method && it.version == body.version
                } ?: throw Throwable("No route")

                r.function.call(r.instance, ctx, body)
            }
            .failureHandler { ctx ->
                ctx.jsonResponse(ctx.failure()?.message ?: "Unknown Error",
                                 -1,
                                 if (ctx.statusCode() > 0) ctx.statusCode() else 500
                )
            }

    return this.requestHandler {
        router.accept(it)
    }
}
