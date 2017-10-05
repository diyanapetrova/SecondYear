package uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue.LinkedList;

import uk.ac.standrews.cs.cs2001.w08.common.QueueEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.QueueFullException;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IPriorityQueue;

public final class LinkedListPriorityQueue implements IPriorityQueue {
    private ListNode root;
    private ListNode last;
    private int size;
    private int maxSize;

    /**
     * Constructor.
     *
     * @param maxSize of the queue
     */
    public LinkedListPriorityQueue(int maxSize) {
        size = 0;
        this.maxSize = maxSize;
    }

    @Override
    public void enqueue(Comparable element) throws QueueFullException {
        if (size >= maxSize) {
            throw new QueueFullException();
        }

        ListNode toAdd = new ListNode(element);
        //empty queue
        if (root == null) {
            root = toAdd;
            last = root;
        } else {
            ListNode previous = insertPoint(element);
            //"bigger" than root
            if (previous == null) {
                root.setPrevious(toAdd);
                toAdd.setNext(root);
                root = toAdd;
            }
            //the "smallest" added to the end
            else if (previous == last) {
                last.setNext(toAdd);
                toAdd.setPrevious(last);
                last = toAdd;
            }
            //not an edge case
            else {
                ListNode next = previous.getNext();
                toAdd.setNext(next);
                next.setPrevious(toAdd);
                previous.setNext(toAdd);
                toAdd.setPrevious(previous);
            }
        }
        /*//printing how the queue is populated
        System.out.println();
        print();*/
        size++;
    }

    /**
     * Finds the insertion point for a payload, comparing it to every payload in the nodes.
     * @param payLoad the payload to be added
     * @return the node which should preside the node wit the payload
     */
    private ListNode insertPoint(Comparable payLoad) {
        if (payLoad.compareTo(root.getPayLoad()) >= 0) {
            //if the node is bigger than the root
            return null;
        }else if (last == root) {
            //only one element in the queue
            return root;
        }else if(payLoad.compareTo(last.getPayLoad()) <= 0){
            //element less than the tail
            return last;
        }
        return recursion(payLoad, root.getNext());
    }

    /**
     * Recursion looking for node with payload less than the one that is being inserted. Returns the previous node as a
     * starting point for insertion.
     *
     * @param payLoad the element to add
     * @param node the current node, which payload is being compared
     * @return the node after which the element should be added
     */
    private ListNode recursion(Comparable payLoad, ListNode node) {
        if (payLoad.compareTo(node.getPayLoad()) > 0) {
            return node.getPrevious();
        }
        return recursion(payLoad, node.getNext());
    }


    @Override
    public Comparable dequeue() throws QueueEmptyException {
        if (root == null) {
            throw new QueueEmptyException();
        }
        Comparable element = root.getPayLoad();
        //only one element in the list
        if (size == 1) {
            root = null;
        } else {
            ListNode nextRoot = root.getNext();
            nextRoot.setPrevious(null);
            root = nextRoot;
        }
        size--;
        return element;
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
        root = null;
        last = null;
        size = 0;
    }

    //debugging
    private void print() {
        ListNode current = root;
        while (current != null) {
            System.out.print(" -> " + current.getPayLoad().toString());
            current = current.getNext();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedListPriorityQueue)) return false;

        LinkedListPriorityQueue that = (LinkedListPriorityQueue) o;

        if (size != that.size||maxSize!=that.maxSize) return false;

        ListNode thisN = root;
        ListNode thatN = that.root;

        while (thatN!=null&&thisN!=null){
            if(thatN.getPayLoad().compareTo(thisN.getPayLoad())!=0){
                return false;
            }
            thatN = thatN.getNext();
            thisN = thisN.getNext();
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result =31 * maxSize;
        return result;
    }
}
