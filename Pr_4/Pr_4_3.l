%{
#include <stdio.h>

int char_count = 0, word_count = 0, line_count = 0;
%}

%%

[\n]     { line_count++; char_count++; }  
[ \t]+ { char_count+=yyleng;}                        
[^  \t\n]+ { word_count++; char_count+=yyleng;}   

%%

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Usage: %s <filename>\n", argv[0]);
        return 1;
    }

    FILE *file = fopen(argv[1], "r");
    if (!file) {
        printf("Error opening file: %s\n", argv[1]);
        return 1;
    }

    yyin = file;  
    yylex();      
    fclose(file);

    printf("Characters: %d\n", char_count);
    printf("Words: %d\n", word_count);
    printf("Lines: %d\n", line_count);

    return 0;
}

int yywrap() {
    return 1;
}
