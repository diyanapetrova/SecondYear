#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include "sud.h"

Status check_list(int* list, int size){
    bool checker [size];                        //flag array to detect duplicates
    memset(checker, false, sizeof(checker));    //set all values to false as not found yet
    bool incomplete = false;
    for(int i = 0; i < size; i++){
        int current = list[i];
        if(current==0){
            incomplete = true;
        }else {
            if(checker[current -1])             //the value correcsponds to the index - 1
            return INVALID;
        checker[current-1] = true;
        }
    }
    return incomplete ? INCOMPLETE : COMPLETE; 
}

void printArr(int* arr,int size){
    for(int i = 0; i< size; i++){
        printf("%d ", arr[i]);
    }
    printf("\n");
}

Status check_sudoku(Sudoku s){
    bool incomplete = false;
    int n = s->n;
    //rows and columns
    for(int x = 0; x < n*n; x++){
        //int row[n*n];
        int column[n*n];
        for(int y = 0; y < n*n; y++){
            //row[y] = getValue(s, x, y);
            column[y] = getValue(s, y, x);
        }
        Status row_s = check_list((s->arr+(x*n*n)), n*n);   //pass the pointer in the arr in the sudoku
        Status col_s = check_list(column, n*n);
        if(row_s==INVALID||col_s==INVALID)
            return INVALID;
        if(row_s==INCOMPLETE||col_s==INCOMPLETE)
            incomplete = true;
    }

    //boxes
    for(int rIndex = 0; rIndex < n*n; rIndex+= n){
        for(int cIndex = 0; cIndex < n*n; cIndex+= n){
            int box[n*n];
            int i = 0;//index for the box array
            for(int r = rIndex; r < rIndex + n; r++){
                for(int c = cIndex; c < cIndex + n; c++){
                    box[i] = getValue(s, r, c);
                    i++;
                }
            }
            Status box_s = check_list(box, n*n);
                if(box_s==INVALID)
                    return box_s;
        }
    }

    return incomplete ? INCOMPLETE : COMPLETE;
}

