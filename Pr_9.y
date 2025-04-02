%{
#include <stdio.h>
#include <stdlib.h>
void yyerror(const char *s);
int yylex(void);
%}

%token I E T A B

%%

Start: S    { printf("Valid string\n"); return 0; }

S: I Exp T S Sp    /* S → i E t S S' */
 | A              /* S → a */
 ;

Sp: E S           /* S' → e S */
  |               /* S' → ε */
  ;

Exp: B            /* E → b */
 ;

%%

void yyerror(const char *s) {
    printf("Invalid string\n");
}

int main() {
    printf("Enter a string to validate: ");
    yyparse();
    return 0;
}