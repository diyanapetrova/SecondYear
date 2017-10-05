package uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue.LinkedList;

public final class ListNode {
    private Comparable payLoad;
    private ListNode next;
    private ListNode previous;

    public ListNode(Comparable payLoad) {
        this.payLoad = payLoad;
    }

    public Comparable getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(Comparable payLoad) {
        this.payLoad = payLoad;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public ListNode getPrevious() {
        return previous;
    }

    public void setPrevious(ListNode previous) {
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListNode listNode = (ListNode) o;

        return getPayLoad().equals(listNode.getPayLoad());

    }

    @Override
    public int hashCode() {
        return getPayLoad().hashCode();
    }
}
