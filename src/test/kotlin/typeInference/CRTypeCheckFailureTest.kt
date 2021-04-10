package typeInference

import errors.TypeCheckError
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.BaseIRTest

internal class CRTypeCheckFailureTest : BaseIRTest() {
    @Test
    fun `test simple filter`() {
        val ir = "filter{(element+1)}".toIR()
        assertThrows<TypeCheckError> {
            ir.accept(CRTypeInferenceVisitor())
        }
    }

    @Test
    fun `test simple map`() {
        val ir = "map{(element+1=(element>0))}".toIR()
        assertThrows<TypeCheckError> {
            ir.accept(CRTypeInferenceVisitor())
        }
    }

    @Test
    fun `test simple chain`() {
        val ir = "map{element+1}%>%filter{element}".toIR()
        assertThrows<TypeCheckError> {
            ir.accept(CRTypeInferenceVisitor())
        }
    }

    @Test
    fun `test simple filter chain`() {
        val ir = "filter{element}%>%map{element+1}".toIR()
        assertThrows<TypeCheckError> {
            ir.accept(CRTypeInferenceVisitor())
        }
    }

    @Test
    fun `test left chain no element`() {
        val ir = "map{1=1}%>%map{element+1}".toIR()
        assertThrows<TypeCheckError> {
            ir.accept(CRTypeInferenceVisitor())
        }
    }

    @Test
    fun `test long chain`() {
        val ir = "map{element+1}%>%filter{1=1}%>%map{(element>0)*2}".toIR()
        assertThrows<TypeCheckError> {
            ir.accept(CRTypeInferenceVisitor())
        }
    }

    @Test
    fun `test long chain last fail`() {
        val ir = "map{element+1}%>%filter{1=1}%>%map{element|(0=0)}".toIR()
        assertThrows<TypeCheckError> {
            ir.accept(CRTypeInferenceVisitor())
        }
    }

    @Test
    fun `test ambiguous fail`() {
        val ir = "map{element|(element>0)}".toIR()
        assertThrows<TypeCheckError> {
            ir.accept(CRTypeInferenceVisitor())
        }
    }
}
