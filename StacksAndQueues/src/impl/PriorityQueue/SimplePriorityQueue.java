package uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue;

import uk.ac.standrews.cs.cs2001.w08.common.QueueEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.QueueFullException;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IPriorityQueue;

import java.util.Arrays;

/**
 * Simple priority queue that:
 * - can have duplicates; dequeue the first max element;
 * - don't do any "cleaning" i dequeue() and clear(), only changes the index
 */

public final class SimplePriorityQueue implements IPriorityQueue {
    private Comparable[] arr;
    private int index;

    /**
     * Constructor, initialising the array for storing the elements with the integer arguments. Get the absolute value
     * to prevent exception.
     *
     * @param maxSize the size of the array
     */
    public SimplePriorityQueue(int maxSize) {
        maxSize = Math.abs(maxSize);
        this.arr = new Comparable[maxSize];
        this.index = 0;
    }

    @Override
    public void enqueue(Comparable element) throws QueueFullException {
        if (index > arr.length - 1) {
            throw new QueueFullException();
        }
        arr[index] = element;
        index++;
    }

    @Override
    public Comparable dequeue() throws QueueEmptyException {
        if (index - 1 < 0) {
            throw new QueueEmptyException();
        }

        int maxIndex = 0;
        for (int i = 1; i < size(); i++) {
            if (arr[i].compareTo(arr[maxIndex]) > 0) {
                maxIndex = i;
            }
        }

        Comparable max = arr[maxIndex];
        //assign the place of the current max to the last added element
        arr[maxIndex] = arr[index - 1];
        index--;
        return max;
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
        index = 0;
    }

    // two queues are equal only if elements were queue and dequeue in the same order
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePriorityQueue that = (SimplePriorityQueue) o;
        return index == that.index && Arrays.deepEquals(arr, that.arr);

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(arr);
        result = 31 * result + index;
        return result;
    }
}
