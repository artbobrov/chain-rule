package errors

class SyntaxError : IllegalArgumentException {
    constructor(message: String) : super(transformMessage(message))
    constructor(message: String, cause: Throwable) : super(transformMessage(message), cause)
    constructor(cause: Throwable) : super(cause)

    companion object {
        private fun transformMessage(message: String): String {
            return "SYNTAX ERROR. $message"
        }
    }
}