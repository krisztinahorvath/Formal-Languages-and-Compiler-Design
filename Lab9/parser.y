%{
#include "lexer.h"
#include <stdio.h>
#include <stdlib.h>

int yyerror(const char *s);

#define YYDEBUG 1
%}

%token MAIN;
%token INTEGER;
%token STRING;
%token ARRAY;
%token IF;
%token ELSE;
%token WHILE;
%token READ;
%token WRITE;

%token PLUS;
%token MINUS;
%token TIMES;
%token DIV;
%token LESS;
%token LESSEQ;
%token EQ;
%token NEQ;
%token BIGGEREQ;
%token EQQ;
%token BIGGER;

%token SQBRACKETOPEN;
%token SQBRACKETCLOSE;
%token SEMICOLON;
%token OPEN;
%token CLOSE;
%token BRACKETOPEN;
%token BRACKETCLOSE;
%token COMMA;

%token IDENTIFIER;
%token INTCONSTANT;
%token STRINGCONSTANT;

%start Program

%%
Program : MAIN BRACKETOPEN CmpdStmt BRACKETCLOSE { printf("Program -> main { CmpdStmt }\n"); }
        ;

CmpdStmt : StmtList {printf("CmpdStmt -> StmtList\n");}
         ;

StmtList : Stmt StmtList     { printf("StmtList -> Stmt StmtList\n"); }
         | Stmt     { printf("StmtList -> Stmt\n"); }
         ;

Stmt : AssignStmt     { printf("Stmt -> AssignStmt\n"); }
     | IOStmt     { printf("Stmt -> IOStmt\n"); }
     | Declaration     { printf("Stmt -> Declaration\n"); }
     | IfStmt     { printf("Stmt -> IfStmt\n"); }
     | WhileStmt     { printf("Stmt -> WhileStmt\n"); }
     ;

AssignStmt : IDENTIFIER EQ Expression SEMICOLON   { printf("AssignStmt -> IDENTIFIER = Expression ;\n"); }
          ;

Expression : Expression PLUS Term   { printf("Expression -> Expression + Term\n"); }
           | Expression MINUS Term { printf("Expression -> Expression - Term\n"); }
           | Term     { printf("Expression -> Term\n"); }
           ;

Term : Term TIMES Factor     { printf("Term -> Term * Factor\n"); }
     | Term DIV Factor     { printf("Term -> Term / Factor\n"); }
     | Factor     { printf("Term -> Factor\n"); }
     ;

Factor : OPEN Expression CLOSE     { printf("Factor -> ( Expression )\n"); }
       | IDENTIFIER     { printf("Factor -> IDENTIFIER\n"); }
       | STRINGCONSTANT     { printf("Factor -> STRINGCONSTANT\n"); }
       | INTCONSTANT     { printf("Factor -> INTCONSTANT\n"); }
       ;

IOStmt : READ OPEN IDENTIFIER CLOSE SEMICOLON  { printf("IOStmt -> read ( IDENTIFIER ) ;\n"); }
       | WRITE OPEN IDENTIFIER CLOSE SEMICOLON   { printf("IOStmt -> write ( IDENTIFIER ) ;\n"); }
       | WRITE OPEN STRINGCONSTANT CLOSE SEMICOLON   { printf("IOStmt -> write ( STRINGCONSTANT ) ;\n"); }
       ;

Declaration : Type IDENTIFIER SEMICOLON     { printf("Declaration -> Type IDENTIFIER ;\n"); }
            ;

Type : INTEGER     { printf("Type -> INTEGER\n"); }
     | STRING     { printf("Type -> STRING\n"); }
     | ARRAY SQBRACKETOPEN INTCONSTANT SQBRACKETCLOSE    { printf("Type -> ARRAY [INTCONSTANT]\n"); }
     ;

IfStmt : IF OPEN Condition CLOSE BRACKETOPEN StmtList BRACKETCLOSE     { printf("IfStmt -> if (Condition) { StmtList }\n"); }
       | IF OPEN Condition CLOSE BRACKETOPEN StmtList BRACKETCLOSE ELSE BRACKETOPEN StmtList BRACKETCLOSE     { printf("IfStmt -> if (Condition) { StmtList } else { StmtList }\n"); }
       ;

WhileStmt : WHILE OPEN Condition CLOSE BRACKETOPEN StmtList BRACKETCLOSE     { printf("WhileStmt -> while (Condition) { StmtList }\n"); }
          ;

Relation : LESS     { printf("Relation -> <\n"); }
         | LESSEQ     { printf("Relation -> <=\n"); }
         | EQQ     { printf("Relation -> ==\n"); }
         | NEQ     { printf("Relation -> !=\n"); }
         | BIGGEREQ     { printf("Relation -> >=\n"); }
         | BIGGER     { printf("Relation -> >\n"); }
         ;        
Condition : Expression Relation Expression     { printf("Condition -> Expression RELATION Expression\n"); }
          ;

%%
int yyerror(const char *s) {
    printf("Error: %s\n", s);
    return 0;
}

extern FILE *yyin;

int main(int argc, char **argv) {
    if (argc > 1)
        yyin = fopen(argv[1], "r");
    if (!yyparse())
        fprintf(stderr, "\tOK\n");
    return 0;
}
