#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include "ss.h"

int main(){
    int bufferSize = 1025;//1024 + 1 for the new line char
    char line[bufferSize];
    //used this rather than gets because it is safer
    fgets (line , bufferSize , stdin);
    assert(line != NULL);
    Command instr = parseCommand(line);
    describeCommand(instr);
}