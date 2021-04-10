package ir

import ir.visitor.CRIRElementVisitor

class CRFilterCall(children: List<CRIRElement>) : CRIRExpressionBase(children), CRIRBasicCallElement {
    override var expression: CRIRExpression
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitFilterCall(this)
    }

    override fun copy() = CRFilterCall(children.copy())
}
