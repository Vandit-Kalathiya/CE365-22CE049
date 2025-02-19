%{
#include <stdio.h>
#include <string.h>
#include <ctype.h>

int uppercase = 0, lowercase = 0, digit = 0, special = 0;
int length = 0;

void check_password();
%}

%option noyywrap 

%%
[a-z]        { lowercase = 1; length++; }
[A-Z]        { uppercase = 1; length++; }
[0-9]        { digit = 1; length++; }
[*;#$@]      { special = 1; length++; }
.            { length++; }

\n           { check_password(); length = 0; uppercase = 0; lowercase = 0; digit = 0; special = 0; }
%%

void check_password() {
    if (length < 9 || length > 15) {
        printf("Invalid Password: Length must be between 9 and 15 characters.\n");
    } else if (!uppercase) {
        printf("Invalid Password: Must contain at least one uppercase letter.\n");
    } else if (!lowercase) {
        printf("Invalid Password: Must contain at least one lowercase letter.\n");
    } else if (!digit) {
        printf("Invalid Password: Must contain at least one digit.\n");
    } else if (!special) {
        printf("Invalid Password: Must contain at least one special character (*, ;, #, $, @).\n");
    } else {
        printf("Valid Password.\n");
    }
}

int main() {
    printf("Enter password to validate:\n");
    yylex();
    return 0;
}
