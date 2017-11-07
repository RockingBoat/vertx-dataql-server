package rockingboat.vertx.dataql.jjson.extension

import rockingboat.vertx.dataql.jjson.JJsonPathKey

private val regexArrayIndex = Regex("^\\[(\\d+)\\]$")
private val regexFullArray = Regex("^\\*$")
private val regexSplitByDot = Regex("[/\\.]")
private val regexSet = Regex("^<(.+?)>$")
private val regexSplitByComma = Regex(",\\s*")


fun String.toListOfJsonPathKey(): List<JJsonPathKey> = this.split(regexSplitByDot).map { keyToJsonPathKey(it) }

private fun keyToJsonPathKey(key: String): JJsonPathKey {
    regexArrayIndex.find(key)
        ?.groups
        ?.elementAtOrNull(1)
        ?.value
        ?.let { return JJsonPathKey.ArrayIndex(it.toInt()) }


    regexFullArray.find(key)
        ?.groups
        ?.elementAtOrNull(0)
        ?.value
        ?.let { return JJsonPathKey.FullArray }

    regexSet.find(key)
        ?.groups
        ?.elementAtOrNull(1)
        ?.value
        ?.let { return JJsonPathKey.ObjectKeys(it.split(regexSplitByComma).toSet()) }

    return JJsonPathKey.ObjectKey(key)
}
