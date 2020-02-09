import DataStore.Board;
import DataStore.City;
import DataStore.EditionGermany;
import DataStore.Factory;
//import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Objects;
//import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Smoketest.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-14
 */
public class NeutralFactoryTest {
   // @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final Factory factory = Factory.newFactory();

    @Test public void newCity() {
        // arrange
        City sut = factory.newCity("Entenhausen", 1);
        // act
        // assert
        assertEquals("Entenhausen", sut.getName());
        assertEquals(1, sut.getArea());
    }

    @Test
    public void newBoard() {
        // arrange
        Board sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        assertFalse(sut.getCities().isEmpty());
    }
}