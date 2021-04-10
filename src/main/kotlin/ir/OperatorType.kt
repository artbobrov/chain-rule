package ir

sealed class OperatorType(val text: String) {
    object Plus : OperatorType("+") {
        fun action(left: Int, right: Int): Int = left + right
    }

    object Minus : OperatorType("-") {
        fun action(left: Int, right: Int): Int = left - right
    }

    object Times : OperatorType("*") {
        fun action(left: Int, right: Int): Int = left * right
    }

    object Gt : OperatorType(">") {
        fun action(left: Int, right: Int): Boolean = left > right
    }

    object Lt : OperatorType("<") {
        fun action(left: Int, right: Int): Boolean = left < right
    }

    object Eq : OperatorType("=") {
        fun <T> action(left: T, right: T): Boolean = left == right
    }

    object And : OperatorType("&") {
        fun action(left: Boolean, right: Boolean): Boolean = left && right
    }

    object Or : OperatorType("|") {
        fun action(left: Boolean, right: Boolean): Boolean = left || right
    }

    companion object {
        fun fromText(text: String): OperatorType? {
            return OperatorType::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .firstOrNull { it.text == text }
        }

        fun isOperator(text: String): Boolean {
            return fromText(text) != null
        }
    }
}
