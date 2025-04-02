%{
#include "y.tab.h"
%}

%%

"i"     { return I; }
"e"     { return E; }
"t"     { return T; }
"a"     { return A; }
"b"     { return B; }
\n      { return 0; }
.       { return yytext[0]; }

%%

int yywrap() {
    return 1;
}