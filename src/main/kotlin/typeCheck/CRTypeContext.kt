package typeCheck

import errors.TypeCheckError
import ir.CRElement
import ir.CRIRElement

data class CRTypeContext(
    val constraints: CRTypeConstraints = CRTypeConstraints()
) {

    fun resolveTypeFor(ir: CRIRElement): CRType? {
        val constraint = constraints[ir] ?: return null
        return when (constraint) {
            is CRTypeConstraint.DependsOn -> {
                val elementTypes = constraint.others.mapNotNull { resolveTypeFor(it) }
                if (elementTypes.size != constraint.others.size) {
                    return null
                }
                val type = elementTypes.first()
                if (!elementTypes.all { it.canConvertTo(type) || type.canConvertTo(it) }) {
                    throw TypeCheckError("Ambigious type of ${ir.text}")
                }
                return constraint.builder(elementTypes)
            }
            is CRTypeConstraint.Exact -> constraint.type
        }
    }

    fun resolveElementType(): Pair<CRElement?, CRType> {
        val elementConstraints = constraints.constraintsForType(CRElement::class)
        fun isAmbiguous(): Boolean {
            val first = elementConstraints.first()
            return elementConstraints.size > 1
                    && !elementConstraints.all { it.second is CRTypeConstraint.Exact && it.second == first.second }
        }
        if (isAmbiguous()) {
            throw IllegalStateException("Ambiguous type of \"element\"")
        }
        val (ir, constraint) = elementConstraints.firstOrNull()
            ?: return null to CRType.Any // No "element" – No constraints
        return when (constraint) {
            is CRTypeConstraint.Exact -> ir to constraint.type
            else -> throw TypeCheckError("Ambigious type of \"element\"")
        }
    }

    fun updateElementType(type: CRType) {
        val elementConstraints = constraints.constraintsForType(CRElement::class)
        val firstIllegal = elementConstraints.firstOrNull {
            val exact = it.second as? CRTypeConstraint.Exact ?: return@firstOrNull true
            !exact.type.canConvertTo(type)
        }
        if (firstIllegal != null) {
            throw TypeCheckError("Cannot convert ${firstIllegal.first.text} to $type")
        }
        for ((element, _) in elementConstraints) {
            update(element, type)
        }
    }

    fun update(ir: CRIRElement, type: CRType) {
        when (val constraint = constraints[ir]) {
            is CRTypeConstraint.Exact -> {
                if (!constraint.type.canConvertTo(type)) { // `constraint.type` is more strict type
                    throw TypeCheckError("Cannot substitute ${constraint.type} type of ${ir.text} to $type")
                }
            }
            is CRTypeConstraint.DependsOn -> {
                for (other in constraint.others) {
                    update(other, constraint.builder(listOf(type)))
                }
            }
        }
        constraints[ir] = CRTypeConstraint.Exact(type)
    }

    fun evalContext(): CRTypeEvalContext {
        return CRTypeEvalContext(this.constraints.toMutableMap())
    }

    operator fun plus(other: CRTypeContext): CRTypeContext {
        return CRTypeContext(constraints + other.constraints)
    }

    operator fun plusAssign(other: CRTypeContext) {
        constraints += other.constraints
    }
}
