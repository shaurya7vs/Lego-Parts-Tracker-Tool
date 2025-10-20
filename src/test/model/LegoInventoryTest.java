package model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class LegoInventoryTest {
    private LegoInventory inventory;
    private LegoPiece piece1;
    private LegoPiece piece2;

    @BeforeEach
    void setUp() {
        inventory = new LegoInventory("Shaurya's Collection");
        piece1 = new LegoPiece("Brick", "Red", "2x4", 10);
        piece2 = new LegoPiece("Plate", "Blue", "1x2", 5);
    }

    @Test
    void testConstructor() {
        assertEquals("Shaurya's Collection", inventory.getCollectionName());
        assertEquals(0, inventory.getTotalPieces());
        assertTrue(inventory.getPieces().isEmpty());
    }

    @Test
    void testAddPiece() {
        inventory.addPiece(piece1);
        inventory.addPiece(piece2);
        List<LegoPiece> pieces = inventory.getPieces();
        assertEquals(2, pieces.size());
        assertEquals(piece1, pieces.get(0));
        assertEquals(piece2, pieces.get(1));
    }

    @Test
    void testGetTotalPieces() {
        inventory.addPiece(piece1);
        inventory.addPiece(piece2);
        assertEquals(15, inventory.getTotalPieces());
    }

    @Test
    public void testRemovePieceSuccessfully() {
        inventory.addPiece(piece1);
        inventory.addPiece(piece2);
        inventory.removePiece(piece1);
        assertFalse(inventory.getPieces().contains(piece1));
        assertTrue(inventory.getPieces().contains(piece2));
        assertEquals(1, inventory.getPieces().size());
    }

    @Test
    public void testRemoveNonExistentPiece() {
        LegoPiece fakePiece = new LegoPiece("Tile", "Green", "1x1", 3);
        inventory.addPiece(piece1);
        inventory.addPiece(piece2);
        inventory.removePiece(fakePiece);

        assertTrue(inventory.getPieces().contains(piece1));
        assertTrue(inventory.getPieces().contains(piece2));
        assertEquals(2, inventory.getPieces().size());
    }
}