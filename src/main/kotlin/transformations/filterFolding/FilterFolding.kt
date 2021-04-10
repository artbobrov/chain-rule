package transformations.filterFolding

import ir.*
import ir.visitor.CRIRElementVisitor
import transformations.CRTransformation

object FilterFolding : CRTransformation {
    override fun transform(ir: CRIRElement): CRIRElement = ir.accept(FilterFoldingVisitor())
}

private class FilterFoldingVisitor : CRIRElementVisitor<CRIRElement> {
    override fun visitIRElement(ir: CRIRElement): CRIRElement = ir

    override fun visitCallComposition(ir: CRCallComposition): CRIRElement {
        ir.leftCall = ir.leftCall.accept(this) as CRIRCallElement
        ir.rightCall = ir.rightCall.accept(this) as CRIRCallElement
        val leftCall = ir.leftCall
        val rightCall = ir.rightCall
        if (rightCall is CRFilterCall) {
            when (leftCall) {
                is CRCallComposition -> {
                    val leftRightCall = leftCall.rightCall
                    if (leftRightCall is CRFilterCall) {
                        leftRightCall.expression = CRBinaryExpression(
                            listOf(
                                leftRightCall.expression.wrapWithParenthesis(),
                                CROperatorType(OperatorType.And),
                                rightCall.expression.wrapWithParenthesis()
                            )
                        )
                        return leftCall
                    }
                }
                is CRFilterCall -> {
                    rightCall.expression = CRBinaryExpression(
                        listOf(
                            leftCall.expression.wrapWithParenthesis(),
                            CROperatorType(OperatorType.And),
                            rightCall.expression.wrapWithParenthesis()
                        )
                    )
                    return rightCall
                }
            }
        }
        return ir
    }
}
