package ir

interface CRIRCallElement : CRIRElement

interface CRIRBasicCallElement : CRIRCallElement {
    var expression: CRIRExpression
}
