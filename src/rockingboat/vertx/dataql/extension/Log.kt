package rockingboat.vertx.dataql.extension

import de.jupf.staticlog.Log

inline fun Log.debugAny(obj: Any?) {
    Log.debug(obj.toString())
}