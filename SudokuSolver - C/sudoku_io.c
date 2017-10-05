#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "sud.h"

int getValue(Sudoku s, int x, int y){
    return s -> arr[x*(s->n)*(s->n) + y];
}

void setValue(Sudoku s, int x, int y, int value){
    s->arr[x*(s->n)*(s->n) + y] = value;
}

Sudoku makeSudoku(int data[], int n){
    int size = n*n*n*n;
    Sudoku this = malloc(sizeof(struct Sudoku_s));//returns a pointer from the struct last line
    int* p = malloc(sizeof(int)*size);//allocate the memory for the array and then give the poiner to it ot the struct
    this -> arr = memcpy(p, data, sizeof(int)*size);;
    this -> n = n;
    return this;
}

Sudoku copySudoku(Sudoku s){
    return makeSudoku(s->arr, s->n);
}

void cleanSudoku(Sudoku s){
    free(s->arr); 
    free(s);
}

void printSudoku(Sudoku s){
    int n = s->n;
    for(int i = 0; i < n*n; i++){
        for(int j = 0; j< n*n; j++){
            printf("%3d",s->arr[i*n*n + j]);
        }
        printf("\n");
    }
}
