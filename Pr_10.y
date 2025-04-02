%{
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
void yyerror(const char *s);
int yylex(void);
%}

%union {
    double val;
}

%token <val> DIGIT
%token PLUS MINUS MULT DIV POW LPAREN RPAREN NEWLINE

%type <val> E T F G L

%left PLUS MINUS    /* Lower precedence */
%left MULT DIV      /* Medium precedence */
%right POW          /* Highest precedence */

%%

L: E NEWLINE    { printf("%.2f\n", $1); return 0; }

E: E PLUS T     { $$ = $1 + $3; }
 | E MINUS T    { $$ = $1 - $3; }
 | T            { $$ = $1; }

T: T MULT F     { $$ = $1 * $3; }
 | T DIV F      { $$ = $1 / $3; }
 | F            { $$ = $1; }

F: G POW F      { $$ = pow($1, $3); }
 | G            { $$ = $1; }

G: LPAREN E RPAREN { $$ = $2; }
 | DIGIT           { $$ = $1; }

%%

void yyerror(const char *s) {
    printf("Invalid expression\n");
}

int main() {
    printf("Enter an arithmetic expression (followed by 'n'): ");
    yyparse();
    return 0;
}