package typeCheck

import errors.TypeCheckError
import errors.typeResolutionError
import ir.*
import ir.visitor.CRIRElementVisitor

class CRTypeCheckVisitor : CRIRElementVisitor<CRTypeContext> {
    override fun visitUnaryExpression(ir: CRUnaryExpression): CRTypeContext {
        val expression = ir.expression
        val expressionContext = visitIRExpression(expression)
        when (ir.operator.operatorType) {
            CROperatorType.OperatorType.PLUS,
            CROperatorType.OperatorType.MINUS -> {
                if (expressionContext.resolveTypeFor(expression)?.canConvertTo(CRType.Int) == false) {
                    throw TypeCheckError("${expression.text} is expected to have Int type")
                }

                expressionContext.update(expression, CRType.Int)
                expressionContext.constraints[ir] = CRTypeConstraint.Exact(CRType.Int)

                return expressionContext
            }
            else -> throw IllegalStateException("Unexpected operator for unary expression")
        }
    }

    override fun visitParenthesizedExpression(ir: CRParenthesizedExpression): CRTypeContext {
        val expression = ir.expression
        val expressionContext = visitIRExpression(expression)
        expressionContext.constraints[ir] = CRTypeConstraint.DependsOn(expression)
        return expressionContext
    }

    override fun visitBinaryExpression(ir: CRBinaryExpression): CRTypeContext {
        val leftExpression = ir.leftExpression
        val rightExpression = ir.rightExpression
        val leftContext = visitIRExpression(leftExpression)
        val rightContext = visitIRExpression(rightExpression)
        val leftType = leftContext.resolveTypeFor(leftExpression) ?: typeResolutionError(leftExpression)
        val rightType = rightContext.resolveTypeFor(rightExpression) ?: typeResolutionError(rightExpression)

        when (ir.operator.operatorType) {
            CROperatorType.OperatorType.EQ -> {
                if (!leftType.canConvertTo(rightType) && !rightType.canConvertTo(leftType)) {
                    throw TypeCheckError(
                        "${leftExpression.text} and ${rightExpression.text} are expected to have the same type"
                    )
                }
                leftContext.update(leftExpression, CRType.Boolean)
                rightContext.update(rightExpression, CRType.Boolean)
                val context = leftContext + rightContext
                context.constraints[ir] = CRTypeConstraint.Exact(CRType.Boolean)
                return context
            }
            CROperatorType.OperatorType.GT,
            CROperatorType.OperatorType.LT -> {
                if (!leftType.canConvertTo(CRType.Int) && !rightType.canConvertTo(CRType.Int)) {
                    throw TypeCheckError(
                        "${leftExpression.text} and ${rightExpression.text} are expected to have Int type"
                    )
                }
                leftContext.update(leftExpression, CRType.Int)
                rightContext.update(rightExpression, CRType.Int)
                val context = leftContext + rightContext
                context.constraints[ir] = CRTypeConstraint.Exact(CRType.Boolean)
                return context
            }
            CROperatorType.OperatorType.PLUS,
            CROperatorType.OperatorType.MINUS,
            CROperatorType.OperatorType.TIMES -> {
                if (!leftType.canConvertTo(CRType.Int) && !rightType.canConvertTo(CRType.Int)) {
                    throw TypeCheckError(
                        "${leftExpression.text} and ${rightExpression.text} are expected to have Int type"
                    )
                }
                leftContext.update(leftExpression, CRType.Int)
                rightContext.update(rightExpression, CRType.Int)
                val context = leftContext + rightContext
                context.constraints[ir] = CRTypeConstraint.Exact(CRType.Int)
                return context
            }
            CROperatorType.OperatorType.AND,
            CROperatorType.OperatorType.OR -> {
                if (!leftType.canConvertTo(CRType.Boolean) && !rightType.canConvertTo(CRType.Boolean)) {
                    throw TypeCheckError(
                        "${leftExpression.text} and ${rightExpression.text} are expected to have Boolean type"
                    )
                }
                leftContext.update(leftExpression, CRType.Boolean)
                rightContext.update(rightExpression, CRType.Boolean)
                val context = leftContext + rightContext
                context.constraints[ir] = CRTypeConstraint.Exact(CRType.Boolean)
                return context
            }
            CROperatorType.OperatorType.COMPOSITION -> throw IllegalStateException("Unexpected operator for binary expression")

        }
    }

    override fun visitCallComposition(ir: CRCallComposition): CRTypeContext {
        val leftCall = ir.leftCall
        val rightCall = ir.rightCall
        val leftContext = visitIRCallElement(leftCall)

        if (rightCall == null) {
            val leftCallType = leftContext.resolveTypeFor(leftCall) ?: typeResolutionError(leftCall)
            leftContext.constraints[ir] = CRTypeConstraint.Exact(leftCallType)
            return leftContext
        }

        val leftCallType = leftContext.resolveTypeFor(leftCall) ?: typeResolutionError(leftCall)

        val rightContext = visitIRCallElement(rightCall)
        val rightCallType = rightContext.resolveTypeFor(rightCall) ?: typeResolutionError(leftCall)
        val (rightElement, _) = rightContext.resolveElementType()

        if (leftCallType !is CRType.Function1
            || rightCallType !is CRType.Function1
            || !leftCallType.canChainWith(rightCallType)
        ) {
            throw TypeCheckError(
                "Chain rule ${leftCall.text} with type $leftCallType is not compatible with ${rightCall.text} with type $rightCallType"
            )
        }

        if (rightElement == null) {
            leftContext.constraints.putIfAbsent(
                leftCall.lastExpression,
                CRTypeConstraint.Exact(CRType.Any)
            )
        } else {
            leftContext.constraints[leftCall.lastExpression] = CRTypeConstraint.DependsOn(rightElement)
        }
        val context = leftContext + rightContext
        context.constraints[ir] = CRTypeConstraint.DependsOn(listOf(leftCall, rightCall)) { types ->
            val (leftType, rightType) = types
            require(leftType is CRType.Function1 && rightType is CRType.Function1) {
                "Dependents have to be functions"
            }
            if (!leftType.returns.canConvertTo(rightType.argument) && !rightType.argument.canConvertTo(leftType.returns)) {
                throw TypeCheckError("Chains ${leftCall.text} and ${rightCall.text} are not convertible")
            }

            CRType.Function1(leftType.argument, rightType.returns)
        }
        return context
    }

    override fun visitFilterCall(ir: CRFilterCall): CRTypeContext {
        val expression = ir.expression
        val expressionContext = visitIRExpression(expression)
        val expressionType = expressionContext.resolveTypeFor(expression) ?: typeResolutionError(expression)
        val (element, elementType) = expressionContext.resolveElementType()

        if (!expressionType.canConvertTo(CRType.Boolean)) {
            throw TypeCheckError("${expression.text} are expected to have Boolean type")
        }

        expressionContext.updateElementType(elementType)

        if (element == null) {
            expressionContext.constraints[ir] = CRTypeConstraint.Exact(
                CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Any))
            )
        } else {
            expressionContext.constraints[ir] = CRTypeConstraint.DependsOn(element) {
                CRType.Function1(CRType.Array(it), CRType.Array(it))
            }
        }
        return expressionContext
    }

    override fun visitMapCall(ir: CRMapCall): CRTypeContext {
        val expression = ir.expression
        val expressionContext = visitIRExpression(expression)
        val expressionType = expressionContext.resolveTypeFor(expression) ?: typeResolutionError(expression)
        val (element, elementType) = expressionContext.resolveElementType()

        expressionContext.updateElementType(elementType)

        if (element == null) {
            expressionContext.constraints[ir] = CRTypeConstraint.Exact(
                CRType.Function1(CRType.Array(CRType.Any), CRType.Array(expressionType))
            )
        } else {
            expressionContext.constraints[ir] = CRTypeConstraint.DependsOn(element) {
                CRType.Function1(CRType.Array(it), CRType.Array(expressionType))
            }
        }

        return expressionContext
    }

    override fun visitElement(ir: CRElement): CRTypeContext {
        return CRTypeContext(
            CRTypeConstraints(ir to CRTypeConstraint.Exact(CRType.Any))
        )
    }

    override fun visitNumber(ir: CRNumber): CRTypeContext = CRTypeContext(
        CRTypeConstraints(ir to CRTypeConstraint.Exact(CRType.Int))
    )

    override fun visitOperator(ir: CROperatorType): CRTypeContext {
        error("Not expected to be called")
    }

    override fun visitTerminal(ir: CRLeaf): CRTypeContext {
        error("Not expected to be called")
    }
}