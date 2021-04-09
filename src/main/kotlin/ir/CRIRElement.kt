package ir

import ir.visitor.CRIRElementVisitor
import ir.visitor.impl.ToStringVisitor

interface CRIRElement {
    fun <T> accept(visitor: CRIRElementVisitor<T>): T

    val children: List<CRIRElement>

    val text: String
}

abstract class CRIRElementBase(children: List<CRIRElement>) : CRIRElement {
    override val children: MutableList<CRIRElement> = children.toMutableList()

    protected inline fun <reified T : CRIRElement> getChild(offset: Int): T? {
        val childIndex = indexOfChild<T>(offset)
        return childIndex?.let { children[it] as T }
    }

    protected inline fun <reified T : CRIRElement> replaceChild(newChild: T, offset: Int) {
        val childIndex = indexOfChild<T>(offset) ?: childNotFound(offset)

        children[childIndex] = newChild
    }

    protected inline fun <reified T : CRIRElement> deleteChild(offset: Int) {
        val childIndex = indexOfChild<T>(offset) ?: childNotFound(offset)
        children.removeAt(childIndex)
    }

    protected inline fun <reified T : CRIRElement> indexOfChild(offset: Int): Int? {
        var index = offset
        return children.indexOfFirst { it is T && index-- == 0 }.takeIf { it != -1 }
    }

    override val text: String
        get() {
            val buffer = StringBuffer()
            val visitor = ToStringVisitor(buffer)
            accept(visitor)
            return buffer.toString()
        }
}


fun childNotFound(offset: Int): Nothing {
    throw IllegalArgumentException("Child with index $offset is not found")
}