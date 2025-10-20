package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Event;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event e1;
    private Date d1;

    //NOTE: these tests might fail if time at which line (2) below is executed
    //is different from time that line (1) is executed.  Lines (1) and (2) must
    //run in same millisecond for this test to make sense and pass.

    @BeforeEach
    public void runBefore() {
        e1 = new Event("Sensor open at door");   // (1)
        d1 = Calendar.getInstance().getTime();   // (2)
        
    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", e1.getDescription());
        assertEquals(d1, e1.getDate());
    }

    @Test
    public void testToString() {
        assertEquals(d1.toString() + "\n" + "Sensor open at door", e1.toString());
    }

    @Test
    public void testEqualsNull() {
        assertFalse(e1.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(e1.equals(""));
    }

    @Test
    public void testHashCodeCalledDirectly() {
        int hash = e1.hashCode();
        assertNotEquals(0, hash);
    }
}