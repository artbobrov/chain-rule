package typeCheck

import ir.CRIRElement

sealed class CRTypeConstraint {
    data class Exact(val type: CRType) : CRTypeConstraint()

    data class DependsOn(
        val others: List<CRIRElement>,
        val builder: (List<CRType>) -> CRType
    ) : CRTypeConstraint() {
        constructor(
            other: CRIRElement,
            builder: (CRType) -> CRType = { it }
        ) : this(listOf(other), { builder(it.first()) })

        init {
            require(others.isNotEmpty()) {
                "\"Connected\" constraint of elements should not be empty"
            }
        }
    }
}
