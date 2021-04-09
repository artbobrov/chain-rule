package errors

import ir.CRIRElement

class TypeCheckError: IllegalStateException {
    constructor(message: String) : super(transformMessage(message))
    constructor(message: String, cause: Throwable) : super(transformMessage(message), cause)
    constructor(cause: Throwable) : super(cause)

    companion object {
        private fun transformMessage(message: String): String {
            return "TYPE ERROR. $message"
        }
    }
}

fun typeResolutionError(ir: CRIRElement): Nothing {
    error("Cannot resolve type for ${ir.text}")
}