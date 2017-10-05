#include <stdio.h>
#include "sud.h"

int main(){
    int n;
	scanf("%d", &n);
    int size = n*n*n*n;
    int data [size];

    //getting data
    for(int i = 0; i < size; i++)
            scanf("%d",&data[i]);
    
    Sudoku s = makeSudoku(data, n);
    printSudoku(s);
    int result = check_sudoku(s);
    
    switch(result){
        case COMPLETE: printf("COMPLETE"); break;  
        case INCOMPLETE: printf("INCOMPLETE"); break;
        case INVALID: printf("INVALID"); break;
    }
    printf("\n");   
    cleanSudoku(s);
}