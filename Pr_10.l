%{
#include "y.tab.h"
#include <stdlib.h>
#include <math.h>
%}

%%

[0-9]+(\.[0-9]+)?  { yylval.val = atof(yytext); return DIGIT; }  /* Changed to yylval.val */
"+"                { return PLUS; }
"-"                { return MINUS; }
"*"                { return MULT; }
"/"                { return DIV; }
"^"                { return POW; }
"("                { return LPAREN; }
")"                { return RPAREN; }
"n"                { return NEWLINE; }
[ \t]              ; /* skip whitespace */
.                  { return yytext[0]; }

%%

int yywrap() {
    return 1;
}