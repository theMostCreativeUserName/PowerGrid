package edu.hm.cs;

import edu.hm.cs.rs.powergrid.Bag;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Smoketest fuer Bag.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-11
 */
public class SmokeBagTest {
    /**
     * Maximale Laufzeit jedes  einzelnen Tests.
     * Verhindert Endlosschleifen.
     */
    @Rule public Timeout globalTimeout = Timeout.seconds(1);

    /**
     * Konkreter Bag-Typ.
     * TODO: Fuegen Sie hier Ihre Typ ein.
     */
    @SuppressWarnings("rawtypes")
    private static final Class<? extends Bag> bagType = edu.hm.severin.powergrid.ListBag.class;

    /**
     * Eine neue, leere Tuete.
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings("unchecked")
    private <E> Bag<E> getSUT() {
        try {
            return bagType.getDeclaredConstructor()
                    .newInstance();
        }
        catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Eine neue Tuete mit den Elementen einer Collection.
     * @param collection Collection, deren Elemente die neue Tuete enthaelt.
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings("unchecked")
    private <E> Bag<E> getSUT(Collection<? super E> collection) {
        try {
            return bagType.getDeclaredConstructor(Collection.class)
                    .newInstance(collection);
        }
        catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Eine neue Tuete mit gegebenen Elementen.
     * @param elements Elemente, die die neue Tuete enthaelt.
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings({"unchecked", "varargs"})
    private <E> Bag<E> getSUT(E... elements) {
        try {
            return bagType.getDeclaredConstructor(Object[].class)
                    .newInstance((Object)elements);
        }
        catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test public void emptySize() {
        // arrange
        Bag<Void> sut = getSUT();
        // act
        // assert
        assertEquals(0, sut.size());
    }

    @Test public void addSome() {
        // arrange
        Bag<Boolean> sut = getSUT();
        // act
        sut.add(true);
        sut.add(false);
        sut.add((Boolean)null);
        sut.add(true);
        //  assert
        assertEquals(4, sut.size());
    }

    @Test public void getSome() {
        // arrange
        final List<Integer> numbers = List.of(1, 2, 2, 42);
        Bag<Integer> sut = getSUT(numbers);
        // act
        List<Integer> readback = new ArrayList<>();
        for(int number: sut)
            readback.add(number);
        // assert
        Collections.sort(readback);
        assertEquals(numbers, readback);
    }

    @Test public void removeSome() {
        // arrange
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        // act
        sut.remove("2nd");
        sut.remove("4th");
        // assert
        assertEquals(3, sut.size());
    }

}