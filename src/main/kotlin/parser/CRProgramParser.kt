package parser

import antlr.ChainRuleLexer
import antlr.ChainRuleParser
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream

object CRProgramParser : CRParser {
    private val parser = ChainRuleParser(null).apply {
        removeErrorListeners()
        addErrorListener(ThrowingErrorListener)
    }

    override fun parse(stream: CharStream): ChainRuleParser.ProgramContext {
        val lexer = ChainRuleLexer(stream).apply {
            removeErrorListeners()
            addErrorListener(ThrowingErrorListener)
        }

        val tokenStream = CommonTokenStream(lexer)
        parser.inputStream = tokenStream
        return parser.program()
    }
}