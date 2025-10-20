package model;

import persistence.JsonReader;
import persistence.JsonWriter;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterNonExistantFile() {
        try {
            MasterBuilder masterBuilder = new MasterBuilder("TestUser");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyMasterBuilder() {
        try {
            MasterBuilder masterBuilder = new MasterBuilder("TestUser");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMasterBuilder.json");
            writer.open();
            writer.write(masterBuilder);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMasterBuilder.json");
            masterBuilder = reader.read();
            assertEquals("TestUser", masterBuilder.getUserName());
            assertTrue(masterBuilder.getInventories().isEmpty());
            assertTrue(masterBuilder.getBuilds().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterMasterBuilderWithInventory() {
        try {
            MasterBuilder masterBuilder = new MasterBuilder("TestUser");
            LegoInventory inventory = new LegoInventory("TestInventory");
            masterBuilder.addInventory(inventory);

            JsonWriter writer = new JsonWriter("./data/testWriterMasterBuilderWithInventory.json");
            writer.open();
            writer.write(masterBuilder);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterMasterBuilderWithInventory.json");
            masterBuilder = reader.read();
            assertEquals("TestUser", masterBuilder.getUserName());
            assertEquals(1, masterBuilder.getInventories().size());
            assertEquals("TestInventory", masterBuilder.getInventories().get(0).getCollectionName());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterMasterBuilderWithBuilds() {
        try {
            MasterBuilder masterBuilder = new MasterBuilder("TestUser");
            Build build = new Build(1);
            masterBuilder.addBuild(build);

            JsonWriter writer = new JsonWriter("./data/testWriterMasterBuilderWithBuilds.json");
            writer.open();
            writer.write(masterBuilder);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterMasterBuilderWithBuilds.json");
            masterBuilder = reader.read();
            assertEquals("TestUser", masterBuilder.getUserName());
            assertEquals(1, masterBuilder.getBuilds().size());
            assertEquals(1, masterBuilder.getBuilds().get(0).getBuildId());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterMasterBuilderWithInventoryAndPieces() {
        try {
            MasterBuilder masterBuilder = new MasterBuilder("TestUser");
            LegoInventory inventory = new LegoInventory("TestInventory");
            LegoPiece piece = new LegoPiece("Brick", "Red", "2x4", 5);
            inventory.addPiece(piece);
            masterBuilder.addInventory(inventory);

            JsonWriter writer = new JsonWriter("./data/testWriterMasterBuilderWithInventoryAndPieces.json");
            writer.open();
            writer.write(masterBuilder);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterMasterBuilderWithInventoryAndPieces.json");
            masterBuilder = reader.read();
            assertEquals("TestUser", masterBuilder.getUserName());
            assertEquals(1, masterBuilder.getInventories().size());
            LegoInventory readInventory = masterBuilder.getInventories().get(0);
            assertEquals("TestInventory", readInventory.getCollectionName());
            assertEquals(1, readInventory.getPieces().size());
            LegoPiece readPiece = readInventory.getPieces().get(0);
            checkLegoPiece("Brick", "Red", "2x4", 5, readPiece);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    @SuppressWarnings("methodlength")
    void testWriterMasterBuilderWithBuildsAndPieces() {
        try {
            MasterBuilder masterBuilder = new MasterBuilder("TestUser");
            Build build = new Build(2);
            
            // Add required pieces
            build.addRequiredPiece(new LegoPiece("Brick", "Blue", "2x4", 10));
            build.addRequiredPiece(new LegoPiece("Plate", "Green", "1x2", 5));
            
            masterBuilder.addBuild(build);

            JsonWriter writer = new JsonWriter("./data/testWriterMasterBuilderWithBuildsAndPieces.json");
            writer.open();
            writer.write(masterBuilder);
            writer.close();

            // Read back the data
            JsonReader reader = new JsonReader("./data/testWriterMasterBuilderWithBuildsAndPieces.json");
            masterBuilder = reader.read();

            // Check MasterBuilder details
            assertEquals("TestUser", masterBuilder.getUserName());
            assertEquals(1, masterBuilder.getBuilds().size());

            // Check Build details
            Build readBuild = masterBuilder.getBuilds().get(0);
            assertEquals(2, readBuild.getBuildId());
            assertEquals(2, readBuild.getRequiredPieces().size());

            // Check first required piece
            LegoPiece piece1 = readBuild.getRequiredPieces().get(0);
            assertEquals("Brick", piece1.getPartType());
            assertEquals("Blue", piece1.getColor());
            assertEquals("2x4", piece1.getDimensions());
            assertEquals(10, piece1.getQuantity());

            // Check second required piece
            LegoPiece piece2 = readBuild.getRequiredPieces().get(1);
            assertEquals("Plate", piece2.getPartType());
            assertEquals("Green", piece2.getColor());
            assertEquals("1x2", piece2.getDimensions());
            assertEquals(5, piece2.getQuantity());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
