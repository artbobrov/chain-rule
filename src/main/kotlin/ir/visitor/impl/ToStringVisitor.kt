package ir.visitor.impl

import ir.CRLeaf
import ir.visitor.CRIRElementBaseRecursiveVisitor

class ToStringVisitor(private val buffer: StringBuffer = StringBuffer()) : CRIRElementBaseRecursiveVisitor() {
    override fun visitTerminal(ir: CRLeaf) {
        buffer.append(ir.text)
    }
}
