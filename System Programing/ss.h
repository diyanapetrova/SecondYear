typedef struct Command_s{
    char* programName;
    char* input;
    char* output;
    char** args;
    int argn;
}*Command;

Command makeCommand(char* input, char* output, char** arguments, int n);

Command parseCommand(char* line);

void describeCommand(Command instr);

void freeCommand(Command inst);