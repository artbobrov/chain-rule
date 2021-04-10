package transformations.constantFolding

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.BaseIRTest

internal class ConstantFoldingTest : BaseIRTest() {
    @Test
    fun `test redundant plus removal`() {
        val ir = ConstantFolding.transform("map{(+((element)))}".toIR())
        assertEquals("map{(element)}", ir.text)
    }

    @Test
    fun `test brackets with minus redundant`() {
        val ir = ConstantFolding.transform("map{(-((1)))}".toIR())
        assertEquals("map{(-1)}", ir.text)
    }

    @Test
    fun `test brackets minus binary expr`() {
        val ir = ConstantFolding.transform("map{(-((1+1+3)))}".toIR())
        assertEquals("map{(-5)}", ir.text)
    }

    @Test
    fun `test brackets minus binary expr non foldable`() {
        val ir = ConstantFolding.transform("map{-((((5+element*element))))}".toIR())
        assertEquals("map{-(element*element+5)}", ir.text)
    }

    @Test
    fun `test simple binary expression`() {
        val ir = ConstantFolding.transform("map{(-((1+1+3)))}".toIR())
        assertEquals("map{(-5)}", ir.text)
    }

    @Test
    fun `test simple bool or binary expression`() {
        val ir = ConstantFolding.transform("map{((1=3|3=5))}".toIR())
        assertEquals("map{((1=0))}", ir.text)
    }

    @Test
    fun `test simple bool and brackets binary expression`() {
        val ir = ConstantFolding.transform("map{((1=3&3=5|(1=3|(((3=5))))))}".toIR())
        assertEquals("map{((1=0))}", ir.text)
    }

    @Test
    fun `test simple fold multiple operations with element in the end`() {
        val ir = ConstantFolding.transform("map{1+1+1+1+1+element}".toIR())
        assertEquals("map{5+element}", ir.text)
    }

    @Test
    fun `test simple fold multiple operations with element in the middle`() {
        val ir = ConstantFolding.transform("map{1+1+element+1+1+1}".toIR())
        assertEquals("map{5+element}", ir.text)
    }

    @Test
    fun `test fold multiple operations with element in the beginning with brackets`() {
        val ir = ConstantFolding.transform("map{(element+1)+1+1+1+1}".toIR())
        assertEquals("map{5+element}", ir.text)
    }

    @Test
    fun `test bool redundant disjunction false`() {
        val ir = ConstantFolding.transform("map{element|4>5}".toIR())
        assertEquals("map{element}", ir.text)
    }

    @Test
    fun `test bool redundant disjunction true`() {
        val ir = ConstantFolding.transform("map{element|6>5}".toIR())
        assertEquals("map{0=0}", ir.text)
    }

    @Test
    fun `test bool redundant conjunction false`() {
        val ir = ConstantFolding.transform("map{element&4>5}".toIR())
        assertEquals("map{1=0}", ir.text)
    }

    @Test
    fun `test bool redundant conjunction true`() {
        val ir = ConstantFolding.transform("map{element&6>5}".toIR())
        assertEquals("map{element}", ir.text)
    }
}
