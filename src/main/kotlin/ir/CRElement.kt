package ir

import ir.visitor.CRIRElementVisitor

class CRElement(children: List<CRIRElement>) : CRIRExpressionBase(children), CRIRExpression {
    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitElement(this)
    }

    override fun copy() = CRElement(children.copy())
}
