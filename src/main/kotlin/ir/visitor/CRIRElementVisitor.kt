package ir.visitor

import ir.*

interface CRIRElementVisitor<T> {
    fun visitIRElement(ir: CRIRElement): T = ir.accept(this)

    fun visitIRCallElement(ir: CRIRCallElement): T = visitIRElement(ir)

    fun visitIRExpression(ir: CRIRExpression): T = visitIRElement(ir)

    fun visitElement(ir: CRElement): T

    fun visitUnaryExpression(ir: CRUnaryExpression): T

    fun visitParenthesizedExpression(ir: CRParenthesizedExpression): T

    fun visitBinaryExpression(ir: CRBinaryExpression): T

    fun visitCallComposition(ir: CRCallComposition): T

    fun visitFilterCall(ir: CRFilterCall): T

    fun visitMapCall(ir: CRMapCall): T

    fun visitOperator(ir: CROperatorType): T

    fun visitNumber(ir: CRNumber): T

    fun visitTerminal(ir: CRLeaf): T
}


abstract class CRIRElementBaseVisitor : CRIRElementVisitor<Unit> {
    override fun visitIRElement(ir: CRIRElement) {
        for (child in ir.children) {
            child.accept(this)
        }
    }

    override fun visitIRCallElement(ir: CRIRCallElement) = visitIRElement(ir)

    override fun visitIRExpression(ir: CRIRExpression) = visitIRElement(ir)

    override fun visitParenthesizedExpression(ir: CRParenthesizedExpression) = visitIRExpression(ir)

    override fun visitElement(ir: CRElement) = visitIRExpression(ir)

    override fun visitUnaryExpression(ir: CRUnaryExpression) = visitIRExpression(ir)

    override fun visitBinaryExpression(ir: CRBinaryExpression) = visitIRExpression(ir)

    override fun visitCallComposition(ir: CRCallComposition) = visitIRElement(ir)

    override fun visitFilterCall(ir: CRFilterCall) = visitIRCallElement(ir)

    override fun visitMapCall(ir: CRMapCall) = visitIRCallElement(ir)

    override fun visitOperator(ir: CROperatorType) = visitIRElement(ir)

    override fun visitNumber(ir: CRNumber) = visitIRExpression(ir)

    override fun visitTerminal(ir: CRLeaf) = visitIRElement(ir)
}