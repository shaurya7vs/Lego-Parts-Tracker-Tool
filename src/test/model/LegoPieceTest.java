package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LegoPieceTest {
    private LegoPiece testLegoPiece;

    @BeforeEach
    void setUp() {
        testLegoPiece = new LegoPiece("Brick", "Red", "2x4", 10);
    }

    @Test
    void testConstructor() {
        assertEquals("Brick", testLegoPiece.getPartType());
        assertEquals("Red", testLegoPiece.getColor());
        assertEquals("2x4", testLegoPiece.getDimensions());
        assertEquals(10, testLegoPiece.getQuantity());
    }

    @Test
    void testSetQuantityValid() {
        testLegoPiece.setQuantity(5);
        assertEquals(5, testLegoPiece.getQuantity());
    }

    @Test
    void testAddQuantity() {
        testLegoPiece.addQuantity(9);
        assertEquals(19, testLegoPiece.getQuantity());

        testLegoPiece.addQuantity(7);
        assertEquals(26, testLegoPiece.getQuantity());
    }

    @Test
    void testRemoveQuantity() {
        testLegoPiece.addQuantity(10);
        testLegoPiece.removeQuantity(0);
        assertEquals(20, testLegoPiece.getQuantity());

        testLegoPiece.removeQuantity(1);
        assertEquals(19, testLegoPiece.getQuantity());

        testLegoPiece.removeQuantity(20);
        assertEquals(19, testLegoPiece.getQuantity());
    }
}