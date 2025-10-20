package model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class BuildTest {
    private Build build;
    private LegoPiece piece1;
    private LegoPiece piece2;

    @BeforeEach
    void setUp() {
        build = new Build(51095);
        piece1 = new LegoPiece("Brick", "Red", "2x4", 10);
        piece2 = new LegoPiece("Plate", "Blue", "1x2", 5);
    }

    @Test
    void testConstructor() {
        assertEquals(51095, build.getBuildId());
        assertTrue(build.getRequiredPieces().isEmpty());
    }

    @Test
    void testAddRequiredPiece() {
        build.addRequiredPiece(piece1);
        build.addRequiredPiece(piece2);
        List<LegoPiece> pieces = build.getRequiredPieces();
        assertEquals(2, pieces.size());
        assertEquals(piece1, pieces.get(0));
        assertEquals(piece2, pieces.get(1));
    }
}
