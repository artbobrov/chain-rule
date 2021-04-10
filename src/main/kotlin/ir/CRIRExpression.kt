package ir

interface CRIRExpression : CRIRElement {
    override fun copy(): CRIRExpression

    fun wrapWithParenthesis(): CRParenthesizedExpression
}

abstract class CRIRExpressionBase(children: List<CRIRElement>) : CRIRElementBase(children), CRIRExpression {
    override fun wrapWithParenthesis(): CRParenthesizedExpression {
        return CRParenthesizedExpression(
            listOf(CRLeaf("("), copy(), CRLeaf(")"))
        )
    }

    abstract override fun copy(): CRIRExpressionBase
}
