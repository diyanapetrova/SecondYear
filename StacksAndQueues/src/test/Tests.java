package uk.ac.standrews.cs.cs2001.w08.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * This is a JUnit test suite for the ArrayDoubleStack, ArrayPriorityQueue and Stack classes.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayDoubleStackTests.class,
        ArrayPriorityQueueTests.class,
        StackTests.class
})
public class Tests {
}
