package transformations.constantFolding

import ir.*
import ir.visitor.CRIRElementVisitor
import transformations.CRTransformation

object ConstantFolding : CRTransformation {
    override fun transform(ir: CRIRElement): CRIRElement = ir.accept(ConstantFoldingVisitor())
}

private class ConstantFoldingVisitor : CRIRElementVisitor<CRIRElement> {
    override fun visitElement(ir: CRElement): CRIRElement = ir

    override fun visitUnaryExpression(ir: CRUnaryExpression): CRIRElement {
        ir.expression = visitIRExpression(ir.expression) as CRIRExpression
        when (ir.operator.operatorType) {
            OperatorType.Plus -> {
                return ir.expression.skipParenthesisExpression
            }
            OperatorType.Minus -> {
                val innerExpression = ir.expression.skipParenthesisExpression
                if (innerExpression is CRNumber) {
                    innerExpression.number = -innerExpression.number
                    ir.expression = innerExpression
                    return innerExpression
                } else {
                    // cannot remove inner expression's parenthesis
                    val parenthesizedExpression = innerExpression.skipParenthesisExpression.parent as CRIRExpression?
                    ir.expression = parenthesizedExpression ?: throw IllegalStateException()
                }
                return ir
            }
            else -> throw IllegalStateException("Unexpected operator for unary expression")
        }
    }

    override fun visitParenthesizedExpression(ir: CRParenthesizedExpression): CRIRElement {
        ir.expression = visitIRExpression(ir.expression) as CRIRExpression
        return ir
    }

    override fun visitBinaryExpression(ir: CRBinaryExpression): CRIRElement {
        ir.leftExpression = visitIRExpression(ir.leftExpression) as CRIRExpression
        ir.rightExpression = visitIRExpression(ir.rightExpression) as CRIRExpression
        val sir = trySwapExpressions(ir)
        val innerLeftExpression = sir.leftExpression.skipParenthesisExpression
        val innerRightExpression = sir.rightExpression.skipParenthesisExpression
        when (val operatorType = sir.operator.operatorType) {
            is OperatorType.Plus -> {
                when {
                    innerLeftExpression is CRNumber && innerRightExpression is CRNumber -> {
                        val newNumber = operatorType.action(innerLeftExpression.number, innerRightExpression.number)
                        return CRNumber(newNumber)
                    }
                    innerLeftExpression is CRElement && innerRightExpression is CRElement -> {
                        return CRBinaryExpression(
                            listOf(CRNumber(2), CROperatorType(OperatorType.Times), innerLeftExpression)
                        )
                    }
                }
            }
            is OperatorType.Minus -> {
                when {
                    innerLeftExpression is CRNumber && innerRightExpression is CRNumber -> {
                        val number = operatorType.action(innerLeftExpression.number, innerRightExpression.number)
                        return CRNumber(number)
                    }
                }
            }
            is OperatorType.Times -> {
                when {
                    innerLeftExpression is CRNumber && innerRightExpression is CRNumber -> {
                        val number = operatorType.action(innerLeftExpression.number, innerRightExpression.number)
                        return CRNumber(number)
                    }
                }
            }
            is OperatorType.Gt -> {
                when {
                    innerLeftExpression is CRNumber && innerRightExpression is CRNumber -> {
                        val flag = operatorType.action(innerLeftExpression.number, innerRightExpression.number)
                        return CRBoolean(flag)
                    }
                }
            }
            is OperatorType.Lt -> {
                when {
                    innerLeftExpression is CRNumber && innerRightExpression is CRNumber -> {
                        val flag = operatorType.action(innerLeftExpression.number, innerRightExpression.number)
                        return CRBoolean(flag)
                    }
                }
            }
            is OperatorType.Eq -> {
                when {
                    innerLeftExpression is CRNumber && innerRightExpression is CRNumber -> {
                        val flag = operatorType.action(innerLeftExpression.number, innerRightExpression.number)
                        return CRBoolean(flag)
                    }
                    innerLeftExpression is CRBoolean && innerRightExpression is CRBoolean -> {
                        val flag = operatorType.action(innerLeftExpression.value, innerRightExpression.value)
                        return CRBoolean(flag)
                    }
                }
            }
            is OperatorType.And -> {
                when {
                    innerLeftExpression is CRBoolean && innerRightExpression is CRBoolean -> {
                        val flag = operatorType.action(innerLeftExpression.value, innerRightExpression.value)
                        return CRBoolean(flag)
                    }
                    innerLeftExpression is CRBoolean && !innerLeftExpression.value
                        || innerRightExpression is CRBoolean && !innerRightExpression.value -> {
                        return CRBoolean(false)
                    }
                    innerLeftExpression is CRBoolean && innerLeftExpression.value -> return innerRightExpression
                    innerRightExpression is CRBoolean && innerRightExpression.value -> return innerLeftExpression
                }
            }
            is OperatorType.Or -> {
                when {
                    innerLeftExpression is CRBoolean && innerRightExpression is CRBoolean -> {
                        val newNumber = operatorType.action(innerLeftExpression.value, innerRightExpression.value)
                        return CRBoolean(newNumber)
                    }
                    innerLeftExpression is CRBoolean && innerLeftExpression.value
                        || innerRightExpression is CRBoolean && innerRightExpression.value -> {
                        return CRBoolean(true)
                    }
                    innerLeftExpression is CRBoolean && !innerLeftExpression.value -> return innerRightExpression
                    innerRightExpression is CRBoolean && !innerRightExpression.value -> return innerLeftExpression
                }
            }
        }
        if (innerLeftExpression is CRElement) {
            sir.leftExpression = sir.rightExpression
            sir.rightExpression = innerLeftExpression
        }
        return sir
    }

    override fun visitCallComposition(ir: CRCallComposition): CRIRElement {
        ir.leftCall = visitIRCallElement(ir.leftCall) as CRIRCallElement
        ir.rightCall = visitIRCallElement(ir.rightCall) as CRIRCallElement
        return ir
    }

    override fun visitFilterCall(ir: CRFilterCall): CRIRElement {
        ir.expression = visitIRExpression(ir.expression) as CRIRExpression
        return ir
    }

    override fun visitMapCall(ir: CRMapCall): CRIRElement {
        ir.expression = visitIRExpression(ir.expression) as CRIRExpression
        return ir
    }

    override fun visitNumber(ir: CRNumber): CRIRElement = ir

    override fun visitBoolean(ir: CRBoolean): CRIRElement = ir

    override fun visitOperator(ir: CROperatorType): CRIRElement {
        error("Not expected to be called")
    }

    override fun visitTerminal(ir: CRLeaf): CRIRElement {
        error("Not expected to be called")
    }

    private fun trySwapExpressions(ir: CRBinaryExpression): CRBinaryExpression {
        val leftExpression = ir.leftExpression.skipParenthesisExpression
        val rightExpression = ir.rightExpression.skipParenthesisExpression
        if (rightExpression is CRBinaryExpression && leftExpression is CRNumber) {
            // swap right and left expression
            ir.rightExpression = leftExpression
            ir.leftExpression = rightExpression
            return trySwapExpressions(ir)
        }
        if (leftExpression !is CRBinaryExpression || rightExpression !is CRNumber) {
            // cannot swap to fold.
            return ir
        }
        if (leftExpression.operator.operatorType != ir.operator.operatorType) {
            // operators don't match
            return ir
        }
        val leftRightExpression = leftExpression.rightExpression.skipParenthesisExpression
        if (leftRightExpression !is CRElement) {
            // some complex expression. cannot fold
            return ir
        }
        leftExpression.rightExpression = ir.rightExpression.skipParenthesisExpression
        ir.rightExpression = leftRightExpression
        ir.leftExpression = visitIRExpression(ir.leftExpression) as CRIRExpression
        return ir
    }

    private val CRIRExpression.skipParenthesisExpression: CRIRExpression
        get() = when (this) {
            is CRParenthesizedExpression -> expression.skipParenthesisExpression
            else -> this
        }
}
