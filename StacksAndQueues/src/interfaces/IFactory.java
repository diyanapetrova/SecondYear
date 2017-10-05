package uk.ac.standrews.cs.cs2001.w08.interfaces;


/**
 * Interface for a factory allowing the other interfaces to be instantiated without knowing the implementation classes.
 */
public interface IFactory {

    /**
     * Creates an instance of {@link IDoubleStack}.
     *
     * @param maxSize the maximum size of the stack
     * @return the double stack
     */
    IDoubleStack makeDoubleStack(int maxSize);


    /**
     * This method creates an instance of {@link IPriorityQueue}.
     *
     * @param maxSize the maximum size of queue
     * @return the priority queue
     */
    IPriorityQueue makePriorityQueue(int maxSize);


}
