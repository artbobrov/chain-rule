package ir

import ir.visitor.CRIRElementVisitor

class CRBinaryExpression(children: List<CRIRElement>) : CRIRExpressionBase(children), CRIRTypedElement {
    var leftExpression: CRIRExpression
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    var operator: CROperatorType
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    var rightExpression: CRIRExpression
        get() = getChild(1) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 1)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitBinaryExpression(this)
    }

    override fun copy() = CRBinaryExpression(children.copy())
}
