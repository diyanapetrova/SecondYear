package uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue.TreeHeap;

import uk.ac.standrews.cs.cs2001.w08.common.QueueEmptyException;
import uk.ac.standrews.cs.cs2001.w08.common.QueueFullException;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IPriorityQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

/**
 * A ADT of priority queue using a heap, implemented by a tree.
 */

public final class TreeHeapPriorityQueue implements IPriorityQueue {
    private TreeNode root;
    private LinkedList<TreeNode> nodesToPopulate;
    private java.util.Stack<TreeNode> fullNodes;
    private int size;
    private int maxSize;

    /**
     * Constructor initialising the two complimentary data structures on which the implementation depend and assign the
     * max size;
     *
     * @param maxSize the max sie of the queue
     */
    public TreeHeapPriorityQueue(int maxSize) {
        nodesToPopulate = new LinkedList<>();
        fullNodes = new Stack<>();
        size = 0;
        this.maxSize = maxSize;
    }

    @Override
    public void enqueue(Comparable element) throws QueueFullException {
        if (size >= maxSize) {
            throw new QueueFullException();
        }

        TreeNode toAdd;
        //if tree empty
        if (root == null) {
            toAdd = new TreeNode(element, null);
            root = toAdd;
        } else {
            //get the next node that need population
            TreeNode toPopulate = nodesToPopulate.peek();
            toAdd = new TreeNode(element, toPopulate);

            if (toPopulate.getLeft() == null) {
                toPopulate.setLeft(toAdd);
            } else {
                toPopulate.setRight(toAdd);
                fullNodes.push(nodesToPopulate.poll());
            }
            //order heap
            heap(toPopulate, true);
        }
        size++;
        nodesToPopulate.add(toAdd);
    }

    @Override
    public Comparable dequeue() throws QueueEmptyException {
        if (root == null) {
            throw new QueueEmptyException();
        }

        //if only one element in the queue return root and clear
        if (size == 1) {
            Comparable payLoad = root.getPayload();
            clear();
            return payLoad;
        }

        Comparable highest = root.getPayload();
        TreeNode last = nodesToPopulate.pollLast();
        TreeNode lastParent = last.getParent();
        swapPayload(root, last);

        //need to update the states of the parent node from full to tobePopulated
        if (!fullNodes.isEmpty()) {
            if (fullNodes.peek() == lastParent) {
                nodesToPopulate.push(fullNodes.pop());
            }
        }

        //removing the last node
        if (lastParent.getLeft() == last) {
            lastParent.setLeft(null);
        } else {//if it is not the left child -> can be sure it is the right one
            lastParent.setRight(null);
        }

        //order heap
        heap(root, false);
        size--;
        return highest;
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
        nodesToPopulate = new LinkedList<>();
        fullNodes = new Stack<>();
        size = 0;
    }

    /**
     * Getting all the elements in the tree in array for the equals().
     *
     * @return an arrayList of the node's payload
     */
    public ArrayList<Comparable> getElements() {
        ArrayList<Comparable> elements = new ArrayList<>(size);
        postOrderTraverseAdding(elements, root);
        Collections.sort(elements);
        return elements;
    }

    /**
     * Post-order traversal of the tree, adding the payload of each node to a list.
     *
     * @param elements the arrayList to add the elements
     * @param node     the current node to traverse
     */
    private void postOrderTraverseAdding(ArrayList<Comparable> elements, TreeNode node) {
        if (node.getLeft() != null) {
            postOrderTraverseAdding(elements, node.getLeft());
        }
        if (node.getRight() != null) {
            postOrderTraverseAdding(elements, node.getRight());
        }
        elements.add(node.getPayload());
    }

    /**
     * Heaps the tree. Depending on the direction checks each tree on the way if it has the max-heap property and swap
     * elements where needed to ensure order.
     *
     * @param current the root of the subtree to work on
     * @param up      boolean direction
     */
    private void heap(TreeNode current, boolean up) {
        if (current.getLeft() != null) {
            if (current.getLeft().getPayload().compareTo(current.getPayload()) > 0) {
                swapPayload(current, current.getLeft());
                if (!up) {
                    heap(current.getLeft(), false);
                }
            }
            if (current.getRight() != null && current.getRight().getPayload().compareTo(current.getPayload()) > 0) {
                swapPayload(current, current.getRight());
                if (!up) {
                    heap(current.getLeft(), false);
                }
            }
        }
        if (up && current.getParent() != null) {
            heap(current.getParent(), true);
        }
    }

    /**
     * Swapping the payload of two nodes.
     *
     * @param one     the first node
     * @param another the second node
     */
    private void swapPayload(TreeNode one, TreeNode another) {
        Comparable temp = one.getPayload();
        one.setPayload(another.getPayload());
        another.setPayload(temp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeHeapPriorityQueue that = (TreeHeapPriorityQueue) o;

        if (size != that.size) return false;
        if (maxSize != that.maxSize) return false;
        //empty trees
        if (root == null && that.root == null) return true;
        //trees with different sizes
        if (root == null || !root.equals(that.root)) return false;
        //compare the elements
        ArrayList<Comparable> elements = getElements();
        ArrayList<Comparable> thatElements = that.getElements();
        return elements != null ? elements.equals(thatElements) : thatElements == null;
    }

    @Override
    public int hashCode() {
        return maxSize * 31;
    }
}
