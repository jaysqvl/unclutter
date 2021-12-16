package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;

// Represents a singular to-do list of tasks
public class List implements Writeable {

    ArrayList<Task> tasks;
    private String listName;

    // CONSTRUCTOR
    // EFFECTS: creates a list with empty array of tasks and id list name
    public List(ArrayList<Task> tasks, String listName) {
        this.tasks = tasks;
        this.listName = listName;
    }

    // EFFECTS: returns String list name
    public String getListName() {
        return listName;
    }

    // EFFECTS: Returns arraylist from the class List
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // EFFECTS: Returns the number of tasks in a list
    public int numTasks() {
        return this.tasks.size();
    }

    // EFFECTS: Returns true if taskList contains taskName
    public Boolean containsTask(String taskName) {
        for (int k = 0; k < getTasks().size(); k++) {
            if (tasks.get(k).readTask().equals(taskName)) {
                return true;
            }
        }
        return false;
    }

    // Modifies: this
    // Requires: Item is not null
    // Effects: adds item to list
    public void addTask(Task task) {
        EventLog.getInstance().logEvent(new Event("Task inserted in List:  "
                + this.listName + " @ Index: " + tasks.size()));
        tasks.add(task);
    }

    // Modifies: this
    // Requires: Item is not null
    // Effects: Inserts item into specified index in list and returns index inserted
    public int insertTask(Task task, int index) {
        if ((index >= 0) && (index < tasks.size())) {
            tasks.add(index, task);
            EventLog.getInstance().logEvent(new Event("Task Inserted in List: "
                    + this.listName + " @ Index: " + index));
            return index;
        } else if (index >= tasks.size()) {
            tasks.add(task);
            EventLog.getInstance().logEvent(new Event("Task Inserted in List: "
                    + this.listName + " @ Index: " + (tasks.size() - 1)));
            return tasks.size() - 1;
        } else {
            tasks.add(0, task);
            EventLog.getInstance().logEvent(new Event("Task Inserted in List: "
                    + this.listName + " @ Index: 0"));
            return 0;
        }
    }

    // Modifies: this
    // Requires: Int index > 0 and index < list.size()
    // Effects: Removes the item at the specified index
    public int removeTask(int index) {
        if (tasks.isEmpty()) {
            EventLog.getInstance().logEvent(new Event("Task Removed in List: "
                    + this.listName + " @ Index: 0"));
            return 0;
        } else if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            EventLog.getInstance().logEvent(new Event("Task Removed in List: "
                    + this.listName + " @ Index: " + index));
            return index;
        } else if (index >= tasks.size()) {
            tasks.remove(tasks.size() - 1);
            EventLog.getInstance().logEvent(new Event("Task Removed in List: "
                    + this.listName + " @ Index: " + tasks.size()));
            return tasks.size();
        } else {
            tasks.remove(0);
            EventLog.getInstance().logEvent(new Event("Task Removed in List: "
                    + this.listName + " @ Index: 0"));
            return 0;
        }
    }

    // MODIFIES: this
    // EFFECTS: if no tasks, do nothing, else remove every list in listOfLists
    public int removeAll() {
        int count = 0;
        for (int i = tasks.size(); i > 0; i--) {
            removeTask(i); // will document all removed tasks
            count++;
        }
        EventLog.getInstance().logEvent(new Event("Removed (" + count + ") lists in to do app"));
        return count;
    }

    // Modifies: this
    // Effects: if index 1 and index 2 are inbounds given list.size(), swaps index1 and index2's positions
    public Boolean swapTask(int index1, int index2) {
        Task store;
        Boolean index1Inbounds = (index1 >= 0 && index1 < tasks.size());
        Boolean index2Inbounds = (index2 >= 0 && index2 < tasks.size());
        if (index1Inbounds && index2Inbounds) {
            store = tasks.get(index1);
            tasks.set(index1, tasks.get(index2));
            tasks.set(index2, store);
            EventLog.getInstance().logEvent(new Event("Tasks swapped in List: "
                    + this.listName + " @ Index: " + index1 + ", " + index2));
            return true;
        } else {
            EventLog.getInstance().logEvent(new Event("Tried 'Swap task' in List: "
                    + this.listName + " @ Indices: " + index1 + ", " + index2));
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: Sorts any list of integers in ascending order using insertion sort, most important at the bottom
    public void sortImportance() {
        int size = tasks.size();
        for (int i = 1; i < size; ++i) {
            Task key = tasks.get(i);
            int j = i - 1;

            while (j >= 0 && tasks.get(j).readImportance() > key.readImportance()) {
                tasks.set(j + 1, tasks.get(j));
                j--;
            }

            tasks.set((j + 1), key);
        }

        EventLog.getInstance().logEvent(new Event("List: " + this.listName + " sorted by importance"));
    }

    // REQUIRES: Dates are valid
    // MODIFIES: this
    // EFFECTS: Sorts any list of dates using a modified version of insertion sort from earliest to latest
    public void sortDate() {
        int size = tasks.size();
        for (int i = 1; i < size; ++i) {
            Task key = tasks.get(i);
            int j = i - 1;

            while (j >= 0 && tasks.get(j).readDateInt() > key.readDateInt()) {
                tasks.set(j + 1, tasks.get(j));
                j--;
            }

            tasks.set((j + 1), key);
        }

        EventLog.getInstance().logEvent(new Event("List: " + this.listName + " sorted by date"));
    }

    // DATA PERSISTENCE:
    // Methods Modelled after JsonSerialization Demo
    // Credit Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    // EFFECTS: turns this into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("listName", listName);
        json.put("tasks", tasksToJson());
        return json;
    }

    // EFFECTS: converts ArrayList<Task> list into a JSONArray instance
    public JSONArray tasksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Task t : tasks) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }

}
