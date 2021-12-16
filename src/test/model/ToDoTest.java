package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoTest {
    private ToDo toDo;
    private ToDo toDo1;
    private ToDo toDo2;
    private ToDo toDo3;
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
    void runBefore() {
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

        toDo = new ToDo();

        toDo1 = new ToDo();
        toDo1.getListOfLists().add(list1);

        toDo2 = new ToDo();
        toDo2.getListOfLists().add(list1);
        toDo2.getListOfLists().add(list2);
        toDo2.getListOfLists().add(list3);

        toDo3 = new ToDo();
        toDo3.getListOfLists().add(list);
        toDo3.getListOfLists().add(list1);
        toDo3.getListOfLists().add(list2);
        toDo3.getListOfLists().add(list3);
        toDo3.getListOfLists().add(list4);
    }

    @Test
    void insertNewListTest() {
        // Test insert at index above numLists
        assertEquals(5, toDo3.insertNewList(10, "Test"));
        assertEquals("Test", toDo3.getListOfLists().get(5).getListName());

        // Test insert at index at boundary of numLists (equal to num lists)
        assertEquals(3, toDo2.insertNewList(3, "Test1"));
        assertEquals("Test1", toDo2.getListOfLists().get(3).getListName());


        // Test insert at index below 0
        assertEquals(0, toDo2.insertNewList(-5, "wowza"));
        assertEquals("wowza", toDo2.getListOfLists().get(0).getListName());

        // Test insert at index at 0
        assertEquals(0, toDo2.insertNewList(0, "testindex0"));
        assertEquals("testindex0", toDo2.getListOfLists().get(0).getListName());

        // test insert at valid point in between
        assertEquals(2, toDo3.insertNewList(2, "between"));
        assertEquals("between", toDo3.getListOfLists().get(2).getListName());
    }

    @Test
    void addListTest() {
        // Add new list "Jays List" to empty list
        assertEquals(0, toDo.addList("Jays List"));
        assertEquals("Jays List", toDo.getListOfLists().get(0).getListName());

        // Add new list "Andrews List" should add to end
        assertEquals(1, toDo.addList("Andrews List"));
        assertEquals("Andrews List", toDo.getListOfLists().get(1).getListName());
    }

    @Test
    void removeListTest() {
        // remove from empty list
        assertEquals(0, toDo.getListOfLists().size());
        assertEquals(-1, toDo.removeList(0));
        assertEquals(0, toDo.getListOfLists().size());

        // remove from inbounds
        assertEquals(1, toDo1.getListOfLists().size());
        assertEquals(0, toDo1.removeList(0));


        // remove from inbounds boundary lower
        assertEquals(5, toDo3.getListOfLists().size());
        assertEquals(0, toDo3.removeList(0));
        assertEquals(4, toDo3.getListOfLists().size());

        // remove from inbounds boundary upper
        assertEquals(4, toDo3.getListOfLists().size());
        assertEquals(3, toDo3.removeList(3));
        assertEquals(3, toDo3.getListOfLists().size());

        // remove from out of bounds above
        assertEquals(3, toDo2.getListOfLists().size());
        assertEquals(2, toDo2.removeList(100));
        assertEquals(2, toDo2.getListOfLists().size());

        // remove from out of bounds below
        assertEquals(3, toDo3.getListOfLists().size());
        assertEquals(0, toDo3.removeList(-100));
        assertEquals(2, toDo3.getListOfLists().size());

        // remove from out of bounds below (smaller)
        assertEquals(2, toDo3.getListOfLists().size());
        assertEquals(0, toDo3.removeList(-1));
        assertEquals(1, toDo3.getListOfLists().size());
    }

    @Test
    void removeAllTest() {
        // Test on empty list
        assertEquals(0, toDo.removeAll());
        assertEquals(0, toDo.numLists());

        // Test on multiple lists
        assertEquals(5, toDo3.removeAll());
        assertEquals(0, toDo3.numLists());

        // Test on single list
        assertEquals(1, toDo1.removeAll());
        assertEquals(0, toDo1.numLists());
    }

    @Test
    void findListTest() {
        // search index empty list
        assertEquals(-1, toDo.findList("list1"));

        // search doesn't exist
        assertEquals(-1, toDo3.findList("list6"));

        // search does exist
        assertEquals(2, toDo3.findList("list2"));

        // search end of list
        assertEquals(4, toDo3.findList("list4"));

        // search beginning of list
        assertEquals(0, toDo3.findList("list"));
    }

    @Test
    void numLists() {
        // empty
        assertEquals(0, toDo.numLists());

        // full
        assertEquals(5, toDo3.numLists());
    }

    @Test
    void toJsonTest() {
        // empty object
        assertTrue(toDo.toJson().getJSONArray("ListOfLists").isEmpty());

        // full random list
        assertFalse(toDo1.toJson().getJSONArray("ListOfLists").isEmpty());
    }
}
