package transformations.mapPropagation

import ir.*
import ir.visitor.CRIRElementVisitor
import transformations.CRTransformation

object MapPropagation : CRTransformation {
    override fun transform(ir: CRIRElement): CRIRElement {
        val visitor = PropagateMapVisitor()
        val ast = ir.accept(visitor)
        val replacement = visitor.elementReplacement?.copy() ?: return ir
        return when (ast) {
            is CRCallComposition -> CRCallComposition(ast, CRMapCall(replacement))
            is CRIRBasicCallElement -> CRCallComposition(ast, CRMapCall(replacement))
            null -> CRMapCall(replacement)
            else -> ir
        }
    }
}

private class PropagateMapVisitor : CRIRElementVisitor<CRIRElement?> {
    var elementReplacement: CRIRExpression? = null
    override fun visitIRElement(ir: CRIRElement): CRIRElement = ir

    override fun visitMapCall(ir: CRMapCall): CRIRElement? {
        val substitution = elementReplacement?.wrapWithParenthesis()?.let(ir.expression::withElement) ?: ir.expression
        substitution as CRIRExpression
        elementReplacement = substitution
        return null
    }

    override fun visitFilterCall(ir: CRFilterCall): CRIRElement {
        val substitution = elementReplacement
        if (substitution != null) {
            ir.expression = ir.expression.withElement(substitution.wrapWithParenthesis()) as CRIRExpression
        }
        return ir
    }

    override fun visitCallComposition(ir: CRCallComposition): CRIRElement? {
        val leftCall = ir.leftCall.accept(this) as CRIRCallElement?
        val rightCall = ir.rightCall.accept(this) as CRIRCallElement?
        return when {
            leftCall != null && rightCall != null -> {
                ir.leftCall = leftCall
                ir.rightCall = rightCall
                return ir
            }
            else -> leftCall ?: rightCall
        }
    }
}

private fun CRIRElement.withElement(expression: CRIRExpression): CRIRElement = accept(
    ReplaceElement(
        expression
    )
)

private class ReplaceElement(val newExpression: CRIRExpression) : CRIRElementVisitor<CRIRElement> {
    override fun visitElement(ir: CRElement): CRIRElement = newExpression.copy()
    override fun visitParenthesizedExpression(ir: CRParenthesizedExpression): CRIRElement {
        ir.expression = visitIRExpression(ir.expression) as CRIRExpression
        return ir
    }

    override fun visitUnaryExpression(ir: CRUnaryExpression): CRIRElement {
        ir.expression = visitIRExpression(ir.expression) as CRIRExpression
        return ir
    }

    override fun visitBinaryExpression(ir: CRBinaryExpression): CRIRElement {
        ir.leftExpression = visitIRExpression(ir.leftExpression) as CRIRExpression
        ir.rightExpression = visitIRExpression(ir.rightExpression) as CRIRExpression
        return ir
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

    override fun visitOperator(ir: CROperatorType): CRIRElement = ir

    override fun visitNumber(ir: CRNumber): CRIRElement = ir

    override fun visitBoolean(ir: CRBoolean): CRIRElement = ir

    override fun visitTerminal(ir: CRLeaf): CRIRElement = ir
}
