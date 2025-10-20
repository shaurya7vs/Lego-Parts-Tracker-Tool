package model;

import static org.junit.jupiter.api.Assertions.*;
import persistence.JsonReader;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MasterBuilder masterBuilder = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyMasterBuilder() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMasterBuilder.json");
        try {
            MasterBuilder masterBuilder = reader.read();
            assertEquals("TestUser", masterBuilder.getUserName());
            assertTrue(masterBuilder.getInventories().isEmpty());
            assertTrue(masterBuilder.getBuilds().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderMasterBuilderWithInventory() {
        JsonReader reader = new JsonReader("./data/testReaderMasterBuilderWithInventory.json");
        try {
            MasterBuilder masterBuilder = reader.read();
            assertEquals("TestUser", masterBuilder.getUserName());
            assertEquals(1, masterBuilder.getInventories().size());
            assertEquals("TestInventory", masterBuilder.getInventories().get(0).getCollectionName());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderMasterBuilderWithBuilds() {
        JsonReader reader = new JsonReader("./data/testReaderMasterBuilderWithBuilds.json");
        try {
            MasterBuilder masterBuilder = reader.read();
            assertEquals("TestUser", masterBuilder.getUserName());
            assertEquals(1, masterBuilder.getBuilds().size());
            assertEquals(1, masterBuilder.getBuilds().get(0).getBuildId());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderMasterBuilderWithInventoryAndPieces() {
        JsonReader reader = new JsonReader("./data/testReaderMasterBuilderWithInventoryAndPieces.json");
        try {
            MasterBuilder masterBuilder = reader.read();
            assertEquals("TestUser", masterBuilder.getUserName());
            assertEquals(1, masterBuilder.getInventories().size());
            LegoInventory inventory = masterBuilder.getInventories().get(0);
            assertEquals(1, inventory.getPieces().size());
            LegoPiece piece = inventory.getPieces().get(0);
            assertEquals("Brick", piece.getPartType());
            assertEquals("Red", piece.getColor());
            assertEquals("2x4", piece.getDimensions());
            assertEquals(5, piece.getQuantity());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderMasterBuilderWithBuildsAndPieces() {
        JsonReader reader = new JsonReader("./data/testReaderMasterBuilderWithBuildsAndPieces.json");
        try {
            MasterBuilder masterBuilder = reader.read();
            
            // Check MasterBuilder details
            assertEquals("TestUser", masterBuilder.getUserName());
            assertEquals(1, masterBuilder.getBuilds().size());

            // Check Build details
            Build build = masterBuilder.getBuilds().get(0);
            assertEquals(2, build.getBuildId());
            assertEquals(2, build.getRequiredPieces().size());

            // Check first required piece
            LegoPiece piece1 = build.getRequiredPieces().get(0);
            assertEquals("Brick", piece1.getPartType());
            assertEquals("Blue", piece1.getColor());
            assertEquals("2x4", piece1.getDimensions());
            assertEquals(10, piece1.getQuantity());

            // Check second required piece
            LegoPiece piece2 = build.getRequiredPieces().get(1);
            assertEquals("Plate", piece2.getPartType());
            assertEquals("Green", piece2.getColor());
            assertEquals("1x2", piece2.getDimensions());
            assertEquals(5, piece2.getQuantity());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}