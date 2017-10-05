#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "tt.h"

int size = 100;
int n;
char* expression;

int* generateVars(int line){
    int* vars = malloc(sizeof(int)*n);
    int arrIndex = 0;
    int toAdd = 0;
    for(int i = n - 1; i >= 0; i--){
        if(line == 0){
            toAdd = 0;
        }else{
            toAdd = (line/(int)(pow(2, i)))%2;
        }
        printf("%d ", toAdd);
        vars[arrIndex++] = toAdd;
    }
    printf(": ");
    return vars;
}

void printTable(){
    for(int line = 0; line < pow(2, n); line++){
        int* vars = generateVars(line);
        calculate(vars);
    }
}

void calculate(int* vars){

    int stack[size];
    int i = 0;//stack index
    int point = 0;//point of expression

    char current = expression[i];
    while(current != '\0'){
        if(current > 96 && current < 123){
            stack[i++] = vars[current - 97];
            printf(" ");
        }else if(current == '0' || current == '1'){
            stack[i++] = (current - 48);
            printf(" ");
        }else{
            int result = 0;
            if(current == '-'){ 
                int last = stack[--i];
                result = (last==0) ? 1 : 0;
            }else{
                int f = stack[--i];
                int s = stack[--i];
                switch(current){
                    case '|':
                        result = f|s;
                        break;
                    case '&':
                        result = f&s;
                        break;
                    case '#':
                        result = f^s;
                        break;
                    case '>':
                        result = (!s || f);
                        break;
                    case '=':result = (s==f);
                        break;
                    default: //should never reach this point
                        printf("Unrecognizeble char %c\n", current);
                        exit(-1);
                }//switch
            }//else two arg operator
            printf("%d",result);
            stack[i++] = result;
        }//else
        current = expression[++point];
    }//while
    free(vars);
    printf(" :   %d\n", stack[--i]);
}

void validation(){
    char current = expression[0];
    int stack = 0;
    int point = 0;
    while(current !='\0'){
        if(current > 96 && current < 123){          
            if(current > (n + 96)){
            printf("Found a var out of scope %c\n", current);
            exit(0);
            }else{
                stack++;
            }
        }else if(current == '0' || current == '1'){
            stack++;
        }else if(current == '|'||current == '&'|| current == '#' || current == '>' || current == '='){
            if(stack < 2){
                printf("Empty stack!\n");
                exit(-1);
            }
            stack--;
        }else if(current == '-'){
            ;
        }else{
            printf("Unrecognizable char %c.\n", current);
            exit(-1);
        }
        point++;
        current = expression[point];
    }
   if(stack!=1){
        printf("Incomplete expression!\n");
        exit(-1);
    }
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
    validation();

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
    //print truth table
    printTable();
}