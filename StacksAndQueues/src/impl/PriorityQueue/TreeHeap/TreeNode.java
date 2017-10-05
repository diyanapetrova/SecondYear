package uk.ac.standrews.cs.cs2001.w08.impl.PriorityQueue.TreeHeap;

final class TreeNode {
    private Comparable payload;
    private TreeNode left;
    private TreeNode right;
    private TreeNode parent;

    TreeNode(Comparable payload, TreeNode parent) {
        this.payload = payload;
        this.parent = parent;
    }

    TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    Comparable getPayload() {
        return payload;
    }

    void setPayload(Comparable payload) {
        this.payload = payload;
    }

    TreeNode getLeft() {
        return left;
    }

    void setLeft(TreeNode left) {
        this.left = left;
    }

    TreeNode getRight() {
        return right;
    }

    void setRight(TreeNode right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode treeNode = (TreeNode) o;

        return getPayload().equals(treeNode.getPayload());

    }

    @Override
    public int hashCode() {
        return getPayload().hashCode();
    }
}
