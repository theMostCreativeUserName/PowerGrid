package edu.hm.cs.rs.powergrid;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Eine Tuete, deren Elemente keine bestimmte Reihenfolge haben.
 * null und Duplikate sind erlaubt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-02-04
 */
public interface Bag<E> extends Collection<E> {
    /**
     * Eine unveraenderliche Sicht auf diese Tuete.
     * Diese Methode kopiert keine Elemente.
     * @return Tuete, die keine Aenderungen erlaubt.
     */
    Bag<E> immutable();

    @Override int size();

    @Override Iterator<E> iterator();

    /**
     * Die Menge der unterschiedlichen Elemente.
     * @return Die unterschiedlichen Elemente. Nicht null. Eventuell leer.
     */
    Set<E> distinct();

    @Override boolean add(E element);

    /**
     * Fuegt ein Element mehrmals ein.
     * @param element Element.
     * @param times   Anzahl Exemplare. Nicht negativ.
     * @return Diese Tuete, eventuell mit mehr Elementen als vorher.
     */
    Bag<E> add(E element, int times);

    /**
     * Fuegt alle Elemente der anderen Tuete in diese ein.
     * @param that Eine Tuete. Nicht null.
     * @return Diese Tuete, eventell mit mehr Elementen als vorher.
     */
    Bag<E> add(Bag<? extends E> that);

    /**
     * Anzahl Exemplare eines Elementes.
     * @param element Gesuchtes Element.
     * @return Anzahl Exemplare. Nicht negativ.
     */
    int count(E element);

    /**
     * Test, ob alle Elemente einer anderen Tute auch in dieser vorkommen.
     * @param that Eine Tuete.
     * @return true genau dann, wenn diese Tuete von jedem Element in that
     * gleich viel oder mehr Exemplare enthaelt.
     */
    boolean contains(Bag<E> that);

    @Override boolean equals(Object o);

    @Override int hashCode();

    /**
     * Entfernt alle Elemente einer anderen Tuete aus dieser Tuete.
     * @param that Eine Tuete.
     * @return Diese Tuete, eventuell mit weniger Elementen als vorher.
     * @throws NoSuchElementException wenn diese Tuete nicht genug Elemente enthaelt.
     */
    Bag<E> remove(Bag<E> that) throws NoSuchElementException;

    @Override boolean remove(Object element);

    /**
     * Entfernt mehrere Exemplare eines bestimmten Elementes aus dieser Tuete.
     * Wenn es nicht genug Exemplare gibt, entfernt die Methode alle.
     * @param element Ein Element.
     * @param times   Anzahl Exemplare, die die Methode hoechstens loescht.
     *                Nicht negativ.
     * @return Diese Tuete, eventuell mit weniger Elementen als vorher.
     */
    Bag<E> remove(Object element, int times);
}
