package rockingboat.vertx.dataql.server

sealed class ResponseData {
    data class Object<out T>(val value: T) : ResponseData()
    data class Array<out T>(val value: List<T>) : ResponseData()
}

data class ResponseError(val key: String, val code: Int, val message: String)
data class ResponseDebug(val source: String, val debugInfo: Any)

data class Response(
        val data: ResponseData,
        val errors: List<ResponseError>?,
        val debugInfo: List<ResponseDebug>

)