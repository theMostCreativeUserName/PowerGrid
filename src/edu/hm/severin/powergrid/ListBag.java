package edu.hm.severin.powergrid;


import edu.hm.cs.rs.powergrid.Bag;


import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Collections;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Iterator;

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
     */
    public ListBag(final Collection<? extends E> collection) {
        this.elements = new ArrayList<>();
        this.elements.addAll(collection);
    }

    /**
     * Bag with vararg element.
     *
     * @param elements elements.
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public ListBag(final E... elements) {
        final List<E> result = new ArrayList<>();
        Arrays.stream(elements).forEach(e -> result.add(e));
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
        result.addAll(getElements());
        return Collections.unmodifiableSet(result);
    }

    /**
     * adds multiples of the same element.
     *
     * @param element element.
     * @param times   number of elements, non negative.
     * @return bag with added elements.
     * @complexity: 3
     */
    @Override
    public Bag<E> add(final E element, final int times) {
        writeAccess();
        final int compare = this.size();
        if (times < 0) {
            throw new IllegalArgumentException();
        }
        final Bag<E> result = new ListBag<>(getElements());

        Stream.iterate(0, count -> count + 1).limit(times).forEach(i -> result.add(element));

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
        writeAccess();
        final int compare = this.size();
        final Bag<E> result = new ListBag<>(getElements());
        that.iterator().forEachRemaining(e -> result.add(e));

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

        long amountOfSameElements2 = 0;
        long amountOfSameElements = 0;

        final long amountOfNull = getElements()
                .stream()
                .filter(Objects::isNull)
                .count();
        if (amountOfNull == 0) {
            amountOfSameElements2 = getElements()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(x -> x.hashCode() == element.hashCode())
                    .count();
        }

        elementNumber += (int) amountOfSameElements2;
        elementNumber += (int) amountOfNull;

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
        thatElement.addAll(that);
        final Bag<E> iterate = new ListBag<>(thatElement);
        iterate.stream().forEach(e -> {
            if (this.contains(e))
                this.remove(e);
            else
                throw new NoSuchElementException();
        });

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
        if (times < 0) {
            throw new IllegalArgumentException();
        }

        Stream.iterate(0, count -> count + 1).limit(times).forEach(i -> this.remove(element));

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
     * @return size, non negative.
     * @complexity 2
     */
    @Override
    public int size() {
        final int size = (int) this.getElements().stream().count();
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
        return this.distinct().stream()
                .mapToInt(element -> (""+ element + count(element)).hashCode())
                .sum();
    }

    /**
     * proves if that is the same as this.
     *
     * @param that object to be checked
     * @return true, if objects are the same
     * false, if not
     * @complexity: 2
     */
    // SpotBugs categorizes this as Dodgy code, another version though, comparable to the AbstractBag.equals()
    // is determined as even worse
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object that) {

        final Set<Object> setForStream = new HashSet<>();
        setForStream.add(that);
        return setForStream
                .stream()
                .filter(x -> x instanceof Bag bag)
                .filter(x -> this.contains((Bag) x))
                .anyMatch(x -> ((Bag) x).contains(this));
    }

    @Override
    public String toString() {
        return "ListBag{" +
                "elements=" + elements +
                ", readOnly=" + readOnly +
                '}';
    }
}
