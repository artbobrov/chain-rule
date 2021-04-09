lexer grammar ChainRuleLexer;

MAP: 'map';

FILTER: 'filter';

CONSTANT: NUMBER;

ELEMENT: 'element';

LCUR: '{';

RCUR: '}';

LPAR: '(';

RPAR: ')';

PLUS: '+';

MINUS: '-';

TIMES: '*';

GT: '>';

LT: '<';

EQ: '=';

AND: '&';

OR: '|';

COMPOSITION: '%>%';

WS: [ \r\t\n] -> skip;

fragment NUMBER: DIGIT+;

fragment DIGIT: '0' ..'9';
