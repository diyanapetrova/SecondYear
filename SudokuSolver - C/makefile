all : sudoku_check sudoku_solver sudoku_advanced

clean :
	rm *.o sudoku_check sudoku_solver sudoku_advanced

sudoku_solver :  sudoku_io.o sudoku_check.o sudoku_solver.o
	clang sudoku_io.o sudoku_check.o sudoku_solver.o -o sudoku_solver -Wall -Wextra -g

#using the basic as advanced
#sudoku_advanced :  sudoku_io.o sudoku_check.o sudoku_solver.o
#	clang sudoku_io.o sudoku_check.o sudoku_solver.o -o sudoku_advanced -Wall -Wextra -g

sudoku_advanced :  sudoku_io.o sudoku_check.o advanced.o
	clang sudoku_io.o sudoku_check.o advanced.o -o sudoku_advanced -Wall -Wextra -g

#using the advanced as basic
#sudoku_solver :  sudoku_io.o sudoku_check.o advanced.o
#	clang sudoku_io.o sudoku_check.o advanced.o -o sudoku_solver -Wall -Wextra -g

sudoku_check : sudoku_io.o sudoku_check.o main_check.o
	clang sudoku_io.o sudoku_check.o main_check.o -o sudoku_check -Wall -Wextra -g

#files
sudoku_io.o : sudoku_io.c sud.h
	clang sudoku_io.c -c -o sudoku_io.o -Wall -Wextra -g -O3

sudoku_check.o : sudoku_check.c sud.h
	clang sudoku_check.c -c -o sudoku_check.o -Wall -Wextra -g -O3

main_check.o : main_check.c sud.h
	clang main_check.c -c -o main_check.o -Wall -Wextra -g -O3

sudoku_solver.o : sudoku_solver.c sud.h
	clang sudoku_solver.c -c -o sudoku_solver.o -Wall -Wextra -g -O3

advanced.o : advanced.c sud.h
	clang advanced.c -c -o advanced.o -Wall -Wextra -g -O3
	
