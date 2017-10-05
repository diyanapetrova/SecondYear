package uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue;

import uk.ac.standrews.cs.cs2001.w08.common.QueueEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.QueueFullException;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IPriorityQueue;

import java.util.Arrays;

/**
 * A ADT of priority queue using a heap, implemented by an array.
 */

public final class ArrayHeapPriorityQueue implements IPriorityQueue {
    private Comparable[] arr;
    private int index;

    /**
     * Constructor, initialising the array for storing the elements with the integer arguments. Get the absolute value
     * to prevent exception.
     *
     * @param maxSize the size of the array
     */
    public ArrayHeapPriorityQueue(int maxSize) {
        maxSize = Math.abs(maxSize);
        this.arr = new Comparable[maxSize];
        this.index = 0;
    }

    public Comparable[] getArr() {
        return arr;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void enqueue(Comparable element) throws QueueFullException {
        if (index > arr.length - 1) {
            throw new QueueFullException();
        }
        System.out.println(Arrays.toString(arr));
        arr[index] = element;
        index++;


        heap((index -2)/2, true);
        System.out.println(Arrays.toString(arr));



    }

    @Override
    public Comparable dequeue() throws QueueEmptyException {
        if (isEmpty()) {
            throw new QueueEmptyException();
        }
        Comparable highest = arr[0];

        arr[0] = arr[index - 1];
        arr[index - 1] = null;
        index--;

        heap(0, false);


        return highest;
    }

    @Override
    public int size() {
        return index;
    }

    @Override
    public boolean isEmpty() {
        return index == 0;
    }

    @Override
    public void clear() {
        arr = new Comparable[arr.length];
        index = 0;
    }

    /**
     * Orders the subtree of the heap. If any of the children is "bigger" than the parent, they are swapped to keep the
     * heap property.
     *
     * @param rootIndex the parent's index
     */
    private void heap(int rootIndex) {
        // TODO: 30/10/2016 can also do max(left, right) then compare the max to the root and then swap if needed
        //Comparable biggerChild =
        int left = rootIndex * 2 + 1;
        //check if the left child exist
        if (left < index && arr[left] != null) {
            if (arr[left].compareTo(arr[rootIndex]) > 0) {
                swap(rootIndex, left);
            }
        }

        int right = (rootIndex + 1) * 2;
        //check if the right child exist
        if (right < index && arr[right] != null) {
            if (arr[right].compareTo(arr[rootIndex]) > 0) {
                swap(rootIndex, right);
            }
        }
    }

    private void heap(int rootIndex, boolean up) {
        int left = rootIndex * 2 + 1;
        if (left<size()) {
            if (arr[left].compareTo(arr[rootIndex]) > 0) {
                swap(rootIndex, left);
                if (!up) {
                    heap(left, false);
                }
            }
            int right = (rootIndex + 1) * 2;
            if (right<size() && arr[right].compareTo(arr[rootIndex]) > 0) {
                swap(rootIndex, right);
                if (!up) {
                    heap(right, false);
                }
            }
        }
        int parent = (rootIndex-1)/2;
        if (up && parent>0) {
            heap(parent, true);
        }
    }

    /**
     * Swapping two elements in the array.
     *
     * @param i the index of the first element
     * @param j the index of the second element
     */
    private void swap(int i, int j) {
        Comparable temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayHeapPriorityQueue that = (ArrayHeapPriorityQueue) o;

        return this.getIndex() == that.getIndex() && Arrays.deepEquals(this.getArr(), that.getArr());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(arr);
        result = 31 * result + index;
        return result;
    }
}
