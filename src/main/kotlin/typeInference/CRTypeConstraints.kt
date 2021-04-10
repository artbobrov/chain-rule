package typeInference

import ir.CRIRElement
import kotlin.reflect.KClass

class CRTypeConstraints private constructor(
    private val constraints: MutableMap<CRIRElement, CRTypeConstraint>
) : MutableMap<CRIRElement, CRTypeConstraint> by constraints {
    constructor(vararg constraints: CRTypeConstraint) : this(constraints.associateBy { it.ir }.toMutableMap())

    operator fun plusAssign(other: CRTypeConstraint) {
        constraints[other.ir] = other
    }

    operator fun plusAssign(other: CRTypeConstraints) {
        constraints += other.constraints
    }

    fun <T : CRIRElement> constraintsForType(klass: KClass<T>): List<CRTypeConstraint> {
        return constraints.values.filter { klass.isInstance(it.ir) }
    }
}
