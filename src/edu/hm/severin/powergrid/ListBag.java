package edu.hm.severin.powergrid;

import java.util.Collection;

import edu.hm.cs.rs.powergrid.Bag;

import java.util.*;

/**
 * a Bag with elements of no specific order.
 *
 * @param <E> elements to be bagged
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
     * copy cTor for Bag
     *
     * @param collection to be copied
     */
    public ListBag(Collection<? extends E> collection) {
        this.elements = new ArrayList<>();
        for (E e : collection) {
            this.add(e);
        }
    }

    /**
     * Bag with vararg element
     *
     * @param elements
     */
    public ListBag(E... elements) {
        List<E> vararg = List.of(elements);
        this.elements = vararg;
    }

    private ListBag(List<E> elements, boolean readOnly) {
        // ToDo: dont copy element
        this.readOnly = readOnly;
        this.elements = elements;
    }

    private boolean listAdd(E e) {
        return getElements().add(e);
    }

    private boolean listRemove(Object e) {
        List<E> list = new ArrayList<>();
        int elementCount = 0;
        for (E element : getElements()) {
            // so element is only once removed
            if (!e.equals(element) || elementCount != 0)
                list.add(element);
            else elementCount++;
        }
        int size = getElements().size();
        elements = (List<E>) list;

        return size >= getElements().size();
    }

    public boolean add(E e) {
        boolean added;
        if (!readOnly) {
            listAdd(e);
            added = true;
        } else added = false;
        return added;
    }

    @Override
    public boolean remove(Object o) {
        boolean removed;
        if (!readOnly) {
            listRemove(o);
            removed = true;
        }else removed = false;
        return removed;
    }

    /**
     * makes Bag immutable returns this view.
     *
     * @return immutable view
     */
    @Override
    public Bag<E> immutable() {
       List<E> elementList =  getElements();
        return new ListBag<E>(elementList, true);
    }

    /**
     * sum of different elements.
     *
     * @return the elements; non null.
     */
    @Override
    public Set<E> distinct() {
        Set<E> result = new HashSet<>();
        for (E element : getElements()) {
            String name = element.toString();
            int times = count(element);
            result.add((E) (name + " : " + times));
        }
        return result;
    }

    /**
     * adds multiples of the same element.
     *
     * @param element element.
     * @param times   number of elements, non negative.
     * @return bag with added elemenets.
     */
    @Override
    public Bag<E> add(E element, int times) {
        if (times < 0) throw new IllegalArgumentException();
        Bag<E> result = new ListBag<>(getElements());
        for (int count = 0; count < times; count++) {
            result.add(element);
        }
        return result;
    }

    /**
     * adds elements of another bag in bag.
     *
     * @param that a bag, non null.
     * @return bag with elements of this and that
     */
    @Override
    public Bag<E> add(Bag<? extends E> that) {
        Bag<E> result = new ListBag<>(getElements());
        Iterator thatIterator = that.iterator();
        while (thatIterator.hasNext()) {
            result.add((E) thatIterator.next());
            that.iterator().next();
        }
        return result;
    }

    /**
     * number of elements in Bag
     *
     * @param element searched for element
     * @return number of elements
     */
    @Override
    public int count(E element) {
        int elementNumber = 0;
        for (E thisElements : getElements()) {
            if (thisElements.equals(element)) elementNumber++;
        }
        return elementNumber;
    }

    /**
     * proves if this contains another bag that.
     *
     * @param that a bag
     * @return false, if that is not contained
     */
    @Override
    public boolean contains(Bag<E> that) {
        int doubletNumber = 0;
        boolean contained = false;
        for (E thatElement : that) {
            for (E thisElement : this) {
                if (thisElement.equals(thatElement)) contained = true;
            }
            if (contained) {
                doubletNumber++;
                contained = false;
            }
        }
        return doubletNumber >= that.size();
    }

    /**
     * removes all elements of another bag.
     *
     * @param that a bag.
     * @return new bag with elements removed
     * or original bag
     * @throws NoSuchElementException, if bag that isn't included in this
     */
    @Override
    public Bag<E> remove(Bag<E> that) throws NoSuchElementException {
        Bag<E> result = new ListBag<>(getElements());
        Iterator thatIterator = that.iterator();
            while (thatIterator.hasNext()) {
                result.remove((E) thatIterator.next());
                that.iterator().next();
            }
            return result;

    }

    /**
     * removes multiples of same element.
     *
     * @param element an element.
     * @param times   number of elements to delete.non negative.
     * @return bag with elements removes.
     * @throws IllegalArgumentException, if times <0.
     */
    @Override
    public Bag<E> remove(Object element, int times) {
        if (times < 0) throw new IllegalArgumentException();
        Bag<E> result = new ListBag<>(getElements());
        for (int count = 0; count < times; count++) {
            result.remove(element);
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new BagIterator<E>();
    }

    /**
     * creates Iterator for Bag.elements.
     *
     * @param <E>
     */
    class BagIterator<E> implements Iterator<E> {
        /**
         * global counter of Iterator
         */
        private int count = -1;

        @Override
        public boolean hasNext() {
            boolean result;
            if (elements.size() > count + 1)
                result = true;
            else result = false;

            return result;
        }

        @Override
        public E next() {
            count++;
            return (E) getElements().get(count);
        }
    }

    /**
     * number of elements of the Bag.
     *
     * @return size, non negativ.
     */
    @Override
    public int size() {
        int size = 0;
        for (E element : getElements()) {
            size++;
        }
        return size;
    }

    private List<E> getElements() {
        return elements;
    }


    @Override
    public String toString() {
        return "ListBag{" +
                "elements=" + elements +
                '}';
    }
}
