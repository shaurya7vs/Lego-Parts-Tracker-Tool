package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import model.LegoPiece;
import model.LegoInventory;

public class JsonTest {
    protected void checkLegoPiece(String partType, String color, String dimensions, int quantity, LegoPiece piece) {
        assertEquals(partType, piece.getPartType());
        assertEquals(color, piece.getColor());
        assertEquals(dimensions, piece.getDimensions());
        assertEquals(quantity, piece.getQuantity());
    }
    
    protected void checkLegoInventory(String collectionName, int numPieces, LegoInventory inventory) {
        assertEquals(collectionName, inventory.getCollectionName());
        assertEquals(numPieces, inventory.getPieces().size());
    }
}
