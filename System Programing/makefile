all : shellsplit runcmds

clean :
	rm *.o shellsplit runcmds

shellsplit :  shellsplit.o command_io.o
	clang shellsplit.o command_io.o -o shellsplit -Wall -Wextra -g
runcmds :  runcmds.o command_io.o
	clang runcmds.o command_io.o -o runcmds -Wall -Wextra -g

#files
shellsplit.o : shellsplit.c ss.h
	clang shellsplit.c -c -o shellsplit.o -Wall -Wextra -g 

command_io.o : command_io.c ss.h
	clang command_io.c -c -o command_io.o -Wall -Wextra -g 

runcmds.o : runcmds.c
	clang runcmds.c -c -o runcmds.o -Wall -Wextra -g 