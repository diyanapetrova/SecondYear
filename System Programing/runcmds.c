#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <assert.h>
#include <string.h>
#include "ss.h"

int d_stdout;

void set(){
	d_stdout = dup(1);
}

void redirectIn(char* file){
	if(file!=NULL){
		FILE* check = freopen(file, "r", stdin);
		if(check==NULL){
			printf("Read failed: %s \n", file);
			exit(0);
		}
	}
}

void redirectOut(char* file){
	if(file!=NULL){
		FILE* check = freopen(file, "w", stdout);
		if(check==NULL){
			FILE * fp;
			fp = fdopen (d_stdout, "w");
			fprintf(fp, "Write failed: %s \n", file);
			fclose(fp);
			exit(0);
		}
	}
}

int main(){
	while(1){
		//getting instruction
		int bufferSize = 1024;
    	char line[bufferSize];
    	char *result = fgets (line , bufferSize , stdin);
    	if(result==NULL){
			break;
		}
    	Command comm = parseCommand(line);
		
		//
		int child = fork();
		if (child != 0){
			/* Parent */
			int stat_loc;
			(void)wait(&stat_loc);
		}else{
			/* Child */
			set();
			char **args = comm->args;
			redirectIn(comm->input);//this should be first and there is no checking there because the output is in tact
			redirectOut(comm->output);
			execv(comm->args[0], args);//everything after this is executed if this line fails
			//if the output is directed to a file the message still needed to be sent to the default output
			FILE * fp;
			fp = fdopen (d_stdout, "w");
			fprintf(fp, "Execute failed: %s\n", comm->programName);
			fclose(fp);
			exit(0);
		}
		
		freeCommand(comm);
	}
}
