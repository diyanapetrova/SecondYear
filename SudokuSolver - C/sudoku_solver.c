#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <assert.h>
#include "sud.h"

Status recursion(int x, int y, Sudoku s, int n, Status* current);

Sudoku solution;

Status recursion(int x, int y, Sudoku s, int n, Status* current){// sol should be null at the begining
    int x1 = x, y1 = y;
    bool end = false;
    //get next coordinates
    if(y1==n*n){
        y1 = 0;
        if(x1!=n*n)
            x1++;
        else end = true;
    }else{
        y1++;
    }  

    //the value is set from the input so move on the next cell
    if(getValue(s, x, y)!= 0 && (!end)){
        return recursion(x1, y1, s, n, current);
    }else if(end){
        return *current;
    }

    //check all posibilities for this cell
    for(int i = 1; i<= n*n; i++){
        setValue(s, x, y, i);
        Status this = check_sudoku(s);
        switch(this){
            case COMPLETE:
                if(solution == NULL){
                    solution = copySudoku(s);
                    *current = COMPLETE;
                }else{
                    return MULTIPLE;
                }
                break;

            case INCOMPLETE://should never end up here if this was the last cell, so no check for that
                {//switch is a conditional statement expect exp, but the next line is declaration -> used {} to make it work
                Sudoku copyS = copySudoku(s);
                *current = recursion(x1, y1, copyS, n, current);
                if(current==MULTIPLE){
                    return MULTIPLE;
                }
                break;}

            case INVALID:
                break;//skip value
        }
    }
    cleanSudoku(s);
    return *current; 
}

Status solve(Sudoku s){
    int check = check_sudoku(s);
    if(check!=INCOMPLETE){
        solution = s;
        return check;
    }

    int n = s->n;
    Status holder = -1;
    Status* current = &holder;
    *current = recursion(0, 0, s, n, current);
    return holder;
}


int main(){
    int n;
	scanf("%d", &n);
    int size = n*n*n*n;
    int data [size];

    //getting data
    for(int i = 0; i < size; i++)
            scanf("%d",&data[i]);
    
    Sudoku s = makeSudoku(data, n);
    //printSudoku(s);
    int result = solve(s);
    switch(result){
        case COMPLETE: printSudoku(solution); break;  
        case MULTIPLE: printf("MULTIPLE"); break;
        case INVALID: printf("UNSOLVABLE"); break;
        default: printf("Error");
        }

}

