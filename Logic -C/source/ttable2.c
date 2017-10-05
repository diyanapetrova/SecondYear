#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "tt2.h"

#define N 0
#define C 1
#define D 2
#define E 3
#define S 4

int size = 100;
int n;
int t;//number of lines that are true
int flag;

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
        vars[arrIndex++] = toAdd;
    }
    return vars;
}

void printCategories(){
    if(t==pow(2,n))
        printf("Valid/Tautology.\n");
    else if(t==0){
        printf("Unsatisfiable.\n");
    }else{
        printf("Satisfiable.\n");
    }
}

void printTable(char* first_exp, char* second_exp){
    for(int line = 0; line < pow(2, n); line++){
        int* vars = generateVars(line);
        switch(flag){
            case N:
            case C:
            case E:
                printVars(vars, n);
                calculate(vars, first_exp, 0);
                break;
            case D:{
                if(calculate(vars, first_exp, 1)){
                    printVars(vars, n);
                    calculate(vars, second_exp, 0);
                }   
            }break;
            case S:
                calculate(vars, first_exp, 1);
                break;
            default:
                printf("Should never get here!");
                exit(-1);
        }
        free(vars);
    }
}

int calculate(int* vars, char* expression, int silent){

    int stack[size];
    int i = 0;//stack index
    int point = 0;//point at the expression

    char current = expression[i];
    while(current != '\0'){
        if(current > 96 && current < 123){ //boolean var
            stack[i++] = vars[current - 97];
            if(!silent)
                printf(" ");
        }else if(current == '0' || current == '1'){ //boolean const
            stack[i++] = (current - 48);
            if(!silent)
                printf(" ");
        }else{ // operators
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
            if(!silent)
                printf("%d",result);
            stack[i++] = result;
        }//else
        current = expression[++point];
    }//while
    int result = stack[--i];
    if(!silent)
        printf(" :   %d\n", result);
    if(result) t++;
    if(flag==S&&result){
        printVars(vars, n);
        printf(" :   %d\n", result);
        exit(0);
    }
    return result;
}

void validation(char* expression){
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
            ;//the number of values on the stack doesn't change'
        }else{
            printf("Unrecognizable char %c\n", current);
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

void printVars(int* arr, int size){
    for(int i = 0; i < size; i++){
        printf("%d ",arr[i]);
    }
     printf(": ");
}

int main(int argc, char *argv[]){
    if(argc<4){
        printf("No or less arguments than expected. Should be <flag> <number of vars> <expression> ... \n");
        exit(0);
    }

    n = atoi(argv[2]);
    if(n==0||n>26){
        printf("Illegal number of propositional variables!\n");
        exit(0);
    }

    char* first_exp = argv[3];
    char* second_exp = NULL;

    //flag
    switch(argv[1][0]){
        case 'n': //normal
            flag = N;
            break;
        case 'c': //categories
            flag = C;
            break;
        case 'd'://database + exp
            flag = D;
            second_exp = argv[4];
            validation(second_exp);
            break;
        case 'e':{//equvalence
            flag = E;
            second_exp = argv[4];
            char* temp = malloc(strlen(first_exp)+strlen(second_exp)+2);
            strcpy(temp, first_exp);
            strcat(temp, second_exp);
            strcat(temp, "=");    
            first_exp = temp;}
            break;
        case 's'://solver
            flag = S;
            break;
        default: 
            printf("Invalid flag!\n");
            exit(-1);
    }

    validation(first_exp);

    //printing the first two rows of output
    for(int i = 0; i < n; i++){
        printf("%c ", (97+i));
    }
    printf(": %s : Result\n", first_exp);
    int chars = n*2 + 2 + strlen(first_exp) + 3 + 6;
    while(chars!=0){
        printf("=");
        chars--;
    }
    printf("\n");

    //print truth table
    printTable(first_exp, second_exp);

    //printing conclusion depending on the flag
    if(flag == C) 
        printCategories();
    if(flag == E)
        (t==pow(2, n))?printf("Equivalent.\n"):printf("Not equivalent.\n");
    if(flag==S)
        printf("No solution to this puzzle.\n");
}