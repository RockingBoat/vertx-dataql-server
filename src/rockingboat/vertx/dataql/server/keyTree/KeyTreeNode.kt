package rockingboat.vertx.dataql.server.keyTree

import rockingboat.vertx.dataql.server.JsonPathKey
import rockingboat.vertx.dataql.server.extension.toListOfJsonPathKey
import rockingboat.vertx.dataql.server.jjson.JJson
import kotlin.reflect.KClass


fun MutableList<KeyTreeNode>.findByKey(key: String) = this.firstOrNull { it.key == key }

private fun walker(obj: JJson.Object, node: KeyTreeNode, type: KClass<*>) {
    node.variants.forEach {
        when {
            it is KeyTreeNodeVariant.Extract && type == KeyTreeNodeVariant.Extract::class -> obj[it.key] = it.result.getValue(it.key)
            it is KeyTreeNodeVariant.Field && type == KeyTreeNodeVariant.Field::class     -> obj[it.key] = it.result.getValue(it.key)
        }
    }

    node.children.forEach {
        walker(obj, it, type)
    }

}


class KeyTreeNode() {
    var key = "ROOT"
    val variants = mutableListOf<KeyTreeNodeVariant>()
    val children = mutableListOf<KeyTreeNode>()

    @Suppress("unused")
    val fields: JJson.Object by lazy {
        val result = JJson.Object()
        walker(result, this, KeyTreeNodeVariant.Field::class)
        result
    }

    @Suppress("unused")
    val extracted: JJson.Object by lazy {
        val result = JJson.Object()
        walker(result, this, KeyTreeNodeVariant.Extract::class)
        result
    }

    private var parent: KeyTreeNode? = null

    constructor(key: String) : this() {
        this.key = key
    }


    fun addChild(keyPath: String, keyTreeNodeVariant: KeyTreeNodeVariant) {
        var currentNode = this
        val jsonPath = keyPath.toListOfJsonPathKey()
        jsonPath.forEach { path ->
            currentNode = when (path) {
                is JsonPathKey.ObjectKey  -> addChild(currentNode, path.key)
                is JsonPathKey.ObjectKeys -> path.set.toList().map { addChild(currentNode, it) }.first()
                is JsonPathKey.FullArray  -> currentNode
                else                      -> throw IllegalArgumentException("kkk ${keyPath} ${path}")
            }
        }

        if (!currentNode.variants.contains(keyTreeNodeVariant))
            currentNode.variants.add(keyTreeNodeVariant)
    }


    override fun toString(): String {
        return toString(0)
    }


    fun process(obj: JJson) = process(obj, this)

    private fun process(obj: JJson, node: KeyTreeNode) {
        if (obj is JJson.Array) {
            obj.value.forEach { process(it, node) }
        } else {
            (obj as? JJson.Object)?.leaveKeys(node.children.map { it.key })

            node.children.forEach {
                val result = obj.getValueByKey(it.key)
                when (result) {
                    is JJson.Array, is JJson.Object -> process(result, it)
                }

                it.variants.forEach {
                    when (it) {
                        is KeyTreeNodeVariant.Field   -> it.result.addValue(result)
                        is KeyTreeNodeVariant.Extract -> it.result.addValue(result)
                    }
                }
            }
        }
    }

    private fun toString(identLevel: Int): String {
        var s = " ".repeat(identLevel * 4) + key
        if (variants.isNotEmpty())
            s += "\t[ ${variants.joinToString(", ") { it.toString() }} ]"


        if (!children.isEmpty()) {
            s += "\n" + children.joinToString("\n") { it.toString(identLevel + 1) }
        }
        return s
    }

    private fun addChild(keyTreeNode: KeyTreeNode) {
        children.add(keyTreeNode)
        keyTreeNode.parent = this
    }


    private fun addChild(currentKeyTreeNode: KeyTreeNode, key: String): KeyTreeNode {
        var foundNode = currentKeyTreeNode.children.findByKey(key)
        if (foundNode == null) {
            val newNode = KeyTreeNode(key)
            newNode.parent = currentKeyTreeNode
            currentKeyTreeNode.addChild(newNode)
            foundNode = newNode
        }

        return foundNode
    }
}


