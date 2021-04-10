package ir

import ir.visitor.CRIRElementVisitor

class CRNumber(children: List<CRIRElement>) : CRIRExpressionBase(children), CRIRExpression {
    constructor(number: Int) : this(listOf(CRLeaf(number.toString())))

    var number: Int
        get() = getChild<CRLeaf>(0)?.text?.toInt() ?: throw IllegalStateException()
        set(value) = replaceChild(CRLeaf(value.toString()), 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitNumber(this)
    }

    override fun copy() = CRNumber(children.copy())
}
