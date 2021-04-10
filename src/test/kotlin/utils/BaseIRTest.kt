package utils

import ir.CRIRElement
import parser.CRProgramParser
import parser.CRRuleContextToIRVisitor

abstract class BaseIRTest {
    protected fun String.toIR(): CRIRElement {
        return CRProgramParser.parse(this).accept(CRRuleContextToIRVisitor())
    }
}
