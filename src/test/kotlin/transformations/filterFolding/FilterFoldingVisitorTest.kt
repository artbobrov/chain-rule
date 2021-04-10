package transformations.filterFolding

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.BaseIRTest

internal class FilterFoldingVisitorTest : BaseIRTest() {
    @Test
    fun `test basic folding 2 chain`() {
        val ir = FilterFolding.transform("filter{element=1}%>%filter{element>0}".toIR())
        assertEquals("filter{(element=1)&(element>0)}", ir.text)
    }

    @Test
    fun `test basic folding 3 chain`() {
        val ir =
            FilterFolding.transform("filter{element}%>%filter{(element)=(1=1)}%>%filter{element|(1>((0+1)*2))}".toIR())
        assertEquals("filter{((element)&((element)=(1=1)))&(element|(1>((0+1)*2)))}", ir.text)
    }

    @Test
    fun `test folding chain with map in the middle`() {
        val ir = FilterFolding.transform(
            "filter{element}%>%filter{element}%>%map{element}%>%filter{element}%>%filter{element}".toIR()
        )
        assertEquals("filter{(element)&(element)}%>%map{element}%>%filter{(element)&(element)}", ir.text)
    }

    @Test
    fun `test folding chain in the middle surrounded by maps`() {
        val ir = FilterFolding.transform(
            "map{element+1}%>%filter{element}%>%filter{element}%>%filter{element}%>%map{element+1}".toIR()
        )
        assertEquals("map{element+1}%>%filter{((element)&(element))&(element)}%>%map{element+1}", ir.text)
    }
}
