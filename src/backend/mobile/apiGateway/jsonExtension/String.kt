package backend.mobile.apiGateway.jsonExtension


val regexArrayIndex = Regex("^\\[(\\d+)\\]$")
val regexFullArray = Regex("^\\*$")
val regexSplitByDot = Regex("[/\\.]")
val regexSet = Regex("^<(.+?)>$")
val regexSplitByComma = Regex(",\\s*")

fun String.jsonPathKey(): JsonPathKey {
    regexArrayIndex.find(this)
        ?.groups
        ?.elementAtOrNull(1)
        ?.value
        ?.let { return JsonPathKey.ArrayIndex(it.toInt()) }


    regexFullArray.find(this)
        ?.groups
        ?.elementAtOrNull(0)
        ?.value
        ?.let { return JsonPathKey.FullArray }

    regexSet.find(this)
        ?.groups
        ?.elementAtOrNull(1)
        ?.value
        ?.let { return JsonPathKey.ObjectKeys(it.split(regexSplitByComma).toSet()) }

    return JsonPathKey.ObjectKey(this)
}

fun String.toJsonPathKeyList(): List<JsonPathKey> {
    val parts = this.split(regexSplitByDot).filter { it != "" }
    return parts.map { it.jsonPathKey() }
}

//
//fun String.toJsonPathKeyTuples(): List<JsonPathKeyTuple> {
//
//    val parts = this.split(regexSplitByDot).filter { it != "" }
//    return parts.mapIndexed { index, s -> JsonPathKeyTuple(s.jsonPathKey(), parts.getOrNull(index + 1)?.jsonPathKey()) }
//}

