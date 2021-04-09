package parser

import errors.SyntaxError
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import utils.BaseResourcedTestCase
import java.io.File
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CRProgramParserTest : BaseResourcedTestCase(CRProgramParserTest::class) {
    private lateinit var parser: CRParser

    @BeforeEach
    fun setUp() {
        parser = CRProgramParser
    }

    @ParameterizedTest
    @MethodSource("fetchSuccessfulFiles")
    fun `test successful parsing`(file: File) {
        assertDoesNotThrow {
            parser.parse(file.toPath())
        }
    }

    @ParameterizedTest
    @MethodSource("fetchFailureFiles")
    fun `test unsuccessful parsing`(file: File) {
        assertThrows<SyntaxError> {
            parser.parse(file.toPath())
        }
    }

    fun fetchSuccessfulFiles(): Stream<File> {
        val innerFiles: Array<File> = successDirectory.listFiles() ?: emptyArray()
        return innerFiles.toList().stream()
    }

    fun fetchFailureFiles(): Stream<File> {
        val innerFiles: Array<File> = failureDirectory.listFiles() ?: emptyArray()
        return innerFiles.toList().stream()
    }
}