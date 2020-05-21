package edu.hm.severin.powergrid;


import edu.hm.cs.rs.powergrid.Bag;

import java.util.*;


/**
 * a Bag with elements of no specific order.
 *
 * @param <E> elements to be bagged
 * @author Severin
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
    @SuppressWarnings("varargs")
    @SafeVarargs
    public ListBag(final E... elements) {
        final E[] vararg = elements;
        final List<E> result = new ArrayList<>();
        for (E arrayPart : vararg) {
            result.add(arrayPart);
        }
        this.elements = result;
    }

    private ListBag(final List<E> elements, final boolean readOnly) {
        this.readOnly = readOnly;
        this.elements = elements;
    }

    private void writeAccess() {
        if (readOnly) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * adds an element to the bag.
     *
     * @param e element to be added
     * @return if element was added
     * @complexity: 1
     */
    @Override
    public boolean add(final E e) {
        writeAccess();
        final int compareSize = this.size();
        getElements().add(e);
        assert compareSize <= this.size();
        return true;
    }

    /**
     * removes an Element from bag.
     *
     * @param o element to be removed.
     * @return if element was removed.
     * @complexity: 1
     */
    public boolean remove(final Object o) {
        writeAccess();
        final int compareSize = this.size();
        this.getElements().remove(o);
        assert this.size() <= compareSize;
        return size() < compareSize;
    }

    /**
     * makes Bag immutable returns this view.
     *
     * @return immutable view
     * @complexity: 1
     */
    @Override
    public Bag<E> immutable() {
        final List<E> elementList = getElements();
        return new ListBag<E>(elementList, true);
    }

    /**
     * sum of different elements.
     *
     * @return the elements; non null.
     * @complexity: 2
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<E> distinct() {
        final Set<E> result = new HashSet<>();
        for (E element : getElements()) {
            final String name = element.toString();
            final boolean add = result.add((E) name);
        }
        final Set<E> immutable = Set.copyOf(result);
        return immutable;
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
        final int compare = this.size();
        if (times <= 0) {
            throw new IllegalArgumentException();
        }
        final Bag<E> result = new ListBag<>(getElements());
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
        final int compare = this.size();
        final Bag<E> result = new ListBag<>(getElements());
        final Iterator<? extends E> thatIterator = that.iterator();
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
     * @complexity: 5
     */
    public int count(final E element) {
        int elementNumber = 0;

        for (E thisElements : getElements()) {
            final String thisString = thisElements + "";
            final String elementString = element + "";
            if (thisString.contains(elementString)) {
                elementNumber++;
            } else if (element != null) {
                if (element.hashCode() == thisElements.hashCode()) {
                    elementNumber++;
                }
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
     * @complexity: 3
     */

    @Override
    public boolean contains(final Bag<E> that) {
        if (this == that) return true;
        final Set<E> thatSet = that.distinct();
        return thatSet.stream().allMatch(element -> this.count(element) >= that.count(element));
    }

    /**
     * removes all elements of another bag.
     *
     * @param that a bag.
     * @return new bag with elements removed
     * or original bag
     * @throws NoSuchElementException, if bag that isn't included in this
     * @complexity: 4
     */
    @Override
    public Bag<E> remove(final Bag<E> that) {
        final int compare = this.size();
        final List<E> thatElement = new ArrayList<>();
        for (E e : that) {
            thatElement.add(e);
        }
        final Bag<E> iterate = new ListBag<>(thatElement);
        for (E element : iterate) {
            if (this.contains(element)) {
                this.remove(element);
            } else {
                throw new NoSuchElementException();
            }
        }
        //this.elements = result;
        assert compare >= this.size();
        return this;

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
        final int compare = getElements().size();
        if (times <= 0) {
            throw new IllegalArgumentException();
        }
        // Bag<E> result = new ListBag<>(getElements());
        for (int count = 0; count < times; count++) {
            this.remove(element);
        }

        assert compare >= this.size();

        return this;
    }

    /**
     * creates iterator for bag elements.
     *
     * @return iterator
     * @complexity: of class = 2, in total: 4
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int count = 0;

            @Override
            public boolean hasNext() {
                final boolean result;
                if (elements.size() > count) {
                    result = true;
                } else {
                    result = false;
                }
                return result;
            }

            @Override
            public E next() {
                return elements.get(count++);
            }
        };
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

    /**
     * redefines hashCode for ListBag.
     *
     * @return int HashCode
     * @complexity: 2
     */
    @Override
    public int hashCode() {
        final Set<E> thisSet = this.distinct();
        final Map<E, Integer> identifier = new HashMap<>();
        for (E element : thisSet) {
            final int elementCount = this.count(element);
            identifier.put(element, elementCount);
        }
        return identifier.hashCode();
    }

    /**
     * proves if that is the same as this.
     *
     * @param that object to be checked
     * @return true, if objects are the same
     * false, if not
     * @complexity: 2
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object that) {
        boolean result = false;
        if (that instanceof Bag bag) {
            if (this.contains(bag))
                if (((Bag) that).contains(this)) result = true;
        }
        return result;
    }
}
