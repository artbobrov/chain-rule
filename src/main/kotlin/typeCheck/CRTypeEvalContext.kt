package typeCheck

import errors.typeResolutionError
import ir.CRIRElement

private typealias CRTypeCache = MutableMap<CRIRElement, CRType>

class CRTypeEvalContext(constraints: Map<CRIRElement, CRTypeConstraint>) {
    private val cache: CRTypeCache = mutableMapOf()

    init {
        val typeConstraints = constraints.toMutableMap()
        for ((element, constraint) in constraints) {
            val exactConstraint = transferType(element, constraint, typeConstraints)
            cache[element] = exactConstraint.type
        }
    }

    private fun transferType(
        ir: CRIRElement,
        constraint: CRTypeConstraint,
        constraints: MutableMap<CRIRElement, CRTypeConstraint>
    ): CRTypeConstraint.Exact {
        when (constraint) {
            is CRTypeConstraint.DependsOn -> {
                val realConstraints = constraint.others.map {
                    transferType(
                        it,
                        constraints[it] ?: typeResolutionError(it),
                        constraints
                    )
                }
                val realConstraint = CRTypeConstraint.Exact(constraint.builder(realConstraints.map { it.type }))
                constraints[ir] = realConstraint
                return realConstraint
            }
            is CRTypeConstraint.Exact -> {
                return constraint
            }
        }
    }

    fun getType(ir: CRIRElement): CRType? {
        return cache[ir]
    }
}