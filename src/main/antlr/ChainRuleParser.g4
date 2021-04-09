parser grammar ChainRuleParser;

options {
	tokenVocab = ChainRuleLexer;
}

program: callChain EOF;

callChain: call | callChain COMPOSITION call;

call: mapCall | filterCall;

mapCall: MAP LCUR expression RCUR;

filterCall: FILTER LCUR expression RCUR;

expression: binaryBitwiseAndExpression |  expression OR binaryBitwiseAndExpression;

binaryBitwiseAndExpression: binaryEqualityExpression | binaryBitwiseAndExpression AND binaryEqualityExpression;

binaryEqualityExpression: binaryRelationalExpression |
	binaryEqualityExpression EQ binaryRelationalExpression;

binaryRelationalExpression: binaryPlusMinusExpression |
	binaryRelationalExpression (LT | GT) binaryPlusMinusExpression;

binaryPlusMinusExpression: binaryTimesExpression |
	binaryPlusMinusExpression (PLUS | MINUS) binaryTimesExpression;

binaryTimesExpression: signedAtom | binaryTimesExpression TIMES signedAtom;

signedAtom: PLUS signedAtom | MINUS signedAtom | atom;

atom: element | constant | parenthesizedExpression;

parenthesizedExpression: LPAR expression RPAR;

constant: CONSTANT;

element: ELEMENT;
