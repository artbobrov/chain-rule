package parser

import antlr.ChainRuleParser
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import java.nio.file.Path

interface CRParser {
    fun parse(stream: CharStream): ChainRuleParser.ProgramContext

    fun parse(string: String): ChainRuleParser.ProgramContext = parse(CharStreams.fromString(string))

    fun parse(path: Path): ChainRuleParser.ProgramContext = parse(CharStreams.fromPath(path))
}
