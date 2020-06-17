package edu.hm.severin.powergrid;


import edu.hm.cs.rs.powergrid.Bag;


import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
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
// PMD, says to much methods, but after the old implementation it is okay
public class ListBag<E> extends AbstractCollection<E> implements Bag<E> {

    /**
     * lists elements in Bag.
     */
    private final List<E> elements;
    /**
     * sets access to read only.
     */
    private final boolean readOnly;

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
        this.readOnly = false;
    }

    /**
     * Bag with vararg element.
     *
     * @param elements elements.
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public ListBag(final E... elements) {
        this.readOnly = false;
        this.elements = new ArrayList<>(Arrays.asList(elements));
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
     * @param element element to be added
     * @return if element was added
     */
    @Override
    public boolean add(final E element) {
        writeAccess();
        final int compareSize = this.size();
        this.getElements().add(element);
        assert compareSize <= this.size();
        return true;
    }

    /**
     * removes an Element from bag.
     *
     * @param object element to be removed.
     * @return if element was removed.
     */
    public boolean remove(final Object object) {
        writeAccess();
        final int compareSize = this.size();
        this.getElements().remove(object);
        assert this.size() <= compareSize;
        return size() < compareSize;
    }

    /**
     * makes Bag immutable returns this view.
     *
     * @return immutable view
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
     */
    @Override
    public Bag<E> add(final E element, final int times) {
        if (times < 0) {
            throw new IllegalArgumentException();
        }
        final int compare = this.size();

        IntStream.range(0, times).sequential().forEach(iterator -> this.add(element));

        assert compare <= this.size();
        return this;
    }

    /**
     * adds elements of another bag in bag.
     *
     * @param that a bag, non null.
     * @return bag with elements of this and that
     */
    @Override
    public Bag<E> add(final Bag<? extends E> that) {
        final int compare = this.size();
        final Bag<E> otherBag = new ListBag<>(that);
        this.addAll(otherBag);

        assert compare <= this.size();
        return this;
    }

    /**
     * number of elements in Bag.
     *
     * @param element searched for element
     * @return number of elements
     */
    public int count(final E element) {
        int elementNumber = 0;

        long amountOfSameElements2 = 0;

        final long amountOfNull = getElements()
                .stream()
                .filter(Objects::isNull)
                .count();
        if (amountOfNull == 0) {
            amountOfSameElements2 = getElements()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(currentElement -> currentElement.hashCode() == element.hashCode())
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
     * @throws NoSuchElementException if bag that isn't included in this
     */
    @Override
    public Bag<E> remove(final Bag<E> that) {
        final int compare = this.size();

        final Bag<E> iterate = new ListBag<>(that);
        iterate.forEach(iterator -> {
            if (this.contains(iterator))
                this.remove(iterator);
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
     * @throws IllegalArgumentException if times is smaller than 0.
     */
    @Override
    public Bag<E> remove(final Object element, final int times) {
        if (times < 0) {
            throw new IllegalArgumentException();
        }
        final int compare = getElements().size();

        IntStream.range(0, times).sequential().forEach(iterator -> this.remove(element));

        assert compare >= this.size();

        return this;
    }

    /**
     * creates iterator for bag elements.
     *
     * @return iterator
     */
    @Override
    public Iterator<E> iterator() {
        //Here are three PMD errors: 2x AccessorMethodGeneration, 1x don't return from nested block ; cant be fixed
        return new Iterator<E>() {
            /**
             * count of iterator.
             */
            private int count;

            @Override
            public boolean hasNext() {
                return elements.size() > count;
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
     */
    @Override public int hashCode() {
        return distinct().stream()
                .mapToInt(element -> Objects.hash(element, count(element)))
                .reduce(0, (hashElement, hashCount) -> hashElement ^ hashCount);
    }

    /**
     * proves if that is the same as this.
     *
     * @param that object to be checked
     * @return true, if objects are the same
     * false, if not
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
                // the instanceOf phrase can#t be read by PMD
                // and it consequently throws an PMDException
                .filter(otherObject -> otherObject instanceof Bag bag)
                .filter(otherObject -> this.contains((Bag) otherObject))
                .anyMatch(otherObject -> ((Bag) otherObject).contains(this));
    }

    @Override
    public String toString() {
        return "ListBag{" +
                "elements=" + elements +
                ", readOnly=" + readOnly +
                '}';
    }
}
