%{
#include <stdio.h>
#include <stdlib.h>

int line_num = 1;  // Track line numbers
FILE *input_file;
%}

%option noyywrap

%%

[ \t\r]+               {/* Ignore whitespaces */}
\n                     { line_num++; }  /* Count newlines */
\/\/.*                 {printf("Comment: %s\n", yytext);}
"/*"([^*]|\*+[^*/])*\*+"/" {/* Ignore multi-line comments */}

 /* ➤ KEYWORDS */
"auto"|"break"|"case"|"char"|"const"|"continue"|"default"|"do"|"double"|"else"|"enum"|"extern"|"float"|"for"|"goto"|"if"|"inline"|"int"|"long"|"register"|"restrict"|"return"|"short"|"signed"|"sizeof"|"static"|"struct"|"switch"|"typedef"|"union"|"unsigned"|"void"|"volatile"|"while" { printf("Keyword: %s\n", yytext); }

 /* ➤ IDENTIFIERS */
[A-Za-z_][A-Za-z0-9_]*  { printf("Identifier: %s\n", yytext); }

 /* ➤ CONSTANTS (Integers & Floating-point numbers) */
[0-9]+                  { printf("Constant: %s\n", yytext); }
[0-9]+"."[0-9]+         { printf("Constant: %s\n", yytext); }

 /* ➤ OPERATORS */
"=="|"!="|"<="|">="|"&&"|"||"|"+"|"-"|"*"|"/"|"%"|"="  { printf("Operator: %s\n", yytext); }

 /* ➤ PUNCTUATION */
.  { printf("Punctuation: %s\n", yytext); }

%%

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("Usage: %s <c_source_file.c>\n", argv[0]);
        return 1;
    }

    input_file = fopen(argv[1], "r");
    if (!input_file) {
        printf("Error: Could not open file %s\n", argv[1]);
        return 1;
    }

    yyin = input_file; // Set input stream for Lex
    yylex(); // Run lexical analysis

    fclose(input_file);
    return 0;
}
