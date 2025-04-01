%{
#include <stdio.h>
#include <ctype.h>
#include <string.h>
int c = 0;
void reverse_case(char *str);
%}

%%

[iI][nN][dD][iI][aA]         { printf("INDIA"); }
[a-zA-Z]+       { 
                  if (strcmp(yytext, "india") != 0) {
                      reverse_case(yytext);
                  }
                }
[\?]+              { c++; printf("?"); }
[ \t\n]           { printf("%s",yytext); }

%%

void reverse_case(char *str) {
    char result[100];
    int i;
    strcpy(result, str);
    for (i = 0; result[i]; i++) {
        if (islower(result[i])) {
            result[i] = toupper(result[i]);
        } else if (isupper(result[i])) {
            result[i] = tolower(result[i]);
        }
    }
    printf("%s", result);
}

int yywrap() {
    return 1;
}

int main() {
    printf("Enter text :\n");
    yylex();
    printf("\nNumber of question sentences : %d\n", c);
    return 0;
}