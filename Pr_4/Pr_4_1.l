%option noyywrap
%{
    #include<stdio.h>
    int v=0,c=0;
%}
%%
[aeiouAEIOU] {v++;}
[a-zA-Z]     {c++;}
[ \t\n]      {yyterminate();}
.            {yyterminate();}
%%
int main()
{
    printf("Enter String:");
    yylex();
    printf("No of vowels: %d",v);
    return 0;
}

// flex Pr_4_1.l
// gcc lex.yy.c -o Pr_4_1