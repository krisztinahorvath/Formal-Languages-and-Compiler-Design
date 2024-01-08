%{
    #include <stdio.h>
    #include <stdlib.h>
    #include <string.h>
    #include "parser.tab.h"
    int lines = 1;
%}

%option noyywrap
%option caseless

DIGIT [0-9]
NON_ZERO_DIGIT [1-9]
INTCONSTANT [+-]?{NON_ZERO_DIGIT}{DIGIT}*|0
LETTER [a-zA-Z_]
SPECIAL_CHAR [ ?:*\^+=.!]
STRINGCONSTANT (\"({LETTER}|{DIGIT}|{SPECIAL_CHAR})*\")
IDENTIFIER {LETTER}({LETTER}|{DIGIT})*
BAD_IDENTIFIER ({DIGIT})+({LETTER})+({LETTER}|{DIGIT})*

%%

"main" { printf("%s - reserved word\n", yytext); return MAIN; }
"integer" { printf("%s - reserved word\n", yytext); return INTEGER; }
"string" { printf("%s - reserved word\n", yytext); return STRING; }
"read" { printf("%s - reserved word\n", yytext); return READ; }
"if" { printf("%s - reserved word\n", yytext); return IF; }
"else" { printf("%s - reserved word\n", yytext); return ELSE; }
"write" { printf("%s - reserved word\n", yytext); return WRITE; }
"while" { printf("%s - reserved word\n", yytext); return WHILE; }
"array" { printf("%s - reserved word\n", yytext); return ARRAY; }

"+" { printf("%s - operator\n", yytext); return PLUS; }
"-" { printf("%s - operator\n", yytext); return MINUS; }
"*" { printf("%s - operator\n", yytext); return TIMES; }
"/" { printf("%s - operator\n", yytext); return DIV; }
"<" { printf("%s - operator\n", yytext); return LESS; }
"<=" { printf("%s - operator\n", yytext); return LESSEQ; }
"=" { printf("%s - operator\n", yytext); return EQ; }
">=" { printf("%s - operator\n", yytext); return BIGGEREQ; }
"==" { printf("%s - operator\n", yytext); return EQQ; }
"!=" { printf("%s - operator\n", yytext); return NEQ; }
">" { printf("%s - operator\n", yytext); return BIGGER; }

"[" { printf("%s - separator\n", yytext); return SQBRACKETOPEN; }
"]" { printf("%s - separator\n", yytext); return SQBRACKETCLOSE; } 
";" { printf("%s - separator\n", yytext); return SEMICOLON; } 
"(" { printf("%s - separator\n", yytext); return OPEN; } 
")" { printf("%s - separator\n", yytext); return CLOSE; }
"{" { printf("%s - separator\n", yytext); return BRACKETOPEN; } 
"}" { printf("%s - separator\n", yytext); return BRACKETCLOSE; } 
"," { printf("%s - separator\n", yytext); return COMMA; }

{IDENTIFIER} { printf("%s - identifier\n", yytext); return IDENTIFIER; }

{BAD_IDENTIFIER} { printf("Error at token %s at line %d\n", yytext, lines); return -1; }

{INTCONSTANT} { printf("%s - integer constant\n", yytext); return INTCONSTANT; }

{STRINGCONSTANT} { printf("%s - string constant\n", yytext); return STRINGCONSTANT; }

[ \t]+ {}

[\n]+ {++lines;}

. {printf("ERROR at token %s at line %d\n", yytext, lines); exit(1);}

%%