%{
#include <stdio.h>
%}

%%

charusat    printf("university");   // Replace "charusat" with "university"
.          putchar(yytext[0]);      // Print other characters as they are
\n         putchar('\n');           // Handle new lines

%%

int main() {
    printf("Enter the input text:\n");
    yylex();  // Call the lexer to process input
    return 0;
}

int yywrap() {
    return 1;
}
