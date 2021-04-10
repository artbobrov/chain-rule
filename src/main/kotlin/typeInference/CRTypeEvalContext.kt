package typeInference

import ir.CRIRElement

private typealias CRTypeCache = MutableMap<CRIRElement, CRType>

class CRTypeEvalContext(irs: Collection<CRIRElement>, context: CRTypeContext) {
    private val cache: CRTypeCache = mutableMapOf()

    init {
        for (ir in irs) {
            val type = context.inferTypeFor(ir)?.type
            cache[ir] = type ?: CRType.Any
        }
    }

    fun getType(ir: CRIRElement): CRType? {
        return cache[ir]
    }
}
