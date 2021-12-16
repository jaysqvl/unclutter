package model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private Task testTask1;
    private Task testTask2;
    private Task testTask3;
    private Task testTask4;
    private Task testTask5;
    private Task testTask6;
    private Task testTask7;
    private Task testTask8;
    private Task testTask9;
    private Task testTask10;
    private Task testTask11;
    private Task testTask12;
    private Task testTask13;
    private Task testTask14;
    private Task testTask15;
    private Task testTask16;
    private Task testTask17;
    private Task testTask18;
    private Task testTask19;

    @BeforeEach
    void setup() {
        testTask1 = new Task("Finish my math homework", "2020-01-11", 10);
        testTask2 = new Task("", "", 1);
        testTask3 = new Task("finish CS project", "2021-10-20", 5);
        testTask4 = new Task("Finish eating", "2022-00-21", 3);
        testTask5 = new Task("Sleep", "2022-20-21", 8);
        testTask6 = new Task("Wake up", "0000-10-25",10);
        testTask7 = new Task("Go to school", "9999-03-00", 4);
        testTask8 = new Task("Tests8", "2026-12-09", 7);
        testTask9 = new Task("Tests9", "2027-06-10", 8);
        testTask10 = new Task("Tests10", "2028--5-10", 10);
        testTask11 = new Task("Tests11", "2028-50-10", 9);
        testTask12 = new Task("Tests12", "2029-05-01", 9);
        testTask13 = new Task("Tests13", "2030-03-31", 9);
        testTask14 = new Task("Tests14", "2028-50-99", 15);
        testTask15 = new Task("Tests15", "2029-05-00", -10);
        testTask16 = new Task("Tests16", "2020", 10);
        testTask17 = new Task("Tests17", "21532143214124", 10);
        testTask18 = new Task("Tests18", "1000-01-11", 10);
        testTask19 = new Task("Tests19", "3000-01-11", 10);
    }

    @Test
    public void readTaskTest() {
        // Testing simple getter
        assertEquals("Finish my math homework", testTask1.readTask());
        assertEquals("", testTask2.readTask());
    }

    @Test
    public void readDateTest() {
        // Testing simple getter
        assertEquals("2020-01-11", testTask1.readDate());
        assertEquals("", testTask2.readDate());
    }

    @Test
    public void readDateIntTest() {
        // Tests going from string to integer
        assertEquals(20211020, testTask3.readDateInt());

        assertEquals(20200111, testTask1.readDateInt());

        // Test the empty string (should return max value so that it in sorts, it shows up as the latest
        // due item
        assertEquals(Integer.MAX_VALUE, testTask2.readDateInt());
    }

    @Test
    public void readImportanceTest() {
        // Testing simple getter
        assertEquals(10, testTask1.readImportance());
        assertEquals(5, testTask3.readImportance());
        assertEquals(1, testTask2.readImportance());
    }

    @Test
    public void setTaskTest() {
        // Tests setting regular application
        assertEquals("Finish my math homework", testTask1.readTask());
        testTask1.setTask("Go on an airplane");
        assertEquals("Go on an airplane", testTask1.readTask());

        // Tests setting to symbols, etc
        assertEquals("", testTask2.readTask());
        testTask2.setTask("!@#$%^&*()_-+={}[]:;''?/.,><");
        assertEquals("!@#$%^&*()_-+={}[]:;''?/.,><", testTask2.readTask());

        // Tests setting to empty string
        assertEquals("finish CS project", testTask3.readTask());
        testTask3.setTask("");
        assertEquals("", testTask3.readTask());
    }

    @Test
    public void setImportanceTest() {
        // Tests setting within bounds
        assertEquals(10, testTask1.readImportance());
        testTask1.setImportance(5);
        assertEquals(5, testTask1.readImportance());

        // Tests negative integer setting
        assertEquals(1, testTask2.readImportance());
        testTask2.setImportance(-1000);
        assertEquals(-1000, testTask2.readImportance());

        // Tests setting positive integer boundary
        assertEquals(5, testTask3.readImportance());
        testTask2.setImportance(10000);
        assertEquals(10000, testTask2.readImportance());
    }

    @Test
    public void setDateTest() {
        // Tests setting within bounds
        assertEquals("2020-01-11", testTask1.readDate());
        testTask1.setDate("2022-03-12");
        assertEquals("2022-03-12", testTask1.readDate());

        // Tests setting to out of bounds month
        assertEquals("", testTask2.readDate());
        testTask2.setDate("2022-13-20");
        assertEquals("2022-13-20", testTask2.readDate());

        // Tests setting to out of bounds day
        assertEquals("2021-10-20", testTask3.readDate());
        testTask2.setDate("2022-10-33");
        assertEquals("2022-10-33", testTask2.readDate());
    }

    @Test
    public void invalidNameTest() {
        // test the invalid name
        assertTrue(testTask2.invalidName());

        // test the valid name
        assertFalse(testTask1.invalidName());
    }

    @Test
    public void dateOutOfBoundsTest() {
        // Empty string date
        assertFalse(testTask2.dateOutOfBounds());

        // Date length greater than 10
        assertTrue(testTask17.dateOutOfBounds());

        // Date length under 10
        assertTrue(testTask16.dateOutOfBounds());

        // Proper format but out of bounds for year, month, or day

        // Day
        // Test the valid day middle
        assertFalse(testTask1.dateOutOfBounds());

        // Test exact boundary lower
        assertFalse(testTask12.dateOutOfBounds());

        // Test exact boundary upper
        assertFalse(testTask13.dateOutOfBounds());

        // Test the invalid day lower
        assertTrue(testTask15.dateOutOfBounds());

        // Test the invalid day upper
        assertTrue(testTask14.dateOutOfBounds());

        // Month
        // Test the valid month (January)
        assertFalse(testTask1.dateOutOfBounds());

        // Test the valid month middle (June)
        assertFalse(testTask9.dateOutOfBounds());

        // Test the valid month (December)
        assertFalse(testTask8.dateOutOfBounds());

        // Test the invalid month lower (00)
        assertTrue(testTask4.dateOutOfBounds());

        // Test the invalid month upper (13)
        assertTrue(testTask5.dateOutOfBounds());

        // Test invalid format negative number
        assertTrue(testTask10.dateOutOfBounds());

        // Test invalid format large positive number
        assertTrue(testTask11.dateOutOfBounds());

        // Year
        // Test valid year
        assertFalse(testTask1.dateOutOfBounds());

        // Test invalid year lower
        assertTrue(testTask6.dateOutOfBounds());

        // Test invalid year upper
        assertTrue(testTask7.dateOutOfBounds());

        // Test year boundary lower
        assertFalse(testTask18.dateOutOfBounds());

        //Test year boundary upper
        assertFalse(testTask19.dateOutOfBounds());
    }

    @Test
    public void yearOutOfBoundsTest() {
        // Test valid year
        assertFalse(testTask1.yearOutOfBounds());

        // Test invalid year lower
        assertTrue(testTask6.yearOutOfBounds());

        // Test invalid year upper
        assertTrue(testTask7.yearOutOfBounds());

        // Test year boundary lower
        assertFalse(testTask18.yearOutOfBounds());

        //Test year boundary upper
        assertFalse(testTask19.yearOutOfBounds());
    }


    @Test
    public void monthOutOfBoundsTest() {
        // Test the valid month (January)
        assertFalse(testTask1.monthOutOfBounds());

        // Test the valid month middle (June)
        assertFalse(testTask9.monthOutOfBounds());

        // Test the valid month (December)
        assertFalse(testTask8.monthOutOfBounds());

        // Test the invalid month lower (00)
        assertTrue(testTask4.monthOutOfBounds());

        // Test the invalid month upper (13)
        assertTrue(testTask5.monthOutOfBounds());

        // Test invalid format negative number
        assertTrue(testTask10.monthOutOfBounds());

        // Test invalid format large positive number
        assertTrue(testTask11.monthOutOfBounds());
    }

    @Test
    public void dayOutOfBoundsTest() {
        // Test the valid day middle
        assertFalse(testTask1.dayOutOfBounds());

        // Test exact boundary lower
        assertFalse(testTask12.dayOutOfBounds());

        // Test exact boundary upper
        assertFalse(testTask13.dayOutOfBounds());

        // Test the invalid day lower
        assertTrue(testTask15.dayOutOfBounds());

        // Test the invalid day upper
        assertTrue(testTask14.dayOutOfBounds());
    }

    @Test
    public void importanceOutOfBoundsTest() {
        // Test the valid importance
        assertFalse(testTask1.importanceOutOfBounds());

        // Test the valid importance lower
        assertFalse(testTask2.importanceOutOfBounds());

        // Test the valid importance upper
        assertFalse(testTask10.importanceOutOfBounds());

        // Test the invalid importance lower
        assertTrue(testTask14.importanceOutOfBounds());

        // Test the invalid importance upper
        assertTrue(testTask15.importanceOutOfBounds());
    }
}
