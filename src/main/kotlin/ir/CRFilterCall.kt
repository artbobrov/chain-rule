package ir

import ir.visitor.CRIRElementVisitor

class CRFilterCall(children: List<CRIRElement>) : CRIRTypedElementBase(children), CRIRCallElement {
    override val lastExpression: CRIRExpression
        get() = expression
    var expression: CRIRExpression
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitFilterCall(this)
    }
}
