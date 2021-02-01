package org.pecker.core.support.tally;

public class UnsafeCountBO {
    private int count = 0;

    public int addAndGet(){
        return ++count;
    }

    public int getAndAdd(){
        return count--;
    }

    public int decrementAndGet(){
        return --count;
    }

    public int getAndDecrement(){
        return count--;
    }

    public int get(){
        return count;
    }
}
