grammar NoColon;

program
    : statement* EOF
    ;

statement
    : variableAssignment
    | ifStatement
    | whileStatement
    | functionDeclaration
    | returnStatement
    | LBRACE block RBRACE
    ;


variableAssignment
    : IDENTIFIER ASSIGN expression
    ;

ifStatement
    : IF expression THEN statement (ELSE statement)?
    ;

whileStatement
    : WHILE expression DO block
    ;

functionDeclaration
    : FUN IDENTIFIER LPAREN parameterList? RPAREN LBRACE block RBRACE
    ;

parameterList
    : parameter (COMMA parameter)*
    ;

parameter
    : IDENTIFIER
    ;

returnStatement
    : RETURN expression
    ;

block
    : statement (COMMA? statement)*
    ;

expression
    : IDENTIFIER LPAREN argumentList? RPAREN            # FunctionCallExpr
    | primary                                           # PrimaryExpr
    | NOT expression                                    # NotExpr
    | expression (MULT | DIV | MOD) expression          # MulDivExpr
    | expression (PLUS | MINUS) expression              # AddSubExpr
    | expression (LT | GT | LE | GE) expression         # ComparisonExpr
    | expression (EQ | NEQ) expression                  # EqualityExpr
    | expression AND expression                         # AndExpr
    | expression OR expression                          # OrExpr
    ;

argumentList
    : expression (COMMA expression)*
    ;

primary
    : LPAREN expression RPAREN                          # ParenExpr
    | INTEGER_LITERAL                                   # IntLiteral
    | TRUE                                              # TrueLiteral
    | FALSE                                             # FalseLiteral
    | IDENTIFIER                                        # IdentifierExpr
    ;

// Keywords
FUN         : 'fun' ;
IF          : 'if' ;
THEN        : 'then' ;
ELSE        : 'else' ;
WHILE       : 'while' ;
DO          : 'do' ;
RETURN      : 'return' ;
TRUE        : 'true' ;
FALSE       : 'false' ;

// Literals
INTEGER_LITERAL : [0-9]+ ;

// Operators
PLUS    : '+' ;
MINUS   : '-' ;
MULT    : '*' ;
DIV     : '/' ;
MOD     : '%' ;
ASSIGN  : '=' ;
EQ      : '==' ;
NEQ     : '!=' ;
LT      : '<' ;
GT      : '>' ;
LE      : '<=' ;
GE      : '>=' ;
AND     : '&&' ;
OR      : '||' ;
NOT     : '!' ;

// Delimiters
LPAREN  : '(' ;
RPAREN  : ')' ;
LBRACE  : '{' ;
RBRACE  : '}' ;
COMMA   : ',' ;

// Identifiers
IDENTIFIER : [a-zA-Z_][a-zA-Z0-9_]* ;

// Whitespace and Comments
WS          : [ \t\r\n]+ -> skip ;
LINE_COMMENT: '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT: '/*' .*? '*/' -> skip ;