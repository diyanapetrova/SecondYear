package uk.ac.standrews.cs.cs2001.w08.test;

import org.junit.Before;
import org.junit.Test;
import uk.ac.standrews.cs.cs2001.w08.common.StackEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.StackOverflowException;
import uk.ac.standrews.cs.cs2001.w08.impl.DoubleStack.Stack;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Tests array collection implementation.
 */
public class StackTests {

    private static final int DEFAULT_MAX_SIZE = 5;
    private static final boolean LEFT = true;
    private Object[] arr;
    private Stack stack;
    private Stack dummy;

    @Before
    public void setUp() {
        arr = new Object[DEFAULT_MAX_SIZE];
        stack = new Stack(arr, LEFT);
    }

    /**
     * Set the stack to a "right" one.
     */
    public void setRight() {
        stack = new Stack(new Object[DEFAULT_MAX_SIZE], !LEFT);
    }

    /**
     * Set the dummy to a stack with a zero-array.
     *
     * @param side left or right
     */
    public void setZeroArray(boolean side) {
        Comparable[] zeroArr = new Comparable[0];
        dummy = new Stack(zeroArr, side);
    }

    /**
     * Tests how the program behave when given an zero-array. The methods isEmpty(), size() and clear() should
     * work without any problems. Testing both right and left stacks.
     */
    @Test
    public void stackConstructorZeroArray() {
        setZeroArray(LEFT);
        assertTrue(dummy.isEmpty());
        assertEquals(0, dummy.size());
        dummy.clear();
        setZeroArray(!LEFT);
        assertTrue(dummy.isEmpty());
        assertEquals(0, dummy.size());
        dummy.clear();
    }

    /**
     * Testing how push() handles a zero-array. (Left stack)
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test(expected = StackOverflowException.class)
    public void stackConstructorZeroArrayPushExceptionLeft() throws StackOverflowException {
        setZeroArray(LEFT);
        dummy.push(1);
    }

    /**
     * Testing how push() handles a zero-array. (Right stack)
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test(expected = StackOverflowException.class)
    public void stackConstructorZeroArrayPushExceptionRight() throws StackOverflowException {
        setZeroArray(!LEFT);
        dummy.push(1);
    }

    //push

    /**
     * Tests if push() throws an exception when overflowing.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test
    public void pushMax() throws StackOverflowException {
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            stack.push(i);
        }
        assertEquals(5, stack.size());
    }

    /**
     * Tests if push() throws an exception when trying to add more elements then the stack.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test(expected = StackOverflowException.class)
    public void pushException() throws StackOverflowException {
        pushMax();
        assertEquals(DEFAULT_MAX_SIZE, stack.size());
        stack.push(0);
    }

    //pop

    /**
     * Tests if pop() can completely empty a stack. Checking the bound of the if statement.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test
    public void popMax() throws StackOverflowException, StackEmptyException {
        pushMax();
        assertEquals(DEFAULT_MAX_SIZE, stack.size());
        for (int i = DEFAULT_MAX_SIZE - 1; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }
        assertEquals(0, stack.size());
    }

    /**
     * Tests if pop() throws an exception when trying to pop() from an empty stack.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test(expected = StackEmptyException.class)
    public void popException() throws StackOverflowException, StackEmptyException {
        popMax();
        stack.pop();
    }

    //top

    /**
     * Simple test for top(). Checking that it doesn't pop an element.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test
    public void topSimple() throws StackOverflowException, StackEmptyException {
        stack.push(0);
        assertEquals(1, stack.size());
        assertEquals(0, stack.top());
        assertEquals(1, stack.size());
    }

    /**
     * Simple test for top().
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test(expected = StackEmptyException.class)
    public void topException() throws StackOverflowException, StackEmptyException {
        stack.top();
    }

    /**
     * Tests size():
     * - an unused stack
     * - a consecutive push and pop of an element
     * - adding a bunch then removing a single item
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test
    public void size() throws StackOverflowException, StackEmptyException {
        assertEquals(0, stack.size());
        stack.push(0);
        assertEquals(1, stack.size());
        stack.pop();
        assertEquals(0, stack.size());
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            stack.push(i);
        }
        stack.pop();
        assertEquals(DEFAULT_MAX_SIZE - 1, stack.size());
    }

    /**
     * Tests clear().
     * - clearing an empty stack
     * - clearing a full stack
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test
    public void clear() throws StackOverflowException {
        stack.clear();
        assertTrue(stack.isEmpty());
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            stack.push(i);
        }
        assertEquals(DEFAULT_MAX_SIZE, stack.size());
        stack.clear();
        assertTrue(stack.isEmpty());
    }

    //right

    /**
     * Tests the implementation with "right" Stack as well.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test
    public void rightNoExceptions() throws StackOverflowException, StackEmptyException {
        setRight();
        pushMax();

        setRight();
        popMax();

        setRight();
        topSimple();

        setRight();
        clear();

        setRight();
        size();
    }

    /**
     * Tests if push() throws an exception when trying to add more elements then the stack.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     */
    @Test(expected = StackOverflowException.class)
    public void rightPushException() throws StackOverflowException {
        setRight();
        pushException();
    }

    /**
     * Tests if pop() throws an exception when trying to pop() from an empty stack.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test(expected = StackEmptyException.class)
    public void rightPopException() throws StackEmptyException, StackOverflowException {
        setRight();
        popException();
    }

    /**
     * Simple test for top().
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test(expected = StackEmptyException.class)
    public void rightTopException() throws StackOverflowException, StackEmptyException {
        setRight();
        topException();
    }

    /**
     * Test equality:
     * - empty stacks(both left)
     * - full stacks(left and right)
     * It looks only at the elements and the order and discard everything else.
     *
     * @throws StackOverflowException if the stack is full, thrown by push()
     * @throws StackEmptyException    if the stack is empty, thrown by pop() or top()
     */
    @Test
    public void equals() throws StackOverflowException, StackEmptyException {
        Object[] copy = Arrays.copyOf(arr, arr.length);
        dummy = new Stack(copy, LEFT);
        assertEquals(stack, dummy);
        dummy = new Stack(copy, !LEFT);
        assertEquals(stack, dummy);

        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            dummy.push(i);
            stack.push(i);
        }
        assertEquals(stack, dummy);
        stack.pop();
        assertNotEquals(stack, dummy);
    }
}
