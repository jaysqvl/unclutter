package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Class Tests Modelled after JsonSerialization Demo
// Credit Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

class JsonReaderTest extends JsonTest {

    Task task1;
    Task task2;
    ArrayList<Task> taskGeneral;

    @BeforeEach
    void runBefore() {
        task1 = new Task("task1", "2020-01-01", 10);
        task2 = new Task("task2", "2020-02-02", 10);
        taskGeneral = new ArrayList<>();
        taskGeneral.add(task1);
        taskGeneral.add(task2);
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ToDo td = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyToDo() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyToDo.json");
        try {
            ToDo td = reader.read();
            assertEquals(0, td.numLists());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralToDo() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralToDo.json");
        try {
            ToDo td = reader.read();
            ArrayList<List> lists = td.getListOfLists();
            assertEquals(2, lists.size());
            checkList("Jays List", taskGeneral, lists.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}