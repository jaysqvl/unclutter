package persistence;

import model.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkList(String listName, ArrayList<Task> taskList, List list) {
        assertEquals(listName, list.getListName());
        for (int i = 0; i < list.getTasks().size(); i++) {
            Task t = taskList.get(i);
            checkTask(t.readTask(), t.readDate(), t.readImportance(), list.getTasks().get(i));
        }
    }

    protected void checkTask(String taskName, String date, int importance, Task task) {
        assertEquals(taskName, task.readTask());
        assertEquals(date, task.readDate());
        assertEquals(importance, task.readImportance());
    }
}
