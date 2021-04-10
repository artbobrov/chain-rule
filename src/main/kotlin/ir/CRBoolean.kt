package ir

import ir.visitor.CRIRElementVisitor

class CRBoolean(children: List<CRIRElement>) : CRIRExpressionBase(children), CRIRExpression {
    constructor(flag: Boolean) : this(listOf(CRLeaf(flag.toValue())))

    var value: Boolean
        get() = (getChild<CRLeaf>(0)?.text ?: throw IllegalStateException()) == Keys.TRUE
        set(value) = replaceChild(CRLeaf(value.toValue()), 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitBoolean(this)
    }

    override fun copy() = CRBoolean(children.copy())
}

private fun Boolean.toValue(): String = if (this) Keys.TRUE else Keys.FALSE

private object Keys {
    const val TRUE = "0=0"
    const val FALSE = "1=0"
}
