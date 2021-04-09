package ir

import ir.visitor.CRIRElementVisitor

class CRParenthesizedExpression(children: List<CRIRElement>) : CRIRTypedElementBase(children), CRIRExpression {
    var expression: CRIRExpression
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitParenthesizedExpression(this)
    }
}