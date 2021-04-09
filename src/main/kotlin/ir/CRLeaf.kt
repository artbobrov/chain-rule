package ir

import ir.visitor.CRIRElementVisitor

data class CRLeaf(override val text: String) : CRIRElement {
    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitTerminal(this)
    }

    override val children: List<CRIRElement>
        get() = emptyList()
}
