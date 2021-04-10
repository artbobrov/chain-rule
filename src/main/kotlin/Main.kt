import antlr.ChainRuleParser
import com.xenomachina.argparser.ArgParser
import ir.CRIRElement
import parser.CRProgramParser
import parser.CRRuleContextToIRVisitor
import transformations.CRTransformation
import transformations.constantFolding.ConstantFolding
import transformations.filterFolding.FilterFolding
import transformations.mapPropagation.MapPropagation
import typeInference.CRTypeInferenceVisitor

private fun ChainRuleParser.ProgramContext.toIR(): CRIRElement {
    val visitor = CRRuleContextToIRVisitor()
    return accept(visitor)
}

private class Arguments(parser: ArgParser) {
    val noInferType by parser.flagging("-n", "--no-infer", help = "Disable type inference")

    val mapPropagation: Boolean by parser.flagging(
        "-m",
        "--map-propagation",
        help = "Show map propagation transformation"
    )
    val constantFolding: Boolean by parser.flagging(
        "-c",
        "--constant-folding",
        help = "Show constant folding transformation"
    )

    val filterFolding: Boolean by parser.flagging("-f", "--filter-folding", help = "Show filter folding transformation")

    val everyTransformation: Boolean by parser.flagging("-a", "--all", help = "Show all transformations combined")
    val code: String by parser.positional("CODE", help = "Source code")
}

fun main(args: Array<String>) {
    val arguments = ArgParser(args).parseInto(::Arguments)

    val parser = CRProgramParser
    val ast = parser.parse(arguments.code)
    val ir = ast.toIR()
    println("IR text: ${ir.text}")
    if (!arguments.noInferType) {
        val evalContext = ir.accept(CRTypeInferenceVisitor()).evalContext()
        println(
            "${arguments.code} :: ${evalContext.getType(ir)}"
        )
    }
    if (arguments.mapPropagation) {
        println("Map Propagated: ${transform(ir.copy(), MapPropagation).text}")
    }
    if (arguments.constantFolding) {
        println("Constant Folded: ${transform(ir.copy(), ConstantFolding).text}")
    }
    if (arguments.filterFolding) {
        println("Filter Folded: ${transform(ir.copy(), FilterFolding).text}")
    }
    if (arguments.everyTransformation) {
        println("All transformations: ${transform(ir.copy(), MapPropagation, FilterFolding, ConstantFolding).text}")
    }
}

private fun transform(sr: CRIRElement, vararg transformations: CRTransformation): CRIRElement {
    var ir = sr
    for (transformation in transformations) {
        ir = transformation.transform(ir)
    }
    return ir
}
