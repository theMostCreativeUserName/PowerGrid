package edu.hm.cs.rs.powergrid.datastore;

import static org.junit.Assert.assertEquals;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-02-19
 *
public class BagTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    Bag<Integer> sut = new TeaBag<Integer>();

    @Test public void addSome() {
        // act
        sut.add(1);
        sut.add(2);
        sut.add(42);
        sut.add(1);
        //  assert
        assertEquals(4, sut.size());  // 4
    }

    @Test public void getSome() {
        // arrange
        sut.add(1);
        sut.add(2);
        sut.add(42);
        sut.add(1);
        List<Integer> read = new ArrayList<>();
        // act
        for(int number: sut)
            read.add(number);
        // assert
        Collections.sort(read);
        assertEquals(List.of(1, 1, 2, 42), read);
    }

    @Test public void removeSome() {
        // arrange
        sut.add(1);
        sut.add(2);
        sut.add(42);
        sut.add(1);
        // act
        sut.remove(2);
        sut.remove(238);
        // assert
        assertEquals(3, sut.size());
    }
}
 */
