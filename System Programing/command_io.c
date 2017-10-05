#include "ss.h"
#include <string.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

Command makeCommand(char* input, char* output, char** arguments, int n){
    Command this = malloc(sizeof(struct Command_s));
    this->programName = arguments[0];
    this->input = input;
    this->output = output;
    this->args = arguments;
    this->argn = n;
    return this;
}

Command parseCommand(char* line){
    //initializing command components
    char* input = NULL;
    char* output = NULL;
    char** arguments = malloc((strlen(line) * sizeof(char*))/2);//the max arguments in 1024 bytes is half of that

    char buffer[strlen(line)];
    int j = 0; //buffer index
    bool inQuotes = false;
    bool isInput = false;
    bool isOutput = false;
    int n = 0; //argmumnet index
    //parsing
    for(int i = 0; i < (int)strlen(line); i++){
        char c = line[i];
        switch(c){
            case '\n':
                if(inQuotes){
                    printf("Error: No closing \" found.\n");
                    exit(0);
                }
                if((isInput||isOutput)&&j==0){
                    printf("Error: Expected a file name to redirect.\n");
                    exit(0);
                }
            case ' ':
                if(inQuotes){
                    buffer[j++] = c;
                }else if(line[i-1]=='>'||line[i-1]=='<'){
                    ;//skip the space after a < or > 
                }else if(j!=0){//ignore multiple spaces
                    char* current;
                    current = malloc(j * sizeof(char) + 1);
                    memcpy(current, buffer, j);
                    current[j] = '\0';
                    j = 0;
                    //puting the data in the right place
                    if(isOutput){
                        if(output!=NULL){
                            printf("Error: More than one output redirections.\n");
                            exit(0);
                        }
                        output = current;
                        n--;//decreasing n as no agrument was added
                    }else if(isInput){
                        if(input!=NULL){
                            printf("Error: More than one input redirections.\n");
                            exit(0);
                        }
                        input = current;
                        n--;//decreasing n as no agrument was added
                    }else{
                        arguments[n] = current;
                    }
                    n++;
                    isOutput = false;
                    isInput = false;
                }
                break;
            case '"':
                inQuotes = !inQuotes;
            break;
            case '>':
                isOutput = true;
                break;
            case '<':
                isInput = true;
                break;
            default: 
                buffer[j++] = c;
                break;
        }
    }
    return makeCommand(input, output, arguments, n);
}

void describeCommand(Command instr){
    printf("Run program \"%s\"", instr->programName);
    //arguments
    int n = instr->argn;
    if(n==1){
        printf(". ");
    }else if(n==2){
        printf(" with argument \"%s\". ", instr->args[1]);
    }else {
        printf(" with arguments ");
        for(int i = 1; i < n; i++){
            if(i!=n-1){
                printf("\"%s\" and ", instr->args[i]);
            }
            else{
                printf("\"%s\". ", instr->args[i]);
            }
        }
    }
    //input and output
    if(instr->input != NULL){
        printf("Read the input from file \"%s\". ", instr->input);
    }
    if(instr->output != NULL){
        printf("Write the output to file \"%s\". ", instr->output);
    }
    printf("\n");
}

void freeCommand(Command inst){
    char** args = inst->args;
    for(int i = 0; i < inst->argn; i++){
        free(args[i]);
    }
    free(args);
    free(inst);
}