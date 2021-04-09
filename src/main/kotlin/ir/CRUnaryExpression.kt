package ir

import ir.visitor.CRIRElementVisitor

class CRUnaryExpression(children: List<CRIRElement>) : CRIRTypedElementBase(children), CRIRExpression {
    var operator: CROperatorType
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    var expression: CRIRExpression
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitUnaryExpression(this)
    }
}