package com.github.codestorm.bounceverse.typing.structures;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 *
 * <h1>{@link BoundedTreeSet}</h1>
 *
 * {@link TreeSet} có giới hạn số phần tử chứa tối đa.
 *
 * @param <T> the type of elements maintained by this set
 */
public class BoundedTreeSet<T> extends TreeSet<T> {
    private int maxSize;

    private void adjust() {
        while (maxSize < size()) {
            remove(last());
        }
    }

    public BoundedTreeSet(int maxSize) {
        this.maxSize = maxSize;
    }

    public BoundedTreeSet(int maxSize, Comparator<? super T> comparator) {
        super(comparator);
        this.maxSize = maxSize;
    }

    public BoundedTreeSet(int maxSize, Collection<? extends T> c) {
        super(c);
        this.maxSize = maxSize;
    }

    public BoundedTreeSet(int maxSize, SortedSet<T> s) {
        super(s);
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        adjust();
    }

    @Override
    public boolean add(T t) {
        boolean changed = super.add(t);
        if (changed) {
            adjust();
        }
        return changed;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = super.addAll(c);
        if (changed) {
            adjust();
        }
        return changed;
    }
}
