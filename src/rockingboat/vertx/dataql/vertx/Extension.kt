package rockingboat.vertx.dataql.vertx

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DataQLMethod(val method: String,
                              val version: String = "V1")
