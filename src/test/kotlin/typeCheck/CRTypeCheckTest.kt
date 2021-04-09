package typeCheck

import ir.CRIRElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import parser.CRProgramParser
import parser.CRRuleContextToIRVisitor

internal class CRTypeCheckTest {
    @Test
    fun `test generic`() {
        val ir = "map{(element)}%>%map{element}".toIR()
        val context = ir.accept(CRTypeCheckVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Any)),
            evalContext.getType(ir),
            "ID functions. Sequence of map should be compete generic."
        )
    }

    @Test
    fun `test filter`() {
        val ir = "filter {element}%>%map{element&element=(1 > 0)}".toIR()
        val context = ir.accept(CRTypeCheckVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Boolean), CRType.Array(CRType.Boolean)),
            evalContext.getType(ir),
            "ID functions. Sequence of map should be compete generic."
        )
    }

    @Test
    fun `test filter constrained`() {
        val ir = "filter {(element)>0 & element < 1 + (-(element))}".toIR()
        val context = ir.accept(CRTypeCheckVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
            "ID functions. Sequence of map should be compete generic."
        )
    }

    @Test
    fun `test first chain constrained`() {
        val ir = "map{(element+1)}%>%map{(element)}%>%filter{(element>0)}".toIR()
        val context = ir.accept(CRTypeCheckVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
            "ID functions. Sequence of map should be compete generic."
        )
    }

    @Test
    fun `test last chain constrained`() {
        val ir = "map{((element))}%>%map{(element)}%>%filter{(element>0)}".toIR()
        val context = ir.accept(CRTypeCheckVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
            "ID functions. Sequence of map should be compete generic."
        )
    }
}

private fun String.toIR(): CRIRElement {
    return CRProgramParser.parse(this).accept(CRRuleContextToIRVisitor())
}