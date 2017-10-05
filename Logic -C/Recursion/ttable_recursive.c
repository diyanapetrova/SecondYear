#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <math.h>
#include "tt_r.h"

int size = 100;
int n;
char* expression;
int line = 0;

void reading(){
    int* stack = malloc(sizeof(int)*size);
    int i = 0;
    recursion(stack, i, 0);
}

void recursion(int* stack, int i, int point){
    char current = expression[point];
    if(current > 96 && current < 123){          //-> boolean variable is a lower case letter 
        if(current > (n + 96)){
            printf("Found a var out of scope %c\n", current);
            exit(0);
        }
        //printf("letter %c \n", current);
        int* stack_f = malloc(sizeof(int)*size);
        memcpy(stack_f, stack, sizeof(int)*(i+1));
        int* stack_s = malloc(sizeof(int)*size);
        memcpy(stack_s, stack, sizeof(int)*(i+1));
        int j = i;
        push(0, stack_f, &i);
        recursion(stack_f, i, ++point);
        push(1, stack_s, &j);
        recursion(stack_s, j, point);
    }else if(current == 48 || current ==49){    //-> boolean constant
        push((current - 48), stack, &i);
        recursion(stack, i, ++point);
    }else{                                      //-> operator or undefined
        int over = 0;
        switch(current){
            case '|':{
                int f = pop(stack, &i);
                int s = pop(stack, &i);
                push(f|s, stack, &i);}
                break;
            case '&':{
                int f = pop(stack, &i);
                int s = pop(stack, &i);
                push(f&s, stack, &i);}
                break;
            case '#':{
                int f = pop(stack, &i);
                int s = pop(stack, &i);
                push(f^s, stack, &i);}
                break;
            case '>':{
                int f = pop(stack, &i);
                int s = pop(stack, &i);
                push(f>s, stack, &i);}
                break;
            case '=':{
                int f = pop(stack, &i);
                int s = pop(stack, &i);
                push(f=s, stack, &i);}
                break;
             case '-':{
                bool f = pop(stack, &i);
                if(f)
                    push(false, stack, &i);
                else push(true, stack, &i);
                }
                break;
            case '\0':
                if(i==1){
                    printVars();
                    printf(" %d \n", pop(stack, &i));
                    line = line + 1;
                    over = 1;
                    free(stack);
                }else{
                    printf("Finished, but there isn't only one thing left on the stack, but %d.\n", i);
                    exit(-1);
                }
                break;
            default: 
                printf("Unrecognizeble char %c\n", current);
                exit(-1);
        }
        if(over==0)
            recursion(stack, i, ++point);
    }
}


void push(int value, int* stack, int* i){
    if(*i == size){
        printf("Overflow in the stack!\n");
        exit(-1);
    }
    stack[*i] = value;
    int newIndex = ++*i;
    *i = newIndex;

}

int pop(int* stack, int* i){
    if(*i==0){
        printf("No values on the stack!\n");
        exit(-1);
    }
    int newIndex = --*i;
    *i = newIndex;
    return stack[*i];
}

void printVars(){
    for(int i = n - 1; i >= 0; i--){
        if(line < (int)(pow(2, i))){
            printf("0 ");
        }else if((line/(int)(pow(2, i)))%2==0){
            printf("0 ");
        }else{
            printf("1 ");
        }
    }
    printf(": ");
}

int main(int argc, char *argv[]){
    if(argc!=3){
        printf("Illegal number of arguments!\n");
        exit(0);
    }

    n = atoi(argv[1]);
    if(n==0||n>26){
        printf("Illegal number of propositional variables!\n");
        exit(0);
    }
    
    expression = argv[2];
    //printing the first two rows of output
    for(int i = 0; i < n; i++){
        printf("%c ", (97+i));
    }
    printf(": %s : Result\n", expression);
    int chars = n*2 + 2 + strlen(expression) + 3 + 6;
    while(chars!=0){
        printf("=");
        chars--;
    }
    printf("\n");
    reading();
}