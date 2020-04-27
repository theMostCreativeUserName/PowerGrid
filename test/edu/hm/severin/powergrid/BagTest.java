package edu.hm.severin.powergrid;

import edu.hm.cs.rs.powergrid.Bag;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.*;

/**
 *tests for bag.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Severin
 */
public class BagTest<E> {
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
    private static final Class<? extends Bag> bagType = ListBag.class;

    /**
     * Eine neue, leere Tuete.
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings({"varargs", "unchecked"})
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
     *
     * @param elements Elemente, die die neue Tuete enthaelt.
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings("unchecked")
    private <E> Bag<E> getSUT(E... elements) {
        try {
            return bagType.getDeclaredConstructor(Object[].class)
                    .newInstance((Object) elements);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    //------------------- Tests -------------------------------------------------------------
    @Test
    public void cTor1() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        assertFalse(sut.isEmpty());
    }

    @Test
    public void cTor2() {
        Bag<String> sut = getSUT();
        assertTrue(sut.isEmpty());
    }

    @Test
    public void cTor3() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        Bag<String> sat = getSUT(sut);
        assertEquals(sut.toString(), sat.toString());
    }

    @Test
    public void cTor4() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        assertTrue(sut.size() == 4);
    }

    @Test
    public void immutable1() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        // act
        Bag<String> copy = sut.immutable();
        assertEquals(sut.toString(), copy.toString());
    }
    @Test(expected = UnsupportedOperationException.class)
    public void immutable2() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        Bag<String> copy = sut.immutable();
        copy.add("5th");
        assertEquals(sut.toString(), copy.toString());
    }
    @Test(expected = UnsupportedOperationException.class)
    public void immutable3() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        Bag<String> copy = sut.immutable();
        copy.remove("2nd");
        assertEquals(sut.toString(), copy.toString());
    }
    @Test
    public void immutable4() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        Bag<String> copy = sut.immutable();
        copy.add("2nd",3);
        assertEquals(sut.toString(), copy.toString());
    }

    @Test
    public void emptySize() {
        Bag<Void> sut = getSUT();
        assertEquals(0, sut.size());
    }

    @Test
    public void size1() {
        Bag<String> sut = getSUT();
        assertEquals(0, sut.size());
    }

    @Test
    public void size2() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd");
        assertEquals(3, sut.size());
    }

    @Test
    public void addOne() {
        // arrange
        Bag<Boolean> sut = getSUT();
        // act
        sut.add(true);
        sut.add(false);
        sut.add((Boolean) null);
        sut.add(true);
        //  assert
        assertEquals(4, sut.size());
    }
    @Test
    public void countType1(){
        Set<Integer> g = new HashSet<>();
        Set<Integer> gf = new HashSet<>();
        g.add(1);
        g.add(3);
        g.add(14);
        gf.add(2);
        gf.add(3);
        gf.add(2);

        Bag<Set<Integer>> sut = getSUT(g,gf, gf);
        assertEquals(2, sut.count((Set<Integer>) gf));

    }

    @Test
    public void equals1() {
        Bag<Integer> sut = getSUT(1,2,2,3);
        assertTrue(sut.equals(sut));
    }
    @Test
    public void equals2() {
        Bag<Integer> sut = getSUT(1,2,3,4);
        Bag<Integer> sat = getSUT(1,4,3,2);
        assertTrue(sut.equals(sat));
    }
    @Test
    public void equals3() {
        Bag<Integer> sut = getSUT(1,2);
        Bag<Integer> sat = getSUT(1,4,3,2);
        assertFalse(sut.equals(sat));
    }
    @Test
    public void addMore1() {
        Bag<Character> sut = getSUT();
        Bag<Character> sat = sut.add('c', 4);
        assertTrue(sat.size() == 4);
    }

    @Test
    public void addMore2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> sat = sut.add('c', 4);
        assertTrue(sat.size() == 8);
    }

    @Test
    public void addBag1() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> sat = getSUT('c');
        Bag<Character> x = sut.add(sat);
        assertTrue(x.size() == 5);
    }

    @Test
    public void addBag2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = getSUT('d', 'a', ' ');
        Bag<Character> x = sat.add(sut);
        assertTrue(x.size() == 9);
    }

    @Test
    public void addBag3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> sat = getSUT('c');
        Bag<Character> x = sut.add(sut);
        assertTrue(x.size() == 8);
    }

    @Test
    public void getSome() {
        // arrange
        final List<Integer> numbers = List.of(1, 2, 2, 42);
        Bag<Integer> sut = getSUT(numbers);
        // act
        List<Integer> readback = new ArrayList<>();
        for (int number : sut)
            readback.add(number);
        // assert
        Collections.sort(readback);
        assertEquals(numbers, readback);
    }

    @Test
    public void removeSome1() {
        // arrange
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        // act
        sut.remove("2nd");
        sut.remove("4th");
        // assert
        assertEquals(3, sut.size());
    }

    @Test
    public void removeSome2() {
        // arrange
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        // act
        sut.remove("2nd");
        sut.remove("2nd");
        // assert
        assertEquals(2, sut.size());
    }

    @Test (expected = NoSuchElementException.class)
    public void removeBag1() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = getSUT('d', 'u', ' ');
        Bag<Character> x = sut.remove(sat);

        assertTrue(x.size() == 4);
    }

    @Test
    public void removeBag2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = sut.remove(sut);
        System.out.println(sat.size());
        assertTrue(sat.size() == 0);
    }

    @Test (expected = NoSuchElementException.class)
    public void removeBag3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = getSUT('a', 'f', ' ');
        Bag<Character> x = sut.remove(sat);
        assertEquals(x.toString(), sut.toString());
    }

    @Test
    public void removeMore1() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'l', 'l');
        Bag<Character> sat = sut.remove('l', 3);
        assertTrue(sat.size() == 3);
    }

    @Test
    public void removeMore2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'l', 'l');
        Bag<Character> sat = sut.remove('l', 5);
        assertTrue(sat.size() == 3);
    }

    @Test
    public void removeMore3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'l', 'l');
        Bag<Character> sat = sut.remove('a', 5);
        assertTrue(sat.size() == 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeMore4() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'l', 'l');
        Bag<Character> sat = sut.remove('a', -5);
        assertTrue(sat.size() == 6);
    }

    @Test
    public void contains1() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = getSUT('d', 'u', 'r');

        assertTrue(sut.contains(sat));
    }

    @Test
    public void contains2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = getSUT('a', 'f', ' ');
        assertFalse(sut.contains(sat));
    }

    @Test
    public void contains3() {
        Bag<Character> sut = getSUT('w', 'o', 'r');
        Bag<Character> sat = getSUT('a', 'f', ' ');
        assertFalse(sut.contains(sat));
    }

    @Test
    public void contains4() {
        Bag<Character> sut = getSUT('a', 'f', 'r');
        Bag<Character> sat = getSUT('a', 'f', ' ');
        assertFalse(sut.contains(sat));
    }

    @Test
    public void count1() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        assertTrue(sut.count('w') == 1);
    }

    @Test
    public void count2() {
        Bag<Character> sut = getSUT('w', 'w', 'r', 'l', 'd', 'w');
        assertTrue(sut.count('w') == 3);
    }

    @Test
    public void count3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        assertTrue(sut.count('y') == 0);
    }
    @Test
    public void distinct1() {
        Bag<Character> sut = getSUT('w', 'w', 'r', 'w');
        Set<String> sat = new HashSet<>();
        sat.add("w");
        sat.add("r");
        assertEquals(sut.distinct(),sat);
    }
    @Test
    public void distinct2() {
        Bag<Character> sut = getSUT('w', 'w', 'w', 'w');
        Set<String> sat = new HashSet<>();
        sat.add("w");
        assertEquals(sut.distinct(),sat);
    }
    @Test
    public void distinct3() {
        Bag<Character> sut = getSUT();
        Set<String> sat = new HashSet<>();
        assertEquals(sut.distinct(),sat);
    }
}