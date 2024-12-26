#include<stdio.h>

void main(){

    // int no_of_input_symbols;
    // printf("Number of input symbols : ");
    // scanf("%d",no_of_input_symbols);

    char str[100];
    scanf("%s",str);

    int i = 0;
    while(str[i] == 'a'){
        i++;
    }
    if(str[i] == 'b' && str[i+1] == 'b' && str[i+2] == '\0'){
        printf("Valid\n");
    }else{
        printf("Invalid\n");
    }
}