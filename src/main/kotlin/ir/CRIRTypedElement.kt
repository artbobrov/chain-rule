package ir

import typeInference.CRType
import typeInference.CRTypeEvalContext

interface CRIRTypedElement : CRIRElement {
    fun getType(context: CRTypeEvalContext): CRType? = context.getType(this)
}
