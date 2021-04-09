package ir

import ir.visitor.CRIRElementVisitor

class CRCallComposition(children: List<CRIRElement>) : CRIRTypedElementBase(children), CRIRCallElement {
    override val lastExpression: CRIRExpression
        get() = rightCall?.lastExpression ?: leftCall.lastExpression

    var leftCall: CRIRCallElement
        get() = getChild(0) ?: throw IllegalStateException()
        set(value) = replaceChild(value, 0)

    var rightCall: CRIRCallElement?
        get() = getChild(1)
        set(value) {
            if (value != null) {
                replaceChild(value, 1)
            } else {
                deleteChild<CRCallComposition>(1)
            }
        }

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitCallComposition(this)
    }
}