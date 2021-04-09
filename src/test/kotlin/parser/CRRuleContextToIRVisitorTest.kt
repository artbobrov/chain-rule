package parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import utils.BaseResourcedTestCase
import java.io.File
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CRRuleContextToIRVisitorTest : BaseResourcedTestCase(CRProgramParser::class) {
    private lateinit var parser: CRParser

    @BeforeEach
    fun setUp() {
        parser = CRProgramParser
    }

    @ParameterizedTest
    @MethodSource("fetchSuccessfulFiles")
    fun `test successful ir transformation`(file: File) {
        val code = file.readText().trim()
        val st = parser.parse(file.toPath())
        val ir = st.accept(CRRuleContextToIRVisitor())
        assertEquals(code, ir.text, "IR text should represent file's code")
    }

    fun fetchSuccessfulFiles(): Stream<File> {
        val innerFiles: Array<File> = successDirectory.listFiles() ?: emptyArray()
        return innerFiles.toList().stream()
    }
}