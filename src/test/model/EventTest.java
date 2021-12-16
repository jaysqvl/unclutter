package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
	private Event e;
    private Event n;
    private Event g;
	private Date d;
	
	//NOTE: these tests might fail if time at which line (2) below is executed
	//is different from time that line (1) is executed.  Lines (1) and (2) must
	//run in same millisecond for this test to make sense and pass.
	
	@BeforeEach
	public void runBefore() {
		e = new Event("New List Inserted At Index: 0");   // (1)
		d = Calendar.getInstance().getTime();   // (2)

        g = new Event("Not the same Event");
	}
	
	@Test
	public void testEvent() {
		assertEquals("New List Inserted At Index: 0", e.getDescription());
        // To get these tests to pass, convert them toString just to ensure the time is the same (not to the exact millisecond)
        // but atleast to the second in 99.9% of cases
		assertEquals(d.toString(), e.getDate().toString());
	}

    @Test
    public void equalsAndHashCodeTest() {
        // Avoid using assertEquals so that the Event class can handle e.hashCode and equals overridden.

        // Compare valid to null
        assertFalse(e.equals(n));

        // Compare to same type different values
        assertFalse(e.equals(g));
        assertFalse(e.hashCode() == g.hashCode());

        // Compare to different type
        assertFalse(e.equals(d));
        assertFalse(e.hashCode() == d.hashCode());

        // Compare itself
        assertTrue(e.equals(e));
        assertTrue(e.hashCode() == e.hashCode());
    }

	@Test
	public void testToString() {
		assertEquals(d.toString() + "\n" + "New List Inserted At Index: 0", e.toString());
	}
}
