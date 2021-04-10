package ir

import ir.visitor.CRIRElementVisitor

class CRMapCall(children: List<CRIRElement>) : CRIRExpressionBase(children), CRIRBasicCallElement {
    constructor(expression: CRIRExpression) : this(
        listOf(CRLeaf("map"), CRLeaf("{"), expression, CRLeaf("}"))
    )

    override var expression: CRIRExpression
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitMapCall(this)
    }

    override fun copy() = CRMapCall(children.copy())
}
