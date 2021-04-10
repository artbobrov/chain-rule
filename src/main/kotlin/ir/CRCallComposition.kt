package ir

import ir.visitor.CRIRElementVisitor

class CRCallComposition(children: List<CRIRElement>) : CRIRExpressionBase(children), CRIRCallElement {
    constructor(left: CRIRCallElement, right: CRIRCallElement) : this(listOf(left, CRLeaf("%>%"), right))
    var leftCall: CRIRCallElement
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    var rightCall: CRIRCallElement
        get() = getChild(1) ?: throw IllegalStateException()
        set(value) {
            replaceChild(value, 1)
        }

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitCallComposition(this)
    }

    override fun copy() = CRCallComposition(children.copy())
}
