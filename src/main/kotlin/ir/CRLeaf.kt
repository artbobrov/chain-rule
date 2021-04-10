package ir

import ir.visitor.CRIRElementVisitor

data class CRLeaf(override val text: String) : CRIRElementBase(emptyList()) {
    override fun <T> accept(visitor: CRIRElementVisitor<T>): T {
        return visitor.visitTerminal(this)
    }

    override fun copy(): CRIRElement = CRLeaf(text)
}
