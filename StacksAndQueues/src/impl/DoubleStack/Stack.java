package uk.ac.standrews.cs.cs2001.w08.impl.DoubleStack;


import uk.ac.standrews.cs.cs2001.w08.common.StackEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.StackOverflowException;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IStack;

import java.util.Arrays;

/**
 * A ADT of Side-stack on a given array.
 */

public final class Stack implements IStack {
    private Object[] arr;
    private int stackStart;
    private int nextIndex;
    private int step;//the step to the next index; - step would lead to the previous one
    private int size;

    /**
     * Constructor to initialising the stack fields depending on the side ot the array which it should use.
     *
     * @param arr    the array for storing the elements.
     * @param isLeft or right the side to use
     */
    public Stack(Object[] arr, boolean isLeft) {
        if (isLeft) {
            this.stackStart = 0;
            this.step = 1;
        } else {
            this.stackStart = arr.length - 1;
            this.step = -1;
        }

        this.arr = arr;
        this.nextIndex = stackStart;
        this.size = 0;
    }

    @Override
    public void push(Object element) throws StackOverflowException {
        //check for index out of bounds (zeroArray or stack occupying the whole array)
        if (nextIndex < 0 || nextIndex >= arr.length) {
            throw new StackOverflowException();
        }
        if (arr[nextIndex] != null) {
            throw new StackOverflowException();
        }
        arr[nextIndex] = element;
        nextIndex = nextIndex();
        size++;
    }

    @Override
    public Object pop() throws StackEmptyException {
        if (previousIndex() < 0 || previousIndex() >= arr.length) {
            throw new StackEmptyException();
        }
        Object last = arr[previousIndex()];
        arr[previousIndex()] = null;
        nextIndex = previousIndex();
        size--;
        return last;
    }

    @Override
    public Object top() throws StackEmptyException {
        if (previousIndex() < 0 || previousIndex() >= arr.length) {
            throw new StackEmptyException();
        }
        return arr[previousIndex()];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = stackStart; size != 0; size--) {
            arr[i] = null;
            i = i + step;
        }
    }

    /**
     * Hides the step mechanism for clearer code.
     *
     * @return the previous index
     */
    private int previousIndex() {
        return nextIndex - step;
    }

    /**
     * Hides the step mechanism for clearer code.
     *
     * @return the next index
     */
    private int nextIndex() {
        return nextIndex + step;
    }

    public Object[] getArr() {
        return arr;
    }

    public int getSize() {
        return size;
    }

    public boolean isLeft(){
        return step==1;
    }

    /**
     * Reverse an array. Used in the equals() to reverse an array to be able to compare left and right stacks.
     * @param array the array of elements
     */
    private void reverseArray(Object [] array){
        for(int i = 0; i < array.length / 2; i++)
        {
            Object temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stack that = (Stack) o;

        if(this.getSize()!=that.getSize())
            return false;

        Object [] thisElements;
        Object [] thatElements;

        if(this.isLeft()){
            thisElements = Arrays.copyOfRange(this.getArr(), 0, this.getSize());
        }else {
            thisElements = Arrays.copyOfRange(this.getArr(), this.getArr().length - this.getSize(), this.getArr().length );
            reverseArray(thisElements);
        }

        if(that.isLeft()){
            thatElements = Arrays.copyOfRange(that.getArr(), 0, that.getSize());
        }else {
            thatElements = Arrays.copyOfRange(that.getArr(), that.getArr().length -that.getSize(), that.getArr().length);
            reverseArray(thatElements);
        }

        return Arrays.deepEquals(thisElements,thatElements);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getArr());
        result = 31 * result + getSize();
        return result;
    }
}
