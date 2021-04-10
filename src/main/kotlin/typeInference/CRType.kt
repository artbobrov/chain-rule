package typeInference

sealed class CRType {
    object Int : CRType() {
        override fun toString(): String = "Int"

        override fun canConvertTo(other: CRType): kotlin.Boolean = other == Int
    }

    object Boolean : CRType() {
        override fun toString(): String = "Boolean"

        override fun canConvertTo(other: CRType): kotlin.Boolean = other == Boolean
    }

    data class Array(val inner: CRType) : CRType() {
        override fun toString(): String = "[$inner]"

        override fun canConvertTo(other: CRType): kotlin.Boolean {
            return other is Array && inner.canConvertTo(other.inner)
        }
    }

    data class Function1(val argument: Array, val returns: Array) : CRType() {
        override fun toString(): String = "$argument -> $returns"

        override fun canConvertTo(other: CRType): kotlin.Boolean {
            return other is Function1 && argument.canConvertTo(other.argument) && returns.canConvertTo(other.returns)
        }

        fun canChainWith(other: Function1): kotlin.Boolean {
            return returns.canConvertTo(other.argument) || other.argument.canConvertTo(returns)
        }
    }

    object Any : CRType() {
        override fun toString(): String = "Any"

        override fun canConvertTo(other: CRType): kotlin.Boolean = true
    }

    abstract fun canConvertTo(other: CRType): kotlin.Boolean
}
