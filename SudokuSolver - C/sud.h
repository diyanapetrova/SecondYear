typedef struct Sudoku_s{
    int* arr;
    int n;
}*Sudoku;

int getValue(Sudoku s, int x, int y);
Sudoku makeSudoku(int data[], int n);
Sudoku copySudoku(Sudoku s);
void cleanSudoku(Sudoku s);
void printSudoku(Sudoku s);
void setValue(Sudoku s, int x, int y, int value);

typedef enum {COMPLETE, INCOMPLETE, INVALID, MULTIPLE, UNSOLVABLE} Status;

Status check_list(int list[], int size);
void printArr(int arr[],int size);
Status check_sudoku(Sudoku s);