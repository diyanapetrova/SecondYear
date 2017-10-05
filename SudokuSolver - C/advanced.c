#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <assert.h>
#include "sud.h"
Status recursion(int x, int y, Sudoku s, int n);
bool putMissing(Sudoku s, int x, int y);

Sudoku solution;

int tech1(Sudoku s){
    int size = s->n*s->n;
    int count = 0;
    for(int i = 0; i < size; i++){
        for(int j = 0; j< size; j++){
            if(getValue(s, i, j)==0){
                if(putMissing(s, i, j)){
                    count++;//the number of values that were placed
                }
            }
        }
    }
    return count;
}

bool putMissing(Sudoku s, int x, int y){

    int n = s->n;
    int size = n*n;
    bool possibleValues [size];
    memset(possibleValues, true, sizeof(possibleValues));

    //traverse row + col
    for(int i = 0; i < size; i++){
        int rowEl = getValue(s, x, i);
        int colEl = getValue(s, i, y);
        if(rowEl!=0)
            possibleValues[rowEl-1] = false;
        if(colEl!=0)
            possibleValues[colEl-1] = false;
    }

    //traverse box
    for(int r = (x/n)*n; r < ((x/n)*n + n); r++){//x/n->the row of boxes
        for(int c = (y/n)*n; c < (n + (y/n)*n);c++){//y/n -> the column of boxes
            int boxEl = getValue(s, r, c);
            if(boxEl!=0)
                possibleValues[boxEl-1] = false;
        } 
    }

    bool found = false;
    int missing = 0;
    for(int i = 0; i<size; i++){
        if(possibleValues[i]){
            if(!found){
                missing = i + 1;
                found = true;
            }else{
                missing = 0;
                break;
            }
        }
    }
    //put single value if there is any
    if(found&&missing!=0){
        setValue(s, x, y, missing);
        return true;
    }

    return false;
}


Status recursion(int x, int y, Sudoku s, int n){// sol should be null at the begining
    //apply technique until it makes changes
    int count = 1;
    while(count>0){
        count = tech1(s);
    }

    //check if completed?
    if(check_sudoku(s)==COMPLETE){
        if(solution == NULL){
            solution = copySudoku(s);
            return COMPLETE;//skip the rest of recursion
        }else{
            return MULTIPLE;
        }
    }

    //get next empty cell
    while(getValue(s, x, y)!=0){
        if(y==n*n){
            y = 0;
            if(x!=n*n)
                x++;
        }else{
            y++;
            }
    }

    //check all posibilities for that cell
    for(int i = 1; i<= n*n; i++){
        setValue(s, x, y, i);
        Status this = check_sudoku(s);
        switch(this){
            case COMPLETE:
                if(solution == NULL){
                    solution = copySudoku(s);
                }else{
                    return MULTIPLE;
                }
                break;

            case INCOMPLETE://should never end up here if this was the last cell, so no check for that
                {//switch is a conditional statement expect exp, but the next line is declaration -> used {} to make it work
                Sudoku copyS = copySudoku(s);
                Status current = recursion(x, y, copyS, n);
                if(current==MULTIPLE){
                    return MULTIPLE;
                }
                //cleanSudoku(copyS);
                break;}

            case INVALID:
                break;//skip value
        }
    }
    cleanSudoku(s);
    return COMPLETE; 
}

Status solve(Sudoku s){
    //check if it needs to be solved?
    int check = check_sudoku(s);
    if(check!=INCOMPLETE){
        solution = s;
        return check;
    }

    int n = s->n;
    Status holder = recursion(0, 0, s, n);
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
    int result = solve(s);
    switch(result){
        case COMPLETE: printSudoku(solution); break;  
        case MULTIPLE: printf("MULTIPLE\n"); break;
        case INVALID: printf("UNSOLVABLE\n"); break;
        default: printf("Error");
        }
}