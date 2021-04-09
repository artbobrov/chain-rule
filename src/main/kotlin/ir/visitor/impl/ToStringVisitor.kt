package ir.visitor.impl

import ir.CRLeaf
import ir.visitor.CRIRElementBaseVisitor

class ToStringVisitor(private val buffer: StringBuffer = StringBuffer()) : CRIRElementBaseVisitor() {
    override fun visitTerminal(ir: CRLeaf) {
        buffer.append(ir.text)
    }
}