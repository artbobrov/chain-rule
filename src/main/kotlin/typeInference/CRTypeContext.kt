package typeInference

import errors.TypeCheckError
import ir.CRElement
import ir.CRIRElement

data class CRTypeContext(private val constraints: CRTypeConstraints = CRTypeConstraints()) {
    operator fun plusAssign(other: CRTypeContext) {
        constraints += other.constraints
    }

    operator fun plusAssign(other: CRTypeConstraint) {
        add(other)
    }

    fun evalContext(): CRTypeEvalContext {
        return CRTypeEvalContext(constraints.keys, this)
    }

    fun resolveElementOrCreate(): CRElement {
        val elementConstraints = constraints.constraintsForType(CRElement::class)
        if (elementConstraints.isEmpty()) {
            // put fake element for appearance in function type
            this += CRTypeConstraint.Exact(CRElement(listOf()), CRInferenceType.Variable())
            return resolveElementOrCreate()
        }

        fun canBeUnified(inferredTypes: List<CRInferenceType>): Boolean {
            return inferredTypes.all { inferred ->
                inferredTypes.all { it.unify(inferred) != null }
            }
        }

        fun isAmbiguous(): Boolean = !canBeUnified(elementConstraints.mapNotNull { inferTypeFor(it.ir) })
        if (isAmbiguous()) {
            throw TypeCheckError("Types of elements are ambiguous")
        }

        val elementConstraint = elementConstraints.first()
        return elementConstraint.ir as CRElement
    }

    fun inferTypeFor(ir: CRIRElement): CRInferenceType? {
        return when (val constraint = constraints[ir]) {
            is CRTypeConstraint.DependsOn -> {
                val inferenceTypes = constraint.others.mapNotNull { inferTypeFor(it) }
                if (inferenceTypes.size != constraint.others.size) {
                    return null
                }
                return constraint.builder(inferenceTypes)
            }
            is CRTypeConstraint.Exact -> constraint.inferenceType
            null -> null
        }
    }

    private fun add(constraint: CRTypeConstraint) {
        when (constraint) {
            is CRTypeConstraint.DependsOn -> {
                val currentInferredType = inferTypeFor(constraint.ir)
                constraints += constraint
                val inferredType = inferTypeFor(constraint.ir) ?: throw TypeCheckError(
                    "Inference type of ${constraint.ir.text} failed"
                )
                if (currentInferredType == null) {
                    constraints += CRTypeConstraint.Exact(constraint.ir, inferredType)
                    return
                }
                val unifiedType = inferredType.unify(currentInferredType)
                    ?: throw TypeCheckError(
                        "Types ${inferredType.type} are not compatible with ${currentInferredType.type}"
                    )
                if (currentInferredType is CRInferenceType.Variable) {
                    if (inferredType is CRInferenceType.Variable) {
                        // connect the second variables
                        inferredType.info.type = unifiedType.type
                        currentInferredType.info = inferredType.info
                    }
                    // update the type of variable
                    currentInferredType.type = unifiedType.type
                }
                constraints += CRTypeConstraint.Exact(constraint.ir, currentInferredType)
            }
            is CRTypeConstraint.Exact -> {
                val currentInferredType = inferTypeFor(constraint.ir)
                if (currentInferredType == null) {
                    constraints += constraint // first constraint for type ir
                    return
                }
                val unifiedType = currentInferredType.unify(constraint.inferenceType)
                    ?: throw TypeCheckError(
                        "Cannot constraint type ${currentInferredType.type} with type ${constraint.inferenceType.type}"
                    )
                if (currentInferredType is CRInferenceType.Variable) {
                    // update the type of variable
                    currentInferredType.type = unifiedType.type
                } else {
                    constraints += constraint.copy(inferenceType = unifiedType)
                }
            }
        }
    }
}
