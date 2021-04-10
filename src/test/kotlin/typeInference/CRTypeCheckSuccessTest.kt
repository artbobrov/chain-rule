package typeInference

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.BaseIRTest

internal class CRTypeCheckSuccessTest : BaseIRTest() {
    @Test
    fun `test simple filter`() {
        val ir = "filter{(element)}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Boolean), CRType.Array(CRType.Boolean)),
            evalContext.getType(ir),
            "ID functions. Sequence of map should be compete generic."
        )
    }

    @Test
    fun `test generic`() {
        val ir = "map{(element)}%>%map{element}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Any)),
            evalContext.getType(ir),
            "ID functions. Sequence of map should be compete generic."
        )
    }

    @Test
    fun `test two chain without element first`() {
        val ir = "filter{0=0}%>%map{element+1}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test three chain without element first`() {
        val ir = "filter{0=0}%>%filter{1>2}%>%map{element+1}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test multiple chain without element first boolean`() {
        val ir = "map{0=0}%>%map{1=1}%>%map{2=2}%>%filter{element}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Boolean)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test two chain different types`() {
        val ir = "map{(element+1)=element}%>%filter{element}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Boolean)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test two chain without element second`() {
        val ir = "filter{element=element}%>%map{0}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test check map eq lhs rhs`() {
        val ir = "map{element=element}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Boolean)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test check map double eq lhs rhs`() {
        val ir = "map{(element=element)&(element=element)}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Boolean)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test check filter eq lhs rhs`() {
        val ir = "filter{element=element}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Any)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test check filter eq lhs rhs with rhs constraint`() {
        val ir = "filter{element=(element*10*10)}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test check filter eq lhs rhs with lhs constraint`() {
        val ir = "filter{(element+1)=element}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test two chain constraint last`() {
        val ir = "map{(element)}%>%map{element+1}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test filter`() {
        val ir = "filter {element}%>%map{element|element=(1 > 0)}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Boolean), CRType.Array(CRType.Boolean)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test filter constrained`() {
        val ir = "filter {(element)>0 & element < 1 + (-(element))}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test first chain constrained`() {
        val ir = "map{(element+1)}%>%map{(element)}%>%filter{(element>0)}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test last chain constrained`() {
        val ir = "map{((element))}%>%map{(element)}%>%filter{(element>0)}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Int), CRType.Array(CRType.Int)),
            evalContext.getType(ir),
        )
    }

    @Test
    fun `test no elements`() {
        val ir = "filter{2=2}%>%map{1=1}%>%filter{(1>0)}".toIR()
        val context = ir.accept(CRTypeInferenceVisitor())
        val evalContext = context.evalContext()
        assertEquals(
            CRType.Function1(CRType.Array(CRType.Any), CRType.Array(CRType.Boolean)),
            evalContext.getType(ir),
        )
    }
}
