/* (C) 2020, R. Schiedermeier, rs@cs.hm.edu
 * Private Java 13, Linux i386 4.18.20
 * april: AMD A4-9125 RADEON R3/2300 MHz, 2 Core(s), 7440 MByte RAM
 */
package edu.hm.cs.sol.powergrid;

//import edu.hm.cs.rs.powergrid.AbstractBag;
import edu.hm.cs.rs.powergrid.Bag;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import static org.junit.Assert.*;

import edu.hm.severin.powergrid.ListBag;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/** ABC mit Tests eine Bagklasse.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-04-03
 */
public class BagTest{
    @Rule public Timeout globalTimeout = Timeout.millis(1_000); // max seconds per test

    /**
     * Konkreter Bag-Typ.
     * TODO: Fuegen Sie hier Ihre Typ ein.
     */
    @SuppressWarnings("rawtypes")
    private static final Class<ListBag> bagType = ListBag.class;

    /**
     * Eine neue, leere Tuete.
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings("unchecked")
    private <E> Bag<E> getSut() {
        try {
            return bagType.getDeclaredConstructor()
                    .newInstance();
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
    private <E> Bag<E> getSut(E... elements) {
        try {
            return bagType.getDeclaredConstructor(Object[].class)
                    .newInstance((Object)elements);
        }
        catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test public void ofNone() {
        // arrange
        Bag<Integer> sut = getSut();
        // act
        // assert
        assertEquals(0, sut.size());
        assertTrue(sut.isEmpty());
    }

    @Test public void ofSome() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        // act
        // assert
        assertEquals(5, sut.size());
        assertFalse(sut.isEmpty());
    }

    @Test public void equalsSameOtherOrdering() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> ints = getSut(3, 2, 1, 1, 1);
        // act
        // assert
        assertTrue(sut.equals(sut));
        assertTrue(sut.equals(ints));
        assertTrue(ints.equals(sut));
    }

    @Test public void hashCodeSameOtherOrdering() {
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> other = getSut(1, 1, 1, 3, 2);
        Bag<Integer> another = getSut(1, 1, 1, 3, 238);
        assertTrue(sut.hashCode() == other.hashCode());
        assertFalse(sut.hashCode() == another.hashCode());
    }

    @Test public void iterator() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        final int[] want = {0, 3, 1, 1};
        // act
        int[] have = new int[4];
        for(int number: sut)
            have[number]++;
        // assert
        assertArrayEquals(want, have);
    }

    @Test public void iteratorMultiples() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 2, 2, 1);
        final int[] want = {0, 3, 3};
        // act
        int[] have = new int[3];
        for(int number: sut)
            have[number]++;
        // assert
        assertArrayEquals(want, have);
    }

    @Test public void iteratorSingle() {
        // arrange
        Bag<Integer> sut = getSut(3, 2, 1, 0);
        final int[] want = {1, 1, 1, 1};
        // act
        int[] have = new int[4];
        for(int number: sut)
            have[number]++;
        // assert
        assertArrayEquals(want, have);
    }

    @Test public void iteratorNulls() {
        // arrange
        Bag<String> sut = getSut(null, null, null);
        int want = 3;
        // act
        int have = 0;
        for(String string: sut)
            if(string == null)
                have++;
            else
                fail("unexpected element: " + string);
        // assert
        assertEquals(want, have);
    }

    @Test public void distinct() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Set<Integer> want = Set.of(1, 2, 3);
        // act
        Set<Integer> have = sut.distinct();
        // assert
        assertEquals(want.toString(), have.toString());
    }

    @Test public void add() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> want = getSut(2, 3, 42, 1, 2, 1, 1, 3, 42);
        // act
        sut.add(2);
        sut.add(3);
        sut.add(42);
        sut.add(42);
        // assert
        assertEquals(want, sut);
    }

    @Test public void count() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        // act
        // assert
        assertEquals(3, sut.count(1));
        assertEquals(1, sut.count(3));
        assertEquals(0, sut.count(42));
    }

    @Test public void containsSelf() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        final Bag<Integer> empty = getSut();
        // act
        // assert
        assertTrue(sut.contains(sut));
        assertTrue(empty.contains(empty));
    }

    @Test public void containsEmpty() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        final Bag<Integer> empty = getSut();
        // act
        // assert
        assertFalse(empty.contains(sut));
        assertTrue(sut.contains(empty));
    }

    @Test public void containsSome() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> other = getSut(1, 2, 1, 3);
        // act
        // assert
        assertTrue(sut.contains(other));
        assertFalse(other.contains(sut));
    }

    @Test public void containsDifferent() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> other = getSut(1, 2, 3, 1, 3);
        // act
        // assert
        assertFalse(sut.contains(other));
        assertFalse(other.contains(sut));
    }

    @Test public void removeFound() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        // act
        // assert
        assertTrue(sut.remove(1));
        assertEquals(2, sut.count(1));
    }

    @Test public void removeMissing() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        // act
        // assert
        assertFalse(sut.remove(42));
        assertEquals(5, sut.size());
    }

    @Test public void removeEmpty() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        final Bag<Integer> empty = getSut();
        // act
        // assert
        assertEquals(sut, sut.remove(empty));
        assertEquals(empty, sut.remove(sut));
    }

    @Test public void removeSome() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> some = getSut(1, 2, 1);
        Bag<Integer> rest = getSut(1, 3);
        // act
        // assert
        assertEquals(rest, sut.remove(some));
        assertEquals(getSut(), sut.remove(rest));
    }

    @Test public void removeSomeOthers() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> some = getSut(1, 2, 1);
        Bag<Integer> rest = getSut(1, 3);
        // act
        // assert
        assertEquals(some, sut.remove(rest));
        assertTrue(sut.remove(some).isEmpty());
    }

    @Test public void removeChained() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> some = getSut(1, 2, 1);
        Bag<Integer> rest = getSut(1, 3);
        // act
        // assert
        assertTrue(sut.remove(some).remove(rest).isEmpty());
    }

    @Test(timeout = 1_000, expected = NoSuchElementException.class) public void removeDifferent() {
        // arrange
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        // act
        sut.remove(getSut(1, 2, 1, 1, 4));
    }

    @Test public void nullCountAndRemove() {
        Bag<String> sut = getSut("one", "two", null, "one", null);
        assertEquals(5, sut.size());
        assertEquals(2, sut.count(null));
        sut.remove((String)null);
        assertEquals(4, sut.size());
    }
    @SuppressWarnings("unchecked")
    @Test public void bagOfBags() {
        Bag<Bag<Integer>> sut = getSut(getSut(1, 2),
                getSut(2, 1),
                getSut(1, 1, 1));
        assertEquals(3, sut.size());
        assertEquals(2, sut.count(getSut(1, 2)));
        sut.remove(getSut(2, 1));
        assertEquals(2, sut.size());
    }

    @Test(expected = UnsupportedOperationException.class) public void unmodifiable() {
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> immutable = sut.immutable();
        immutable.add(1);
    }

    @Test public void unmodifiableIsView() {
        Bag<Integer> sut = getSut(1, 2, 1, 1, 3);
        Bag<Integer> immutable = sut.immutable();
        sut.add(1);
        assertEquals(6, immutable.size());
    }

    @Test public void containsNullString() {
        Bag<String> sut = getSut("ha", null);
        assertEquals(2, (long)sut.size());
        assertTrue(sut.contains((String)null));
    }

    @Test public void countJustNulls() {
        Bag<String> sut = getSut(null, null, null);
        assertEquals(3, (long)sut.size());
        assertEquals(3, sut.count(null));
    }

    @Test public void nullIntegers() {
        Bag<Integer> sut = getSut(1, null, 2, null);
        assertEquals(4, (long)sut.size());
        assertEquals(2, sut.count(null));
    }

    @Test public void testremoveNullIntegers() {
        Bag<Integer> sut = getSut(null, 1, null, 2, null);
        sut.remove(null, 2);
        assertEquals(3, (long)sut.size());

        assertEquals(1, sut.count(null));
    }
}
