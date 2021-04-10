package typeInference

import ir.CRIRElement

sealed class CRTypeConstraint {
    data class Exact(
        override val ir: CRIRElement,
        val inferenceType: CRInferenceType
    ) : CRTypeConstraint()

    data class DependsOn(
        override val ir: CRIRElement,
        val others: List<CRIRElement>,
        val builder: (List<CRInferenceType>) -> CRInferenceType
    ) : CRTypeConstraint() {
        constructor(
            ir: CRIRElement,
            other: CRIRElement,
            builder: (CRInferenceType) -> CRInferenceType = { it }
        ) : this(ir, listOf(other), { builder(it.first()) })
    }

    abstract val ir: CRIRElement
}
