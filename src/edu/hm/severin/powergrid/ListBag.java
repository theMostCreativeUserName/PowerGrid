package edu.hm.severin.powergrid;


import edu.hm.cs.rs.powergrid.Bag;

import java.util.*;


/**
 * a Bag with elements of no specific order.
 * @author Severin
 * @param <E> elements to be bagged
 * @complexity: 40
 */
public class ListBag<E> extends AbstractCollection<E> implements Bag<E> {

    /**
     * lists elements in Bag.
     */
    private List<E> elements;
    /**
     * sets access to read only.
     */
    private boolean readOnly;

    /**
     * a new empty Bag.
     */
    public ListBag() {
        this.elements = new ArrayList<>();
        this.readOnly = false;
    }

    /**
     * copy cTor for Bag.
     *
     * @param collection to be copied
     * @complexity: 2
     */
    public ListBag(final Collection<? extends E> collection) {
        this.elements = new ArrayList<>();
        for (E e : collection) {
            this.add(e);
        }
    }

    /**
     * Bag with vararg element.
     *
     * @param elements elements.
     */
    public ListBag(final E... elements) {
        List<E> vararg = List.of(elements);
        this.elements = vararg;
    }

    private ListBag(final List<E> elements, final boolean readOnly) {
        // ToDo: dont copy element
        this.readOnly = readOnly;
        this.elements = elements;
    }

    private void writeAccess() {
        if (readOnly) {
            throw new IllegalStateException();
        }
    }

    /**
     * adds an element to the bag.
     *
     * @param e element to be added
     * @return if element was added
     * @complexity: 1
     */
    public boolean add(final E e) {
        writeAccess();
        int compareSize = this.size();
        getElements().add(e);
        assert this.size() >= compareSize;
        return true;
    }

    /**
     * removes an Element from bag.
     *
     * @param o element to be removed.
     * @return if element was removed.
     * @complexity: 3
     */
    public boolean remove(final Object o) {
        writeAccess();
        int compareSize = this.size();
        List<E> list = new ArrayList<>();
        int elementCount = 0;
        for (E element : getElements()) {
            // so element is only once removed
            if (!o.equals(element) || elementCount != 0) {
                list.add(element);
            } else {
                elementCount++;
            }
        }
        int size = getElements().size();
        elements = list;
        assert this.size() <= compareSize;
        return size >= getElements().size();
    }
    /**
     * makes Bag immutable returns this view.
     *
     * @return immutable view
     */
    @Override
    public Bag<E> immutable() {
        List<E> elementList = getElements();
        return new ListBag<E>(elementList, true);
    }

    /**
     * sum of different elements.
     *
     * @return the elements; non null.
     * @complexity: 2
     */
    @Override
    public Set<E> distinct() {
        Set<E> result = new HashSet<>();
        for (E element : getElements()) {

            String name = element.toString();
            int times = count(element);
            E toAdd = (E) (name + " : " + times);
            boolean add = result.add(toAdd);
        }
        return result;
    }

    /**
     * adds multiples of the same element.
     *
     * @param element element.
     * @param times   number of elements, non negative.
     * @return bag with added elemenets.
     * @complexity: 3
     */
    @Override
    public Bag<E> add(final E element, final int times) {
        int compare = this.size();
        if (times < 0) {
            throw new IllegalArgumentException();
        }
        Bag<E> result = new ListBag<>(getElements());
        for (int count = 0; count < times; count++) {
            result.add(element);
        }
        assert compare <= this.size();
        return result;
    }

    /**
     * adds elements of another bag in bag.
     *
     * @param that a bag, non null.
     * @return bag with elements of this and that
     * @complexity: 2
     */
    @Override
    public Bag<E> add(final Bag<? extends E> that) {
        int compare = this.size();
        Bag<E> result = new ListBag<>(getElements());
        Iterator<? extends E> thatIterator = that.iterator();
        while (thatIterator.hasNext()) {
            result.add(thatIterator.next());
            that.iterator().next();
        }
        assert compare <= this.size();
        return result;
    }

    /**
     * number of elements in Bag.
     *
     * @param element searched for element
     * @return number of elements
     * @complexity: 3
     */
    @Override
    public int count(final E element) {
        int elementNumber = 0;
        for (E thisElements : getElements()) {
            if (thisElements.equals(element)) {
                elementNumber++;
            }
        }
        assert elementNumber >= 0;
        return elementNumber;
    }

    /**
     * proves if this contains another bag that.
     *
     * @param that a bag
     * @return false, if that is not contained
     * @complexity: 5
     */
    @Override
    public boolean contains(final Bag<E> that) {
        int doubletNumber = 0;
        boolean contained = false;
        for (E thatElement : that) {
            for (E thisElement : this) {
                if (thisElement.equals(thatElement)) {
                    contained = true;
                }
            }
            if (contained) {
                doubletNumber++;
                contained = false;
            }
        }
        assert doubletNumber >= 0;
        return doubletNumber >= that.size();
    }

    /**
     * removes all elements of another bag.
     *
     * @param that a bag.
     * @return new bag with elements removed
     * or original bag
     * @throws NoSuchElementException, if bag that isn't included in this
     * @complexity: 2
     */
    @Override
    public Bag<E> remove(final Bag<E> that) throws NoSuchElementException {
        int compare = this.size();
        Bag<E> result = new ListBag<>(getElements());
        Iterator thatIterator = that.iterator();
        while (thatIterator.hasNext()) {
            result.remove(thatIterator.next());
            that.iterator().next();
        }
        assert compare <= this.size();
        return result;

    }

    /**
     * removes multiples of same element.
     *
     * @param element an element.
     * @param times   number of elements to delete.non negative.
     * @return bag with elements removes.
     * @throws IllegalArgumentException, if times <0.
     * @complexity: 3
     */
    @Override
    public Bag<E> remove(final Object element, final int times) {
        int compare = this.size();
        if (times < 0) {
            throw new IllegalArgumentException();
        }
        Bag<E> result = new ListBag<>(getElements());
        for (int count = 0; count < times; count++) {
            result.remove(element);
        }
        assert compare <= this.size();

        return result;
    }

    /**
     * creates iterator for bag elements.
     *
     * @return iterator
     * @complexity: 4
     */
    @Override
    public Iterator<E> iterator() {
        return new BagIterator<E>(this.getElements());
    }

    /**
     * creates Iterator for Bag.elements.
     *
     * @param <E>
     * @complexity: 3
     */
     private final static class BagIterator<E extends Object> implements Iterator<E> {

        /**
         * global counter of Iterator.
         */
        // set this way to be able to access element at index 0
        private int count = -1;
        final private List<E> elements;

        private BagIterator(List<E> elements) {
            this.elements = elements;
        }

        @Override
        public boolean hasNext() {
            boolean result;
            // prevent outOfBounds
            if (elements.size() > count + 1) {
                result = true;
            } else {
                result = false;
            }
            return result;
        }

        @Override
        public E next() {
            count++;
            return (E) elements.get(count);
        }
    }

    /**
     * number of elements of the Bag.
     *
     * @return size, non negativ.
     * @complexity 2
     */
    @Override
    public int size() {
        int size = 0;
        for (E element : getElements()) {
            size++;
        }
        assert size >= 0;
        return size;
    }

    private List<E> getElements() {
        return elements;
    }
}
