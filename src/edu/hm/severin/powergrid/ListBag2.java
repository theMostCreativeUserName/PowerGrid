package edu.hm.severin.powergrid;

import edu.hm.cs.rs.powergrid.AbstractBag;
import edu.hm.cs.rs.powergrid.Bag;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ListBag2 implements Bag {

    @Override
    public Bag immutable() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public Set distinct() {
        return null;
    }

    @Override
    public boolean add(Object element) {
        return false;
    }

    @Override
    public Bag add(Object element, int times) {
        return null;
    }

    @Override
    public Bag add(Bag that) {
        return null;
    }

    @Override
    public int count(Object element) {
        return 0;
    }

    @Override
    public boolean contains(Bag that) {
        return false;
    }

    @Override
    public Bag remove(Bag that) throws NoSuchElementException {
        return null;
    }

    @Override
    public boolean remove(Object element) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public Bag remove(Object element, int times) {
        return null;
    }
}
