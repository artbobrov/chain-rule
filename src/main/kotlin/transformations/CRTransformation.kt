package transformations

import ir.CRIRElement

interface CRTransformation {
    fun transform(ir: CRIRElement): CRIRElement
}
