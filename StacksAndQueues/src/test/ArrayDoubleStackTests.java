package uk.ac.standrews.cs.cs2001.w08.test;

import org.junit.Before;
import org.junit.Test;
import uk.ac.standrews.cs.cs2001.w08.common.AbstractFactoryClient;
import uk.ac.standrews.cs.cs2001.w08.common.StackEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.StackOverflowException;
import uk.ac.standrews.cs.cs2001.w08.impl.DoubleStack.Stack;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IDoubleStack;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IStack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests array collection implementation.
 */
public class ArrayDoubleStackTests extends AbstractFactoryClient {

    private static final int DEFAULT_MAX_SIZE = 10;
    private IDoubleStack dummy;
    private IDoubleStack doubleStack;
    private IStack empty;
    private IStack left;
    private IStack right;

    @Before
    public void setUp() {
        doubleStack = getFactory().makeDoubleStack(DEFAULT_MAX_SIZE);
        left = doubleStack.getFirstStack();
        right = doubleStack.getSecondStack();
        empty = new Stack(new Object[DEFAULT_MAX_SIZE], true);
    }

    /**
     * Tests that the factory constructs a non-null double stack.
     */
    @Test
    public void factoryReturnsNonNullDoubleStackObject() {

        dummy = getFactory().makeDoubleStack(DEFAULT_MAX_SIZE);
        assertTrue("Failure: IFactory.makeDoubleStack returns null, expected non-null IDoubleStack object", dummy != null);
    }

    /**
     * Tests initial state of the stacks.
     */
    @Test
    public void getters() {
        dummy = getFactory().makeDoubleStack(DEFAULT_MAX_SIZE);
        IStack left = dummy.getFirstStack();
        IStack right = dummy.getSecondStack();
        assertTrue(left.isEmpty());
        assertTrue(right.isEmpty());
        assertEquals(left, empty);
        assertEquals(left, right);
    }

    /**
     * Fills the whole array with only one stack.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test
    public void oneStackFull() throws StackOverflowException {
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            left.push(i);
        }
        assertEquals(DEFAULT_MAX_SIZE, left.size());
        assertTrue(right.isEmpty());
    }

    /**
     * Tests that when the array is full with the left stack it will overflow when another element is pushed in the left
     * stack.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test(expected = StackOverflowException.class)
    public void overflowLeft() throws StackOverflowException {
        oneStackFull();
        left.push(0);
    }

    /**
     * Tests that when the array is full with the left stack it will overflow when another element is pushed in the right
     * stack.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test(expected = StackOverflowException.class)
    public void overflowRight() throws StackOverflowException {
        oneStackFull();
        right.push(0);
    }

    /**
     * Shows that the two stacks can split the array evenly.(bunch by bunch and single exchange)
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test
    public void middle() throws StackOverflowException, StackEmptyException {
        oneStackFull();
        for (int i = 0; i < DEFAULT_MAX_SIZE / 2; i++) {
            left.pop();
        }
        for (int i = 0; i < DEFAULT_MAX_SIZE/2; i++) {
            right.push(i);
        }
        assertEquals(left, right);
        //single exchange
        left.pop();
        right.push(0);
    }

    /**
     * Two doubleStacks are should be equal when the underlying array have the same elements.
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test
    public void equal() throws StackOverflowException {
        dummy = getFactory().makeDoubleStack(DEFAULT_MAX_SIZE);
        IStack leftD = dummy.getFirstStack();
        IStack rightD = dummy.getSecondStack();
        assertEquals(dummy, doubleStack);

        for (int i = 0; i < DEFAULT_MAX_SIZE / 2; i++) {
            leftD.push(i);
            rightD.push(i);
            left.push(i);
            right.push(i);
        }
        assertEquals(dummy, doubleStack);
    }

    /**
     * Test that the equals() compare the size first.
     */
    @Test
    public void equalsSize() {
        dummy = getFactory().makeDoubleStack(DEFAULT_MAX_SIZE -1);
        assertNotEquals(dummy, doubleStack);
    }
}
