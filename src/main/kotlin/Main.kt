import antlr.ChainRuleParser
import ir.CRIRElement
import parser.CRProgramParser
import parser.CRRuleContextToIRVisitor
import typeCheck.CRTypeCheckVisitor

private fun ChainRuleParser.ProgramContext.toIR(): CRIRElement {
    val visitor = CRRuleContextToIRVisitor()
    return accept(visitor)
}

fun main() {
    val parser = CRProgramParser
    val code = "map{(element)}%>%map{(element+1)}%>%filter{(element)}"
    val ast = parser.parse(code)
    val ir = ast.toIR()
    require(ir.text == code)
    val context = ir.accept(CRTypeCheckVisitor())
    println(context.resolveTypeFor(ir))
}