package edu.hm.severin.powergrid;

import edu.hm.cs.rs.powergrid.Bag;

import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.*;

/**
 * tests for bag.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Severin
 */
public class BagTest<E> {
    /**
     * Maximale Laufzeit jedes  einzelnen Tests.
     * Verhindert Endlosschleifen.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);

    /**
     * Konkreter Bag-Typ.
     * TODO: Fuegen Sie hier Ihre Typ ein.
     */
    @SuppressWarnings("rawtypes")
    private static final Class<ListBag> bagType = ListBag.class;
    /**
     * Eine neue, leere Tuete.
     *
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings("unchecked")
    private <E> Bag<E> getSUT() {
        try {
            return bagType.getDeclaredConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Eine neue Tuete mit den Elementen einer Collection.
     *
     * @param collection Collection, deren Elemente die neue Tuete enthaelt.
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings("unchecked")
    private <E> Bag<E> getSUT(Collection<? super E> collection) {
        try {
            return bagType.getDeclaredConstructor(Collection.class)
                    .newInstance(collection);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Eine neue Tuete mit gegebenen Elementen.
     *
     * @param elements Elemente, die die neue Tuete enthaelt.
     * @return Tuete. Nicht null.
     */
    @SuppressWarnings({"unchecked", "varargs"})
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
    public void addSome() {
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
    public void removeSome() {
        // arrange
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        // act
        sut.remove("2nd");
        sut.remove("4th");
        // assert
        assertEquals(3, sut.size());
    }

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
        assertSame(4, sut.size());
    }

    @Test
    public void immutable1() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        // act
        Bag<String> copy = sut.immutable();
        assertEquals("ListBag{elements=[1st, 2nd, 3rd, 2nd], readOnly=false}",sut.toString());
        assertEquals("ListBag{elements=[1st, 2nd, 3rd, 2nd], readOnly=true}", copy.toString());
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

    @Test(expected = UnsupportedOperationException.class)
    public void immutable4() {
        Bag<String> sut = getSUT("1st", "2nd", "3rd", "2nd");
        Bag<String> copy = sut.immutable();
        copy.add("2nd", 3);
        assertEquals("ListBag{elements=[1st, 2nd, 3rd, 2nd], readOnly=false}",sut.toString());
        assertEquals("ListBag{elements=[1st, 2nd, 3rd, 2nd], readOnly=true}", copy.toString());
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
    @SuppressWarnings("unchecked")
    public void countType1() {
        Set<Integer> that = new HashSet<>();
        Set<Integer> sat = new HashSet<>();
        that.add(1);
        that.add(3);
        that.add(14);
        sat.add(2);
        sat.add(3);
        sat.add(2);

        Bag<Set<Integer>> sut = getSUT(that, sat, sat);
        assertEquals(2, sut.count(sat));

    }

    @Test
    public void equals1() {
        Bag<Integer> sut = getSUT(1, 2, 2, 3);
        assertEquals(sut, sut);
    }

    @Test
    public void equals2() {
        Bag<Integer> sut = getSUT(1, 2, 3, 4);
        Bag<Integer> sat = getSUT(1, 4, 3, 2);
        assertEquals(sut, sat);
    }

    @Test
    public void equals4() {
        Bag<Integer> sut = getSUT(1, 2);
        String sat = "gg";
        assertFalse(sut.equals(sat));
    }

    @Test
    public void addMore1() {
        Bag<Character> sut = getSUT();
        Bag<Character> sat = sut.add('c', 4);
        assertSame(4, sat.size());
    }

    @Test
    public void addMore2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> sat = sut.add('c', 4);
        assertSame(8, sat.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMore3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> sat = sut.add('c', -2);
        assertSame(8, sat.size());
    }

    @Test
    public void addBag1() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> sat = getSUT('c');
        Bag<Character> test = sut.add(sat);
        assertSame(5, test.size());
    }

    @Test
    public void addBag2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = getSUT('d', 'a', ' ');
        Bag<Character> test = sat.add(sut);
        assertSame(9, test.size());
    }

    @Test
    public void addBag3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        boolean added = sut.add('v');
        assertTrue(added);
        assertSame(sut.size(), 5);
    }

    @Test
    public void addBag4() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> sat = getSUT('c');
        Bag<Character> test = sut.add(sat);
        assertSame(5, test.size());
    }

    @Test
    public void addBagSelf() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> test = sut.add(sut);
        assertSame(8, test.size());
    }

    @Test
    public void addFalse() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        boolean added = sut.add('s');
        assertTrue(added);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addIllegalTimes() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        sut.add('s', -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeIllegalTimes() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        sut.remove('s', -1);
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

    @Test(expected = NoSuchElementException.class)
    public void removeBag1() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = getSUT('d', 'u', ' ');
        Bag<Character> test = sut.remove(sat);

        assertSame(4, test.size());
    }

    @Test
    public void removeBag2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = sut.remove(sut);
        assertSame(0, sat.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void removeBag3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        Bag<Character> sat = getSUT('a', 'f', ' ');
        Bag<Character> test = sut.remove(sat);
        assertEquals(test.toString(), sut.toString());
        
    }

    @Test
    public void removeMore1() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'l', 'l');
        Bag<Character> sat = sut.remove('l', 3);
        assertSame(3, sat.size());
    }

    @Test
    public void removeMore2() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'l', 'l');
        Bag<Character> sat = sut.remove('l', 5);
        assertSame(3, sat.size());
    }

    @Test
    public void removeMore3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'l', 'l');
        Bag<Character> sat = sut.remove('a', 5);
        assertSame(6, sat.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeMore4() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'l', 'l');
        Bag<Character> sat = sut.remove('a', -5);
        assertSame(6, sat.size());
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
        assertSame(1, sut.count('w'));
    }

    @Test
    public void count2() {
        Bag<Character> sut = getSUT('w', 'w', 'r', 'l', 'd', 'w');
        assertSame(3, sut.count('w'));
    }

    @Test
    public void count3() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'l', 'd', 'u');
        assertSame(0, sut.count('y'));
    }


    @Test
    public void distinct1() {
        Bag<Character> sut = getSUT('w', 'w', 'r', 'w');
        Set<String> sat = new HashSet<>();
        sat.add("w");
        sat.add("r");
        Set<String> s = Collections.unmodifiableSet(sat);
        assertEquals(s.toString(),sut.distinct().toString());
    }

    @Test
    public void distinct2() {
        Bag<Character> sut = getSUT('w', 'w', 'w', 'w');
        Set<String> sat = new HashSet<>();
        sat.add("w");
        Set<String> s = Collections.unmodifiableSet(sat);
        assertEquals(s.toString(),sut.distinct().toString());
    }


    @Test
    public void distinct3() {
        Bag<Character> sut = getSUT();
        Set<String> sat = new HashSet<>();
        assertEquals(sut.distinct(), sat);
    }
    @Test public void containsSelf(){
        Bag<Integer> sut = getSUT(1,2,3);
        assertTrue(sut.contains(sut));
    }

    // New tests

    @Test
    public void addMore4() {
        Bag<Character> sut = getSUT('w', 'o', 'r', 'd');
        Bag<Character> sat = sut.add('c', 4);
        assertSame(8, sat.size());
    }

    @Test
    public void count4() {
        Bag<Character> sut = getSUT(null, null);
        assertSame(2, sut.count(null));
    }

    @Test
    public void count5() {
        Bag<Character> sut = getSUT(' ', ' ', ' ', ' ');
        assertSame(4, sut.count(' '));
    }

    @Test
    public void count6() {
        Bag<Character> sut = getSUT(' ', 'b', ' ', ' ');
        assertSame(3, sut.count(' '));
    }

    @Test
    public void equals3() {
        Bag<Integer> sut = getSUT(1, 2, 3, 4);
        Bag<Integer> sat = getSUT(1, 4, 3, 2);
        assertTrue(sut.equals(sat));
    }

    @Test
    public void equals5() {
        Bag<Integer> sut = getSUT(1, 2, 3, 4);
        Bag<Integer> sat = getSUT(1, 3, 3, 3);
        assertFalse(sut.equals(sat));
    }

    @Test
    public void equals6() {
        Bag<Integer> sut = getSUT(1, 2, 3, 4);
        Bag<Integer> sat = getSUT(1, 2, 3, 4, 5, 6);
        assertFalse(sut.equals(sat));
    }

    @Test
    public void equals7() {
        Bag<Integer> sut = getSUT(1, 2, 3, 4, 5, 6);
        Bag<Integer> sat = getSUT(1, 2, 3, 4);
        assertFalse(sut.equals(sat));
    }

    // Neue Tests
    @Test
    public void remove5(){
        Bag<Integer> sut = getSUT(1, 2, 3, 4, 5);
        assertTrue(sut.remove(1));
    }

    @Test
    public void remove6(){
        Bag<Integer> sut = getSUT(1, 2, 3, 4, 5);
        assertFalse(sut.remove(6));
    }

    @Test
    public void remove7(){
        Bag<Integer> sut = getSUT(1, 2, 3, 4, 5);
        Bag<Integer> have = sut.remove(5, 0);
        assertSame(sut, have);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void addBagtoBag(){
        Bag<Integer> first = getSUT(1, 2, 3).immutable();
        Bag<Integer> second = getSUT(4, 5, 6);
        Bag<Integer> sut = first.add(second);
    }

    //x.14 Tests
    @Test (expected = UnsupportedOperationException.class)
    public void addNTimesOnlyRead(){
        Bag<Integer> sut = getSUT(1, 2, 3);
        Bag<Integer> copy = sut.immutable();
        copy.add(4, 2);
        assertEquals(sut.toString(), copy.toString());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void removeBagOnlyRead(){
        Bag<Integer> sut = getSUT(1, 2, 3);
        Bag<Integer> copy = sut.immutable();
        Bag<Integer> second = getSUT(3);
        copy.remove(second);
        assertEquals(sut.toString(), copy.toString());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void removeNTimesOnlyRead(){
        Bag<Integer> sut = getSUT(1, 2, 3);
        Bag<Integer> copy = sut.immutable();
        copy.remove(3, 3);
        assertEquals(sut.toString(), copy.toString());
    }

    //x.15
    @Test
    public void hashCodeSame(){
        Bag<Integer> sut = getSUT(1, 2, 3);
        Bag<Integer> have = getSUT(1, 2, 3);
        assertEquals(sut.hashCode(), have.hashCode());
    }

    @Test
    public void hashCodeNotSame(){
        Bag<Integer> sut = getSUT(1, 2, 3);
        Bag<Integer> have = getSUT(4, 5, 6);
        assertNotEquals(sut.hashCode(), have.hashCode());
    }

    @Test
    public void hashCodeOfBagWithNull(){
        Bag<Integer> sut = getSUT(1, null);
        assertEquals(sut.hashCode(), 35);
    }

    @Test
    public void hashCodeOfBagEmpty(){
        Bag<Integer> sut = new ListBag<>();
        assertEquals(sut.hashCode(), 0);
    }

    @Test
    public void hashCodeOfWithValue(){
        Bag<Integer> sut = getSUT(1, 2, 3);
        assertEquals(sut.hashCode(), 1022);
    }

    @Test
    public void hashCodeOfWithValue2(){
        Bag<String> sut = getSUT("Hallo", "Ni hao", "Hello");
        assertEquals(sut.hashCode(), -742459123);
    }



}