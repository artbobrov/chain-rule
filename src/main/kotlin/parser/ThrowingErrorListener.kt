package parser

import errors.SyntaxError
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

object ThrowingErrorListener : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        when {
            msg != null && e != null -> throw SyntaxError(msg, e)
            msg != null -> throw SyntaxError(msg)
            e != null -> throw SyntaxError(e)
            else -> throw SyntaxError("Unknown syntax error at $line:$charPositionInLine")
        }
    }
}
