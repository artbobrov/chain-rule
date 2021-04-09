package ir

import ir.visitor.CRIRElementVisitor

class CROperatorType(children: List<CRIRElement>) : CRIRTypedElementBase(children), CRIRElement {
    enum class OperatorType(val text: String) {
        PLUS("+"),
        MINUS("-"),
        TIMES("*"),
        GT(">"),
        LT("<"),
        EQ("="),
        AND("&"),
        OR("|"),
        COMPOSITION("%>%");

        companion object {
            fun fromText(text: String): OperatorType? {
                return values().firstOrNull { it.text == text }
            }

            fun isOperator(text: String): Boolean {
                return fromText(text) != null
            }
        }
    }

    var operatorType: OperatorType
        get() = getChild<CRLeaf>(0)?.text?.let { OperatorType.fromText(it) } ?: throw IllegalStateException()
        set(value) = replaceChild(CRLeaf(value.text), 0)

    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitOperator(this)
    }
}