package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Class Modelled after JsonSerialization Demo
// Credit Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

class JsonWriterTest extends JsonTest {

    Task task1;
    Task task2;
    ArrayList<Task> taskGeneral;
    ArrayList<Task> taskGeneral1;
    List list;
    List list1;
    List list2;
    ArrayList<List> lists;

    @BeforeEach
    void runBefore() {
        task1 = new Task("task1", "2020-01-01", 10);
        task2 = new Task("task2", "2020-02-02", 10);

        taskGeneral = new ArrayList<>();
        taskGeneral.add(task1);
        taskGeneral1 = new ArrayList<>();
        taskGeneral1.add(task1);
        taskGeneral1.add(task2);

        list = new List(new ArrayList<>(), "list");
        list1 = new List(taskGeneral, "list1");
        list2 = new List(taskGeneral, "list2");

        lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);

    }

    @Test
    void testWriterInvalidFile() {
        try {
            ToDo td = new ToDo();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyToDo() {
        try {
            ToDo td = new ToDo();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyToDo.json");
            writer.open();
            writer.write(td);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyToDo.json");
            td = reader.read();
            assertEquals(0, td.numLists());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralToDo() {
        try {
            ToDo td = new ToDo();
            td.getListOfLists().add(list1);
            td.getListOfLists().add(list2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralToDo.json");
            writer.open();
            writer.write(td);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralToDo.json");
            td = reader.read();
            ArrayList<List> listOfLists = td.getListOfLists();
            assertEquals(2, listOfLists.size());
            checkList("list1", taskGeneral, td.getListOfLists().get(0));
            checkList("list2", taskGeneral1, td.getListOfLists().get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}