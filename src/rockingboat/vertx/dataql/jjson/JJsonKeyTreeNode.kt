package rockingboat.vertx.dataql.jjson

import rockingboat.vertx.dataql.jjson.extension.toListOfJsonPathKey
import kotlin.reflect.KClass


fun MutableList<JJsonKeyTreeNode>.findByKey(key: String) = this.firstOrNull { it.key == key }


class JJsonKeyTreeNode() {
    var key = "ROOT"
    private val variants = mutableListOf<JJsonKeyTreeNodeVariant>()
    private val children = mutableListOf<JJsonKeyTreeNode>()

    @Suppress("unused")
    val fields: JJson.Object by lazy {
        val result = JJson.Object()
        walker(result, this, JJsonKeyTreeNodeVariant.Field::class)
        result
    }

    @Suppress("unused")
    val extracted: JJson.Object by lazy {
        val result = JJson.Object()
        walker(result, this, JJsonKeyTreeNodeVariant.Extract::class)
        result
    }

    private var parent: JJsonKeyTreeNode? = null

    constructor(key: String) : this() {
        this.key = key
    }


    fun addChild(keyPath: String, JJsonKeyTreeNodeVariant: JJsonKeyTreeNodeVariant) {
        var currentNode = this
        val jsonPath = keyPath.toListOfJsonPathKey()
        jsonPath.forEach { path ->
            currentNode = when (path) {
                is JJsonPathKey.ObjectKey  -> addChild(currentNode, path.key)
                is JJsonPathKey.ObjectKeys -> path.set.toList().map { addChild(currentNode, it) }.first()
                is JJsonPathKey.FullArray  -> currentNode
                else                       -> throw IllegalArgumentException("kkk ${keyPath} ${path}")
            }
        }

        if (!currentNode.variants.contains(JJsonKeyTreeNodeVariant))
            currentNode.variants.add(JJsonKeyTreeNodeVariant)
    }


    override fun toString(): String {
        return toString(0)
    }


    fun process(obj: JJson) = process(obj, this)

    private fun process(obj: JJson, nodeJJson: JJsonKeyTreeNode) {
        if (obj is JJson.Array) {
            obj.value.forEach { process(it, nodeJJson) }
        } else {
            (obj as? JJson.Object)?.leaveKeys(nodeJJson.children.map { it.key })

            nodeJJson.children.forEach {
                val result = obj.getValueByKey(it.key)
                when (result) {
                    is JJson.Array, is JJson.Object -> process(result, it)
                }

                it.variants.forEach {
                    when (it) {
                        is JJsonKeyTreeNodeVariant.Field   -> it.result.addValue(result)
                        is JJsonKeyTreeNodeVariant.Extract -> it.result.addValue(result)
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

    private fun addChild(JJsonKeyTreeNode: JJsonKeyTreeNode) {
        children.add(JJsonKeyTreeNode)
        JJsonKeyTreeNode.parent = this
    }


    private fun addChild(currentJJsonKeyTreeNode: JJsonKeyTreeNode, key: String): JJsonKeyTreeNode {
        var foundNode = currentJJsonKeyTreeNode.children.findByKey(key)
        if (foundNode == null) {
            val newNode = JJsonKeyTreeNode(key)
            newNode.parent = currentJJsonKeyTreeNode
            currentJJsonKeyTreeNode.addChild(newNode)
            foundNode = newNode
        }

        return foundNode
    }


    private fun walker(obj: JJson.Object, nodeJJson: JJsonKeyTreeNode, type: KClass<*>) {
        nodeJJson.variants.forEach {
            when {
                it is JJsonKeyTreeNodeVariant.Extract && type == JJsonKeyTreeNodeVariant.Extract::class -> obj[it.key] = it.result.getValue(it.key)
                it is JJsonKeyTreeNodeVariant.Field && type == JJsonKeyTreeNodeVariant.Field::class     -> obj[it.key] = it.result.getValue(it.key)
            }
        }

        nodeJJson.children.forEach {
            walker(obj, it, type)
        }

    }
}


