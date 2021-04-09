package typeCheck

import ir.CRIRElement
import kotlin.reflect.KClass
import kotlin.reflect.cast

class CRTypeConstraints private constructor(
    private val constraints: MutableMap<CRIRElement, CRTypeConstraint>
) : MutableMap<CRIRElement, CRTypeConstraint> by constraints {
    constructor(vararg entries: Pair<CRIRElement, CRTypeConstraint>) : this(entries.toMap().toMutableMap())

    operator fun plus(other: CRTypeConstraints): CRTypeConstraints {
        return CRTypeConstraints((constraints + other.constraints).toMutableMap())
    }

    fun <T : CRIRElement> constraintsForType(type: KClass<T>): List<Pair<T, CRTypeConstraint>> {
        return constraints.entries.mapNotNull {
            if (type.isInstance(it.key)) {
                type.cast(it.key) to it.value
            } else {
                null
            }
        }
    }
}