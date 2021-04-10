package typeInference

import errors.TypeCheckError
import ir.*
import ir.visitor.CRIRElementVisitor

class CRTypeInferenceVisitor : CRIRElementVisitor<CRTypeContext> {
    override fun visitUnaryExpression(ir: CRUnaryExpression): CRTypeContext {
        val expression = ir.expression
        val context = visitIRExpression(expression)
        when (ir.operator.operatorType) {
            OperatorType.Plus,
            OperatorType.Minus -> {
                context += CRTypeConstraint.Exact(expression, CRInferenceType.Concrete(CRType.Int))
                context += CRTypeConstraint.Exact(ir, CRInferenceType.Concrete(CRType.Int))
                return context
            }
            else -> throw IllegalStateException("Unexpected operator for unary expression")
        }
    }

    override fun visitParenthesizedExpression(ir: CRParenthesizedExpression): CRTypeContext {
        val expression = ir.expression
        val context = visitIRExpression(expression)
        context += CRTypeConstraint.DependsOn(ir, expression)
        return context
    }

    override fun visitBinaryExpression(ir: CRBinaryExpression): CRTypeContext {
        val leftExpression = ir.leftExpression
        val rightExpression = ir.rightExpression
        val context = visitIRExpression(leftExpression)
        val rightContext = visitIRExpression(rightExpression)
        context += rightContext
        when (ir.operator.operatorType) {
            OperatorType.Eq -> {
                context += CRTypeConstraint.DependsOn(leftExpression, rightExpression)
                context += CRTypeConstraint.DependsOn(rightExpression, leftExpression)
                context += CRTypeConstraint.Exact(ir, CRInferenceType.Concrete(CRType.Boolean))
            }
            OperatorType.Gt,
            OperatorType.Lt -> {
                context += CRTypeConstraint.Exact(leftExpression, CRInferenceType.Concrete(CRType.Int))
                context += CRTypeConstraint.Exact(rightExpression, CRInferenceType.Concrete(CRType.Int))
                context += CRTypeConstraint.Exact(ir, CRInferenceType.Concrete(CRType.Boolean))
            }
            OperatorType.Plus,
            OperatorType.Minus,
            OperatorType.Times -> {
                context += CRTypeConstraint.Exact(leftExpression, CRInferenceType.Concrete(CRType.Int))
                context += CRTypeConstraint.Exact(rightExpression, CRInferenceType.Concrete(CRType.Int))
                context += CRTypeConstraint.Exact(ir, CRInferenceType.Concrete(CRType.Int))
            }
            OperatorType.And,
            OperatorType.Or -> {
                context += CRTypeConstraint.Exact(leftExpression, CRInferenceType.Concrete(CRType.Boolean))
                context += CRTypeConstraint.Exact(rightExpression, CRInferenceType.Concrete(CRType.Boolean))
                context += CRTypeConstraint.Exact(ir, CRInferenceType.Concrete(CRType.Boolean))
            }
        }
        return context
    }

    override fun visitFilterCall(ir: CRFilterCall): CRTypeContext {
        val expression = ir.expression
        val context = visitIRExpression(expression)
        context += CRTypeConstraint.Exact(expression, CRInferenceType.Concrete(CRType.Boolean))
        val element = context.resolveElementOrCreate()
        context += CRTypeConstraint.DependsOn(ir, listOf(element)) {
            val realElementType = it.first()
            CRInferenceType.Function1(
                CRInferenceType.Array(realElementType),
                CRInferenceType.Array(realElementType)
            )
        }

        return context
    }

    override fun visitMapCall(ir: CRMapCall): CRTypeContext {
        val expression = ir.expression
        val context = visitIRExpression(expression)

        val element = context.resolveElementOrCreate()
        context += CRTypeConstraint.DependsOn(ir, listOf(element, expression)) {
            val (elementInferredType, expressionInferredType) = it
            CRInferenceType.Function1(
                CRInferenceType.Array(elementInferredType),
                CRInferenceType.Array(expressionInferredType)
            )
        }
        return context
    }

    override fun visitCallComposition(ir: CRCallComposition): CRTypeContext {
        val leftCall = ir.leftCall
        val context = visitIRCallElement(leftCall)

        val rightCall = ir.rightCall
        val rightContext = visitIRCallElement(rightCall)
        val rightElement = rightContext.resolveElementOrCreate()

        context += rightContext

        context += CRTypeConstraint.DependsOn(rightElement, leftCall) {
            if (it !is CRInferenceType.Function1) {
                throw TypeCheckError("Unexpected type ${it.type} of chain ${leftCall.text}")
            }
            it.returns.inner
        }
        context += CRTypeConstraint.DependsOn(ir, listOf(leftCall, rightCall)) {
            val (leftCallInferredType, rightCallInferredType) = it

            if (leftCallInferredType !is CRInferenceType.Function1 ||
                rightCallInferredType !is CRInferenceType.Function1
            ) {
                throw TypeCheckError(
                    "Types of composition components ${leftCall.text} and ${rightCall.text} are not functions. " +
                            "Resolved types $leftCallInferredType and $rightCallInferredType"
                )
            }

            if (!leftCallInferredType.canChainWith(rightCallInferredType)) {
                throw TypeCheckError(
                    "Cannot chain functions ${leftCall.text} and ${rightCall.text} with types " +
                            "with types $leftCallInferredType and $rightCallInferredType"
                )
            }

            CRInferenceType.Function1(
                argument = leftCallInferredType.argument,
                returns = rightCallInferredType.returns
            )
        }

        return context
    }

    override fun visitElement(ir: CRElement): CRTypeContext = CRTypeContext(
        CRTypeConstraints(CRTypeConstraint.Exact(ir, CRInferenceType.Variable()))
    )

    override fun visitNumber(ir: CRNumber): CRTypeContext = CRTypeContext(
        CRTypeConstraints(
            CRTypeConstraint.Exact(ir, CRInferenceType.Concrete(CRType.Int))
        )
    )

    override fun visitBoolean(ir: CRBoolean): CRTypeContext = CRTypeContext(
        CRTypeConstraints(
            CRTypeConstraint.Exact(ir, CRInferenceType.Concrete(CRType.Boolean))
        )
    )

    override fun visitOperator(ir: CROperatorType): CRTypeContext {
        error("Not expected to be called")
    }

    override fun visitTerminal(ir: CRLeaf): CRTypeContext {
        error("Not expected to be called")
    }
}
