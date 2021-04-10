package transformations.mapPropagation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.BaseIRTest

internal class MapPropagationTest : BaseIRTest() {
    @Test
    fun `test basic propagate 2 chain map`() {
        val ir = MapPropagation.transform("map{element+1}%>%map{element*2}".toIR())
        assertEquals("map{(element+1)*2}", ir.text)
    }

    @Test
    fun `test basic propagate 2 chain with filter`() {
        val ir = MapPropagation.transform("map{element+1}%>%filter{(element*2)=0}".toIR())
        assertEquals("filter{((element+1)*2)=0}%>%map{element+1}", ir.text)
    }

    @Test
    fun `test basic propagate 3 chain with double map`() {
        val ir = MapPropagation.transform("map{element+1}%>%map{(element*2)=0}%>%filter{element}".toIR())
        assertEquals("filter{(((element+1)*2)=0)}%>%map{((element+1)*2)=0}", ir.text)
    }

    @Test
    fun `test basic propagate 3 chain with double filter`() {
        val ir = MapPropagation.transform("map{element+1}%>%filter{(element*2)=0}%>%filter{element<0}".toIR())
        assertEquals("filter{((element+1)*2)=0}%>%filter{(element+1)<0}%>%map{element+1}", ir.text)
    }

    @Test
    fun `test basic propagate 4 chain with double filter`() {
        val ir = MapPropagation.transform(
            "map{element+1}%>%filter{(element*2)=0}%>%filter{element<0}%>%filter{element<0}".toIR()
        )
        assertEquals(
            "filter{((element+1)*2)=0}%>%filter{(element+1)<0}%>%filter{(element+1)<0}%>%map{element+1}",
            ir.text
        )
    }

    @Test
    fun `test chain starts with filters`() {
        val ir = MapPropagation.transform(
            (
                "filter{element<2}%>%map{element*8}%>%filter{element<1}" +
                    "%>%map{element*4}%>%map{(element+1)>10}%>%filter{element=0}"
                ).toIR()
        )
        assertEquals(
            "filter{element<2}%>%filter{(element*8)<1}%>%" +
                    "filter{((((element*8)*4)+1)>10)=0}%>%map{(((element*8)*4)+1)>10}",
            ir.text
        )
    }

    @Test
    fun `test chain map end with filters`() {
        val ir = MapPropagation.transform(
            "map{element*8}%>%filter{element<1}%>%map{element+1}".toIR()
        )
        assertEquals(
            "filter{(element*8)<1}%>%map{(element*8)+1}",
            ir.text
        )
    }
}
