package model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class MasterBuilderTest {
    private MasterBuilder masterBuilder;
    private LegoInventory inventory;
    private Build build;
    private LegoPiece piece1;
    private LegoPiece piece2;

    @BeforeEach
    void setUp() {
        masterBuilder = new MasterBuilder("Shaurya");
        inventory = new LegoInventory("Shaurya's Collection");
        build = new Build(18059);
        piece1 = new LegoPiece("Brick", "Red", "2x4", 10);
        piece2 = new LegoPiece("Plate", "Blue", "1x2", 5);
    }

    @Test
    void testConstructor() {
        assertEquals("Shaurya", masterBuilder.getUserName());
        assertTrue(masterBuilder.getInventories().isEmpty());
        assertTrue(masterBuilder.getBuilds().isEmpty());
    }

    @Test
    void testAddInventory() {
        masterBuilder.addInventory(inventory);
        List<LegoInventory> inventories = masterBuilder.getInventories();
        assertEquals(1, inventories.size());
        assertEquals(inventory, inventories.get(0));
    }

    @Test
    void testAddBuild() {
        masterBuilder.addBuild(build);
        List<Build> builds = masterBuilder.getBuilds();
        assertEquals(1, builds.size());
        assertEquals(build, builds.get(0));
    }

    @Test
    void testIsBuildCompleteNotEnoughPieces() {
        build.addRequiredPiece(piece1);
        build.addRequiredPiece(piece2);
        masterBuilder.addBuild(build);
        assertFalse(masterBuilder.isBuildable(build));
    }

    @Test
    void testIsBuildCompleteEnoughPieces() {
        build.addRequiredPiece(piece1);
        inventory.addPiece(new LegoPiece("Brick", "Red", "2x4", 10));
        masterBuilder.addInventory(inventory);
        masterBuilder.addBuild(build);
        assertTrue(masterBuilder.isBuildable(build));
    }

    @Test
    void testGetABuild() {
        Build build1 = new Build(1);
        Build build2 = new Build(2);
        masterBuilder.addBuild(build1);
        masterBuilder.addBuild(build2);
        
        assertEquals(build1, masterBuilder.getABuild(1));
        assertEquals(build2, masterBuilder.getABuild(2));
        assertNull(masterBuilder.getABuild(3)); // Non-existent build
    }

    @Test
    void testGetAnInventory() {
        LegoInventory inv1 = new LegoInventory("Inventory1");
        LegoInventory inv2 = new LegoInventory("Inventory2");
        masterBuilder.addInventory(inv1);
        masterBuilder.addInventory(inv2);
        
        assertEquals(inv1, masterBuilder.getAnInventory("Inventory1"));
        assertEquals(inv2, masterBuilder.getAnInventory("Inventory2"));
        assertNull(masterBuilder.getAnInventory("NonExistentInventory")); // Non-existent inventory
    }
    
    @Test
    void testIsBuildablePartiallyMissingPieces() {
        Build build = new Build(7);
        build.addRequiredPiece(new LegoPiece("Brick", "Red", "2x4", 10)); // Required
        build.addRequiredPiece(new LegoPiece("Plate", "Blue", "1x2", 5)); // Required
    
        LegoInventory inventory = new LegoInventory("TestInventory");
        inventory.addPiece(new LegoPiece("Brick", "Red", "2x4", 10)); // Only one type available
    
        masterBuilder.addInventory(inventory);
        masterBuilder.addBuild(build);
    
        assertFalse(masterBuilder.isBuildable(build)); // Should return false because "Plate" piece is missing
    }
    
    @Test
    void testIsBuildableFailsDueToColorMismatch() {
        Build build = new Build(22);
        LegoInventory inventory = new LegoInventory("TestInventory");

        build.addRequiredPiece(new LegoPiece("Brick", "Red", "2x4", 5));
        inventory.addPiece(new LegoPiece("Brick", "Blue", "2x4", 5)); // Color does not match

        masterBuilder.addInventory(inventory);
        masterBuilder.addBuild(build);

        assertFalse(masterBuilder.isBuildable(build)); // Should return false because color is different
    }

    @Test
    void testIsBuildableFailsDueToDimensionMismatch() {
        Build build = new Build(23);
        LegoInventory inventory = new LegoInventory("TestInventory");

        build.addRequiredPiece(new LegoPiece("Brick", "Red", "2x4", 5));
        inventory.addPiece(new LegoPiece("Brick", "Red", "1x4", 5)); // Dimensions do not match

        masterBuilder.addInventory(inventory);
        masterBuilder.addBuild(build);

        assertFalse(masterBuilder.isBuildable(build)); // Should return false because dimensions are different
    }
}
