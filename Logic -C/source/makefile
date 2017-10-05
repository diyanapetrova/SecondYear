all : ttable ttable2

clean :
	rm *.o ttable ttable2

ttable :  ttable.o
	clang ttable.o -o ttable -Wall -Wextra -g -lm

ttable2 :  ttable2.o
	clang ttable2.o -o ttable2 -Wall -Wextra -g -lm

#files
ttable.o : ttable.c tt.h
	clang ttable.c -c -o ttable.o -Wall -Wextra -g 

ttable2.o : ttable2.c tt2.h
	clang ttable2.c -c -o ttable2.o -Wall -Wextra -g 
