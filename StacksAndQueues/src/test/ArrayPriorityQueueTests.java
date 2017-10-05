package uk.ac.standrews.cs.cs2001.w08.test;

import org.junit.Before;
import org.junit.Test;
import uk.ac.standrews.cs.cs2001.w08.common.AbstractFactoryClient;
import uk.ac.standrews.cs.cs2001.w08.common.QueueEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.QueueFullException;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IPriorityQueue;

import static org.junit.Assert.*;

/**
 * Tests priority queue implementation.
 */
public class ArrayPriorityQueueTests extends AbstractFactoryClient {

    private static final int DEFAULT_MAX_SIZE = 10;
    private IPriorityQueue dummy;
    private IPriorityQueue queue;


    /**
     * Set up for the tests. Initializing the global queue with the DEFAULT_MAX_SIZE.
     */
    @Before
    public void setUp() {
        queue = getFactory().makePriorityQueue(DEFAULT_MAX_SIZE);
    }

    /**
     * Tests that the factory constructs a non-null priority queue.
     */
    @Test
    public void factoryReturnsNonNullPriorityQueueObject() {

        dummy = getFactory().makePriorityQueue(DEFAULT_MAX_SIZE);
        assertTrue("Failure: IFactory.makePriorityQueue returns null, expected non-null IPriorityQueue object",
                queue != null);
    }

    /**
     * Tests how the program behave when given an 0(maxSize). The queue methods isEmpty(), size() and clear() should
     * work without any problems. See the next test for the behavior of queue().
     */
    @Test
    public void zeroMaxSize() {
        dummy = getFactory().makePriorityQueue(0);
        assertTrue(dummy.isEmpty());
        assertEquals(0, dummy.size());
        dummy.clear();
    }

    /**
     * Testing how enqueue() handles a zero-array.
     *
     * @throws QueueFullException if the queue is full, thrown by enqueue()
     */
    @Test(expected = QueueFullException.class)
    public void zeroMaxSizeExceptionEnqueue() throws QueueFullException {
        zeroMaxSize();
        dummy.enqueue(1);
    }

    /**
     * Tests that if a negative maxSize is passed the constructor handles it by taking the absolute value of the argument.
     * Adding elements to the queue until it is full and one more to show that the queue is already full with the thrown
     * exceptions.
     *
     * @throws QueueFullException if the queue is full, thrown by enqueue()
     */
    @Test(expected = QueueFullException.class)
    public void negativeMaxSize() throws QueueFullException {
        dummy = getFactory().makePriorityQueue(-1);
        dummy.enqueue(1);
        assertEquals(1, dummy.size());
        dummy.enqueue(2);
    }

    /**
     * Tests the isEmpty():
     * - an unused queue
     * - an consecutive enqueuing and removing an element
     * Another test case can be seen in the dequeueSimpleExceptionAndSizeAndIsEmpty().
     *
     * @throws QueueFullException  if the queue is full, thrown by enqueue()
     * @throws QueueEmptyException if the queue is empty, thrown by dequeue()
     */
    @Test
    public void isEmpty() throws QueueFullException, QueueEmptyException {
        assertTrue(queue.isEmpty());
        queue.enqueue(1);
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    /**
     * Simple test for adding elements to the queue until it is full. Testing the edge cases of the if statement.
     *
     * @throws QueueFullException if the queue is full, thrown by enqueue()
     */
    @Test
    public void fullQueue() throws QueueFullException {
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            System.out.println(i);
            assertEquals(i, queue.size());
            queue.enqueue(i);
        }
    }

    /**
     * Simple test for adding elements to overpopulate the queue.
     *
     * @throws QueueFullException if the queue is full, thrown by enqueue()
     */
    @Test(expected = QueueFullException.class)
    public void enqueueSimpleException() throws QueueFullException {
        fullQueue();
        queue.enqueue(0);
    }

    /**
     * Simple test for the dequeue(). The order of the entries shouldn't make a difference as it is a priority queue.
     *
     * @throws QueueFullException  if the queue is full, thrown by enqueue()
     * @throws QueueEmptyException if the queue is empty, thrown by dequeue()
     */
    @Test
    public void dequeueSimple() throws QueueFullException, QueueEmptyException {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(5);
        queue.enqueue(4);

        assertEquals(5, queue.dequeue());
        assertEquals(4, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(1, queue.dequeue());

        assertTrue(queue.isEmpty());
    }

    /**
     * Tests if dequeue() throws the exception if the queue is empty.
     *
     * @throws QueueEmptyException if the queue is empty, thrown by dequeue()
     */
    @Test(expected = QueueEmptyException.class)
    public void dequeueSimpleException() throws QueueEmptyException {
        assertTrue(queue.isEmpty());
        queue.dequeue();
    }

    /**
     * Supplemental test to the upper one. Checking that the queue that has been "used"(populated and then emptied) also
     * throws the exception. Also this test proves the correct implementation of size() and that isEmpty() behave
     * properly in case of populating the whole queue and then emptying it.
     *
     * @throws QueueFullException  if the queue is full, thrown by enqueue()
     * @throws QueueEmptyException if the queue is empty, thrown by dequeue()
     */
    @Test(expected = QueueEmptyException.class)
    public void dequeueSimpleExceptionAndSizeAndIsEmpty() throws QueueFullException, QueueEmptyException {
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            assertEquals(i, queue.size());
            queue.enqueue(i);
        }
        for (int i = DEFAULT_MAX_SIZE; i > 0; i--) {
            assertEquals(i, queue.size());
            queue.dequeue();
        }
        assertTrue(queue.isEmpty());
        queue.dequeue();
    }

    /**
     * Tests size():
     * - an unused queue
     * - a consecutive enqueue and dequeue of an element
     * - adding a bunch then removing a single item
     *
     * @throws QueueFullException  if the queue is full, thrown by enqueue()
     * @throws QueueEmptyException if the queue is empty, thrown by dequeue()
     */
    @Test
    public void size() throws QueueFullException, QueueEmptyException {
        assertEquals(0, queue.size());
        queue.enqueue(0);
        assertEquals(1, queue.size());
        queue.dequeue();
        assertEquals(0, queue.size());
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            queue.enqueue(i);
        }
        queue.dequeue();
        assertEquals(DEFAULT_MAX_SIZE - 1, queue.size());
    }

    /**
     * Tests clear().
     * - clearing an empty queue
     * - clearing a full queue
     *
     * @throws QueueFullException if the queue is full, thrown by enqueue()
     */
    @Test
    public void clear() throws QueueFullException {
        queue.clear();
        assertTrue(queue.isEmpty());
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            queue.enqueue(i);
        }
        assertEquals(DEFAULT_MAX_SIZE, queue.size());
        queue.clear();
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests equality of empty queues.
     */
    @Test
    public void equalsEmptyQueues() {
        dummy = getFactory().makePriorityQueue(DEFAULT_MAX_SIZE);
        assertEquals(queue, dummy);
    }

    /**
     * Tests equality of used queues.
     */
    @Test
    public void equalsUsedQueuesSimple() throws QueueFullException {
        dummy = getFactory().makePriorityQueue(DEFAULT_MAX_SIZE);

        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            dummy.enqueue(i);
            assertNotEquals(queue, dummy);
            queue.enqueue(i);
            assertEquals(queue, dummy);
        }
    }

    /**
     * Tests if two queues ,with the same initial elements, are equal after a different "priority" element is added to
     * each one and then removed.
     */
    @Test
    public void equalsUsedQueues() throws QueueFullException, QueueEmptyException {
        dummy = getFactory().makePriorityQueue(DEFAULT_MAX_SIZE);
        for (int i = 0; i < DEFAULT_MAX_SIZE - 1; i++) {
            dummy.enqueue(i);
            queue.enqueue(i);
        }
        assertEquals(queue, dummy);

        int priorityEl = 120;
        dummy.enqueue(priorityEl);
        queue.enqueue(priorityEl + 1);
        assertNotEquals(queue, dummy);

        queue.dequeue();
        dummy.dequeue();
        assertEquals(queue, dummy);
    }

    /**
     * Tests if the queue can work with duplicates. Different implementations handle duplicates differently see report.
     *
     * @throws QueueFullException  if the queue is full, thrown by enqueue()
     * @throws QueueEmptyException if the queue is empty, thrown by dequeue()
     */
    @Test
    public void duplicates() throws QueueFullException, QueueEmptyException {
        for (int i = 0; i < 5; i++) {
            queue.enqueue(0);
        }

        for (int i = 0; i < 5; i++) {
            assertEquals(0, queue.dequeue());
        }
    }

    /**
     * Testing if the comparing stand with objects - strings.
     *
     * @throws QueueFullException if the queue is full, thrown by enqueue()
     * @throws QueueEmptyException if the queue is empty, thrown by dequeue()
     */

    @Test
    public void testStrings() throws QueueFullException, QueueEmptyException {
        String [] words = {"alphabetic", "binary", "collections", "dash", "even",
                "field", "grid", "help", "integer", "join", "key", "list", "map", "null", "or", "paste", "queue",
                "round", "stream", "tree", "unit", "void", "word", "x", "y", "zero"};

        for (int i = 0; i < DEFAULT_MAX_SIZE -2; i++) {
            queue.enqueue(words[i]);
        }
        queue.enqueue(words[words.length -1]);
        queue.enqueue(words[0]);
        assertEquals(words[words.length -1], queue.dequeue());

        for (int i = DEFAULT_MAX_SIZE - 3; i >= 0; i--) {
            assertEquals(words[i], queue.dequeue());
        }
    }

    /**
     * Making sure that the supplementary data structures of the tree are properly updated.
     *
     * @throws QueueFullException if the queue is full, thrown by enqueue()
     * @throws QueueEmptyException if the queue is empty, thrown by dequeue()
     */
    @Test
    public void treeTest() throws QueueFullException, QueueEmptyException {
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            queue.enqueue(i);
        }
        for (int i = DEFAULT_MAX_SIZE - 1; i>0; i--) {
            assertEquals(i, queue.dequeue());
        }
        for (int i = 0; i < DEFAULT_MAX_SIZE/2; i++) {
            queue.enqueue(i);
        }
        for (int i = DEFAULT_MAX_SIZE/2 - 1; i>0; i--) {
            assertEquals(i, queue.dequeue());
        }
    }

}