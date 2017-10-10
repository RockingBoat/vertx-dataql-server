package rockingboat.vertx.dataql.server.extension

import rockingboat.vertx.dataql.server.JsonPathKey

private val regexArrayIndex = Regex("^\\[(\\d+)\\]$")
private val regexFullArray = Regex("^\\*$")
private val regexSplitByDot = Regex("[/\\.]")
private val regexSet = Regex("^<(.+?)>$")
private val regexSplitByComma = Regex(",\\s*")


fun String.toListOfJsonPathKey(): List<JsonPathKey> = this.split(regexSplitByDot).map { keyToJsonPathKey(it) }

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
