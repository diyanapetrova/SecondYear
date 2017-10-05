package uk.ac.standrews.cs.cs2001.w08.impl.DoubleStack;

import uk.ac.standrews.cs.cs2001.w08.interfaces.IDoubleStack;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IStack;

import java.util.Arrays;

public final class DoubleStack implements IDoubleStack {
    private Object[] arr;
    private IStack leftStack;
    private IStack rightStack;

    public DoubleStack(int size) {
        arr = new Object[size];
        leftStack = new Stack(arr, true);
        rightStack = new Stack(arr, false);
    }

    public Object[] getArr() {
        return arr;
    }

    @Override
    public IStack getFirstStack() {
        return leftStack;
    }

    @Override
    public IStack getSecondStack() {
        return rightStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubleStack that = (DoubleStack) o;

        if(this.getArr().length!=that.getArr().length)
            return false;

        return Arrays.deepEquals(this.getArr(), that.getArr());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(arr);
        result = 31 * result + leftStack.hashCode();
        result = 31 * result + rightStack.hashCode();
        return result;
    }
}
