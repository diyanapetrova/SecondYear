package uk.ac.standrews.cs.cs2001.w08.interfaces;

import uk.ac.standrews.cs.cs2001.w08.common.QueueEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.QueueFullException;

/**
 * Simple priority queue interface.
 */
public interface IPriorityQueue {

    /**
     * Adds an element to the queue.
     *
     * @param element the element to be queued
     * @throws QueueFullException if there is no room in the queue for the new element
     */
    void enqueue(Comparable element) throws QueueFullException;

    /**
     * Removes the largest element.
     *
     * @return the element removed
     * @throws QueueEmptyException if the queue is empty
     */
    Comparable dequeue() throws QueueEmptyException;

    /**
     * Returns the number of elements in the queue.
     *
     * @return the number of elements in the queue
     */
    int size();

    /**
     * Checks whether the queue is empty.
     *
     * @return true if the queue is empty
     */
    boolean isEmpty();

    /**
     * Removes all elements from the queue.
     */
    void clear();
}
