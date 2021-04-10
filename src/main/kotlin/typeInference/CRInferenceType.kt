package typeInference

sealed class CRInferenceType {
    class Variable private constructor(var info: Info) : CRInferenceType() {
        constructor(type: CRType = CRType.Any) : this(Info(type = type))

        data class Info(val id: Int = newCounter(), var type: CRType)

        override var type: CRType
            get() = info.type
            set(value) {
                info.type = value
            }

        override fun toString(): String {
            return "Variable(id=${info.id}, type=$type)"
        }

        override fun unify(other: CRInferenceType): CRInferenceType? {
            return when {
                type.canConvertTo(other.type) -> other
                other.type.canConvertTo(type) -> this
                else -> null
            }
        }
    }

    data class Concrete(override val type: CRType) : CRInferenceType() {
        override fun unify(other: CRInferenceType): CRInferenceType? {
            return when {
                type.canConvertTo(other.type) -> other
                other.type.canConvertTo(type) -> this
                else -> null
            }
        }
    }

    data class Array(val inner: CRInferenceType) : CRInferenceType() {
        override val type: CRType.Array
            get() = CRType.Array(inner.type)

        override fun unify(other: CRInferenceType): Array? {
            if (other !is Array) {
                return null
            }
            val innerUnified = inner.unify(other.inner) ?: return null
            return Array(innerUnified)
        }

        override fun toString(): String = "[$inner]"
    }

    data class Function1(val argument: Array, val returns: Array) : CRInferenceType() {
        override val type: CRType.Function1
            get() = CRType.Function1(argument.type, returns.type)

        fun canChainWith(other: Function1): Boolean {
            return type.canChainWith(other.type)
        }

        override fun unify(other: CRInferenceType): CRInferenceType? {
            if (other !is Function1) {
                return null
            }
            val argumentUnified = argument.unify(other.argument) ?: return null
            val returnsUnified = argument.unify(other.returns) ?: return null
            return Function1(argumentUnified, returnsUnified)
        }

        override fun toString(): String = "$argument -> $returns"
    }

    abstract val type: CRType

    abstract fun unify(other: CRInferenceType): CRInferenceType?

    companion object {
        private var counter = 0
        fun newCounter() = ++counter
    }
}
