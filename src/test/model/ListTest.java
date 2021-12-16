package model;

import org.junit.jupiter.api.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTest {

    List list;
    List list1;
    List list2;
    List list3;
    List list4;
    private Task testTask1;
    private Task testTask2;
    private Task testTask3;
    ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<Task> taskList1 = new ArrayList<>();
    ArrayList<Task> taskList2 = new ArrayList<>();

    @BeforeEach
    void setup() {
        testTask1 = new Task("Finish my math homework", "2020-01-11", 10);
        testTask2 = new Task("", "", -1);
        testTask3 = new Task("Do cs project", "2022-03-23", 5);

        taskList1.add(testTask1);
        taskList1.add(testTask2);
        taskList2.add(testTask2);

        list = new List(new ArrayList<>(), "list");

        list1 = new List(new ArrayList<>(), "list1");
        list1.addTask(testTask1);
        list1.addTask(testTask2);

        list2 = new List(new ArrayList<>(), "list2");
        list2.addTask(testTask1);

        list3 = new List(new ArrayList<>(), "list3");
        list3.addTask(testTask2);

        list4 = new List(new ArrayList<>(), "list4");
        list4.addTask(testTask2);
        list4.addTask(testTask1);
    }

    @Test
    public void addTaskTest() {
        // List1 adding testItem1 should have testItem1 appended to end of the list
        list.addTask(testTask1);
        assertEquals(testTask1, list.getTasks().get(0));

        // List2 adding testItem2 should contain testItem2 appended to end of the list
        list2.addTask(testTask2);
        assertEquals(testTask2, list2.getTasks().get(1));
    }

    @Test
    public void insertTaskTest() {
        // List3 = task 2 ---> task 1, task 2
        // list1 = 1, 2 ---> 2, 1, 2
        // list1 = 2, 1, 1, 2 --> 2, 1, 1, 2
        // list = empty ---> testTask1
        // list 2 = task 1 ---> Task 1, task 2
        // list 2 = task 1, task 2 ---> task 2, task 1, task 2

        // List3 insertItem(testItem1, 0) should result in testItem1 becoming the new item at index 0 and return
        // the index it was inserted in
        assertEquals(0, list3.insertTask(testTask1, 0));

        // List1 insertItem(testItem2, 0) should result in testItem2 still being at index 0
        assertEquals(0, list1.insertTask(testTask2, 0));

        // List1 insertItem(testItem2, 0) should result in testItem2 still being at index 0
        assertEquals(2, list1.insertTask(testTask1, 2));

        // List insertItem(testItem1, 0) should result in testItem1 replacing the empty list
        assertEquals(0, list.insertTask(testTask1, 0));

        // List insertItem(testItem1, 10) should result in a SOP("Failed to insert at specified index, added at the end of list");
        assertEquals(0, list.insertTask(testTask1, 0));

        // List insertItem(testItem1, 25) should return an index of 1
        assertEquals(1, list2.insertTask(testTask2, 25));

        // List insertItem(testItem1, -5) should return an index of 0
        assertEquals(0, list2.insertTask(testTask2, -100));
    }

    @Test
    public void removeTaskTest() {
        assertEquals(0, list3.removeTask(0));

        // List1 removeTask(0) should return 0, because there was a valid item at that valid position
        assertEquals(0, list1.removeTask(0));

        // List removeTask(0) should return 0 and have no effect because there were no items in the list
        assertEquals(0, list.removeTask(0));

        // List2 removeTask(10) should return 0 and return the last element in the list
        assertEquals(0, list2.removeTask(10));

        // List1 removeTask(-5) should result in a SOP and the first element removed
        list1.insertTask(testTask1, 0);
        list1.insertTask(testTask3, 1);
        list1.insertTask(testTask2, 2);
        assertEquals(0, list1.removeTask(-5));

        // List1 removeTask(1)
        assertEquals(1, list1.removeTask(1));

        for (int k = 0; k < 15; k++) {
            Task testTask = new Task("test", "", k);
            list.addTask(testTask);
        }

        // list removeTask(1000)
        assertEquals(list.getTasks().size() - 1, list.removeTask(1000));

        // list remove(13) is the exact boundary of the list upper
        assertEquals(13, list.removeTask(13));
    }

    @Test
    void removeAllTest() {
        // Test on empty list
        assertEquals(0, list.removeAll());
        assertEquals(0, list.numTasks());

        // Test on multiple lists
        assertEquals(2, list1.removeAll());
        assertEquals(0, list1.numTasks());

        // Test on single list
        assertEquals(1, list2.removeAll());
        assertEquals(0, list2.numTasks());
    }

    @Test
    public void swapTaskTest() {
        // swaps first and second item index 1 > index 2 (both indices valid)
        assertTrue(list1.swapTask(1,0));
        assertEquals(list4.getTasks().get(0), list1.getTasks().get(0));
        assertEquals(list4.getTasks().get(1), list1.getTasks().get(1));

        // swaps first and second item index 1 < index 2 (both indices valid)
        assertTrue(list1.swapTask(0,1));
        assertEquals(list4.getTasks().get(1), list1.getTasks().get(0));
        assertEquals(list4.getTasks().get(0), list1.getTasks().get(1));

        // first index out of bounds lower
        assertFalse(list1.swapTask(-1,0));
        assertEquals(list4.getTasks().get(1), list1.getTasks().get(0));
        assertEquals(list4.getTasks().get(0), list1.getTasks().get(1));

        // first index out of bounds upper
        assertFalse(list1.swapTask(100,0));
        assertEquals(list4.getTasks().get(1), list1.getTasks().get(0));
        assertEquals(list4.getTasks().get(0), list1.getTasks().get(1));

        // second index out of bounds lower
        assertFalse(list1.swapTask(0,-25));
        assertEquals(list4.getTasks().get(1), list1.getTasks().get(0));
        assertEquals(list4.getTasks().get(0), list1.getTasks().get(1));

        // second index out of bounds upper
        assertFalse(list1.swapTask(0,25));
        assertEquals(list4.getTasks().get(1), list1.getTasks().get(0));
        assertEquals(list4.getTasks().get(0), list1.getTasks().get(1));

        // swaps the same index for itself
        assertTrue(list1.swapTask(0,0));
        assertEquals(list4.getTasks().get(1), list1.getTasks().get(0));
        assertEquals(list4.getTasks().get(0), list1.getTasks().get(1));
    }

    @Test
    public void sortImportanceTest() {

        // List = empty -> empty
        // list2 = task1 -> task1
        // list 2

        // Test the empty case
        assertEquals(new ArrayList<Task>(), list.getTasks());
        list.sortImportance();
        assertEquals(new ArrayList<Task>(), list.getTasks());

        // Test with one item
        ArrayList<Task> test1 = new ArrayList<>();
        test1.add(testTask1);
        assertEquals(test1, list2.getTasks());
        list2.sortImportance();
        assertEquals(test1, list2.getTasks());

        // Test with two items
        ArrayList<Task> test2 = new ArrayList<>();
        test2.add(testTask2);
        test2.add(testTask1);
        assertEquals(test2, list4.getTasks());
        list1.sortImportance();
        assertEquals(list4.getTasks(), list1.getTasks());

        // Test with 4 items scattered
        // task 3, task 2, task 1, task 2 -> task 2, task 2, task 3, task 1
        list4.insertTask(testTask3, 0);
        list4.insertTask(testTask2, 3);
        ArrayList<Task> test3 = new ArrayList<>();
        test3.add(testTask3);
        test3.add(testTask2);
        test3.add(testTask1);
        test3.add(testTask2);
        assertEquals(test3, list4.getTasks());

        list4.sortImportance();
        // Checks to see that the list is sorted accordingly
        for (int k = 1; k < (list4.getTasks().size()); k++) {
            assertTrue(list4.getTasks().get(k - 1).readImportance() <= list4.getTasks().get(k).readImportance());
        }
    }

    @Test
    public void sortDateTest() {
        // Test the empty case
        assertEquals(new ArrayList<Task>(), list.getTasks());
        list.sortDate();
        assertEquals(new ArrayList<Task>(), list.getTasks());

        // Test with one item
        ArrayList<Task> test1 = new ArrayList<>();
        test1.add(testTask1);
        assertEquals(test1, list2.getTasks());
        list2.sortDate();
        assertEquals(test1, list2.getTasks());

        // Test with two or more items
        ArrayList<Task> test2 = new ArrayList<>();
        test2.add(testTask1);
        test2.add(testTask2);
        assertEquals(test2, list1.getTasks());

        list1.sortDate();
        for (int k = 1; k < list1.getTasks().size(); k++) {
            assertTrue(list1.getTasks().get(k - 1).readDateInt() <= list1.getTasks().get(k).readDateInt());
        }

        // Test with 5 items scattered
        // task 3, task 2, task 1, task 2, task 3 -> task 1, task 3, task 3, task 2, task 2
        list4.insertTask(testTask3, 0);
        list4.insertTask(testTask2, 3);
        list4.insertTask(testTask3, 4);
        ArrayList<Task> test3 = new ArrayList<>();
        test3.add(testTask3);
        test3.add(testTask2);
        test3.add(testTask1);
        test3.add(testTask2);
        test3.add(testTask3);
        assertEquals(test3, list4.getTasks());

        list4.sortDate();

        // Checks to see that the list is sorted accordingly and every value is less than the previous
        for (int k = 1; k < (list4.getTasks().size()); k++) {
            assertTrue(list4.getTasks().get(k - 1).readDateInt() <= list4.getTasks().get(k).readDateInt());
        }
    }

    @Test
    public void getListNameTest() {
        assertEquals("list", list.getListName());
        assertEquals("list1", list1.getListName());
    }

    @Test
    public void getTasksTest() {
        assertEquals(taskList1, list1.getTasks());
        assertEquals(taskList, list.getTasks());
    }

    @Test
    void numTasksTest() {
        // Empty List Of Tasks
        assertEquals(0, list.numTasks());

        // Full List Of Tasks
        assertEquals(2, list1.numTasks());
    }

    @Test
    void containsTaskTest() {
        // Empty list
        assertFalse(list.containsTask(testTask1.readTask()));

        // Multiple tasks, take the second one (iterating properly)
        assertTrue(list1.containsTask(testTask2.readTask()));

        // Multiple tasks but doesn't exist in the list
        assertFalse(list1.containsTask(testTask3.readTask()));

        // Test the empty string and on one that actually contains the empty string
        assertFalse(list.containsTask(""));
        assertTrue(list1.containsTask(""));
    }
}
