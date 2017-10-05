package uk.ac.standrews.cs.cs2001.w08.impl;

import uk.ac.standrews.cs.cs2001.w08.impl.DoubleStack.DoubleStack;
import uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue.ArrayHeapPriorityQueue;
import uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue.LinkedList.LinkedListPriorityQueue;
import uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue.SimplePriorityQueue;
import uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue.TreeHeap.TreeHeapPriorityQueue;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IDoubleStack;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IFactory;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IPriorityQueue;

/**
 * This class implements a singleton factory.
 */
public final class Factory implements IFactory {

    private static IFactory factoryInstance = null;

    private Factory() {

    }

    /**
     * Method which returns an instance of the singleton Factory class.
     *
     * @return the instance of the Factory
     */
    public static IFactory getInstance() {
        if (factoryInstance == null) {
            factoryInstance = new Factory();
        }
        return factoryInstance;
    }

    @Override
    public IDoubleStack makeDoubleStack(int maxSize) {
        return new DoubleStack(maxSize);
    }

    @Override
    public IPriorityQueue makePriorityQueue(int maxSize) {
        return new LinkedListPriorityQueue(maxSize);
    }

}
