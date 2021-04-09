package ir

import ir.visitor.CRIRElementVisitor

class CRNumber(children: List<CRIRElement>) : CRIRTypedElementBase(children), CRIRExpression {
    var number: Int
        get() = getChild<CRLeaf>(0)?.text?.toInt() ?: throw IllegalStateException()
        set(value) = replaceChild(CRLeaf(value.toString()), 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitNumber(this)
    }
}
