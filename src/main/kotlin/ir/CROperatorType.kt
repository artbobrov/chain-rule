package ir

import ir.visitor.CRIRElementVisitor

class CROperatorType(children: List<CRIRElement>) : CRIRElementBase(children), CRIRElement {
    constructor(operator: OperatorType) : this(listOf(CRLeaf(operator.text)))

    var operatorType: OperatorType
        get() = getChild<CRLeaf>(0)?.text?.let { OperatorType.fromText(it) } ?: throw IllegalStateException()
        set(value) = replaceChild(CRLeaf(value.text), 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitOperator(this)
    }

    override fun copy() = CROperatorType(children.copy())
}
