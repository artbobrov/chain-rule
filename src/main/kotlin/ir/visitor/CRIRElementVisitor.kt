package ir.visitor

import ir.*

interface CRIRElementVisitor<T> {
    fun visitIRElement(ir: CRIRElement): T = ir.accept(this)

    fun visitIRCallElement(ir: CRIRCallElement): T = visitIRElement(ir)

    fun visitIRExpression(ir: CRIRExpression): T = visitIRElement(ir)

    fun visitParenthesizedExpression(ir: CRParenthesizedExpression) = visitIRExpression(ir)

    fun visitElement(ir: CRElement) = visitIRExpression(ir)

    fun visitUnaryExpression(ir: CRUnaryExpression) = visitIRExpression(ir)

    fun visitBinaryExpression(ir: CRBinaryExpression) = visitIRExpression(ir)

    fun visitCallComposition(ir: CRCallComposition) = visitIRElement(ir)

    fun visitFilterCall(ir: CRFilterCall) = visitIRCallElement(ir)

    fun visitMapCall(ir: CRMapCall) = visitIRCallElement(ir)

    fun visitOperator(ir: CROperatorType) = visitIRElement(ir)

    fun visitNumber(ir: CRNumber) = visitIRExpression(ir)

    fun visitBoolean(ir: CRBoolean) = visitIRExpression(ir)

    fun visitTerminal(ir: CRLeaf) = visitIRElement(ir)
}

abstract class CRIRElementBaseRecursiveVisitor : CRIRElementVisitor<Unit> {
    override fun visitIRElement(ir: CRIRElement) {
        for (child in ir.children) {
            child.accept(this)
        }
    }
}
