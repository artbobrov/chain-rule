package parser

import antlr.ChainRuleParser
import antlr.ChainRuleParserBaseVisitor
import ir.*
import ir.OperatorType.Companion.isOperator
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode

class CRRuleContextToIRVisitor : ChainRuleParserBaseVisitor<CRIRElement>() {
    override fun visitTerminal(node: TerminalNode): CRIRElement {
        val leaf = CRLeaf(node.text)
        return if (isOperator(node.text)) {
            CROperatorType(listOf(leaf))
        } else {
            leaf
        }
    }

    override fun visitProgram(ctx: ChainRuleParser.ProgramContext): CRIRElement {
        return visitCallChain(ctx.callChain())
    }

    override fun visitCallChain(ctx: ChainRuleParser.CallChainContext): CRIRElement {
        if (ctx.COMPOSITION() != null) {
            return CRCallComposition(ctx.toCRIRElements())
        }
        return super.visitCallChain(ctx)
    }

    override fun visitMapCall(ctx: ChainRuleParser.MapCallContext): CRIRElement {
        return CRMapCall(ctx.toCRIRElements())
    }

    override fun visitFilterCall(ctx: ChainRuleParser.FilterCallContext): CRIRElement {
        return CRFilterCall(ctx.toCRIRElements())
    }

    override fun visitExpression(ctx: ChainRuleParser.ExpressionContext): CRIRElement {
        if (ctx.OR() != null) {
            return CRBinaryExpression(ctx.toCRIRElements())
        }
        return super.visitExpression(ctx)
    }

    override fun visitBinaryBitwiseAndExpression(ctx: ChainRuleParser.BinaryBitwiseAndExpressionContext): CRIRElement {
        if (ctx.AND() != null) {
            return CRBinaryExpression(ctx.toCRIRElements())
        }
        return super.visitBinaryBitwiseAndExpression(ctx)
    }

    override fun visitBinaryEqualityExpression(ctx: ChainRuleParser.BinaryEqualityExpressionContext): CRIRElement {
        if (ctx.EQ() != null) {
            return CRBinaryExpression(ctx.toCRIRElements())
        }
        return super.visitBinaryEqualityExpression(ctx)
    }

    override fun visitBinaryRelationalExpression(ctx: ChainRuleParser.BinaryRelationalExpressionContext): CRIRElement {
        if (ctx.GT() != null || ctx.LT() != null) {
            return CRBinaryExpression(ctx.toCRIRElements())
        }
        return super.visitBinaryRelationalExpression(ctx)
    }

    override fun visitBinaryPlusMinusExpression(ctx: ChainRuleParser.BinaryPlusMinusExpressionContext): CRIRElement {
        if (ctx.PLUS() != null || ctx.MINUS() != null) {
            return CRBinaryExpression(ctx.toCRIRElements())
        }
        return super.visitBinaryPlusMinusExpression(ctx)
    }

    override fun visitBinaryTimesExpression(ctx: ChainRuleParser.BinaryTimesExpressionContext): CRIRElement {
        if (ctx.TIMES() != null) {
            return CRBinaryExpression(ctx.toCRIRElements())
        }
        return super.visitBinaryTimesExpression(ctx)
    }

    override fun visitSignedAtom(ctx: ChainRuleParser.SignedAtomContext): CRIRElement {
        if (ctx.PLUS() != null || ctx.MINUS() != null) {
            return CRUnaryExpression(ctx.toCRIRElements())
        }
        return super.visitSignedAtom(ctx)
    }

    override fun visitParenthesizedExpression(ctx: ChainRuleParser.ParenthesizedExpressionContext): CRIRElement {
        return CRParenthesizedExpression(ctx.toCRIRElements())
    }

    override fun visitConstant(ctx: ChainRuleParser.ConstantContext): CRIRElement {
        return CRNumber(ctx.toCRIRElements())
    }

    override fun visitElement(ctx: ChainRuleParser.ElementContext): CRIRElement {
        return CRElement(ctx.toCRIRElements())
    }

    private fun ParserRuleContext.toCRIRElements(): List<CRIRElement> {
        return children.map { it.accept(this@CRRuleContextToIRVisitor) }
    }
}
