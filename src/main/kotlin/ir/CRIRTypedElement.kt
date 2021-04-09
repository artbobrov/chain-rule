package ir

import typeCheck.CRType
import typeCheck.CRTypeConstraints
import typeCheck.CRTypeContext

interface CRIRTypedElement {
    fun getType(context: CRTypeContext): CRType?
}

abstract class CRIRTypedElementBase(children: List<CRIRElement>) : CRIRElementBase(children), CRIRTypedElement {
    override fun getType(context: CRTypeContext): CRType? = context.resolveTypeFor(this)
}