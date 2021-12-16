package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;

// Represents an instance of the ToDoApp container for list of ToDoLists
public class ToDo implements Writeable {

    private ArrayList<List> listOfLists;

    // CONSTRUCTOR
    // EFFECTS: Instantiates a new list of to-do lists.
    public ToDo() {
        listOfLists = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: if no lists, return 0, else remove every list in listOfLists and returns the number of lists removed
    public int removeAll() {
        int count = 0;
        for (int i = numLists(); i > 0; i--) {
            removeList(i); // will document which lists are removed
            count++;
        }
        EventLog.getInstance().logEvent(new Event("Removed (" + count + ") lists in to do app"));
        return count;
    }

    // EFFECTS: if name of list exists, returns the index of the list, else -1
    public int findList(String name) {
        for (int i = 0; i < listOfLists.size(); i++) {
            if (name.equals(listOfLists.get(i).getListName())) {
                return i;
            }
        }
        return -1;
    }

    // EFFECTS: returns listOfLists (getter)
    public ArrayList<List> getListOfLists() {
        return listOfLists;
    }

    // EFFECTS: returns the size of listOfLists
    public int numLists() {
        return listOfLists.size();
    }

    public int insertNewList(int index, String name) {
        if (index >= numLists()) {
            listOfLists.add(new List(new ArrayList<>(), name));
            EventLog.getInstance().logEvent(new Event("New List Inserted At Index: " + (numLists() - 1)));
            return numLists() - 1;
        } else if (index < 0) {
            listOfLists.add(0, new List(new ArrayList<>(), name));
            EventLog.getInstance().logEvent(new Event("New List Inserted At Index: " + 0));
            return 0;
        } else {
            listOfLists.add(index, new List(new ArrayList<>(), name));
            EventLog.getInstance().logEvent(new Event("New List Inserted At Index: " + index));
            return index;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new list to listOfLists with inputted name and returns the index it was placed at
    public int addList(String name) {
        listOfLists.add(new List(new ArrayList<>(), name));
        EventLog.getInstance().logEvent(new Event("New List Inserted At Index: " + (listOfLists.size() - 1)));
        return listOfLists.size() - 1;
    }

    // MODIFIES: this
    // EFFECTS: adds new list to listOfLists with inputted name and taskList and returns the index it was placed at
    public int addList(List list) {
        listOfLists.add(list);
        EventLog.getInstance().logEvent(new Event("New List Inserted At Index: " + (listOfLists.size() - 1)));
        return listOfLists.size() - 1;
    }

    // MODIFIES: this
    // EFFECTS: if this is empty, do nothing, else if index < 0 remove first element, if index > this.size() remove last
    public int removeList(int index) {
        if (listOfLists.size() != 0) {
            if (index >= 0 && index < listOfLists.size()) {
                listOfLists.remove(index);
                EventLog.getInstance().logEvent(new Event("List Removed From Index: " + index));
                return index;
            } else if (index < 0) {
                listOfLists.remove(0);
                EventLog.getInstance().logEvent(new Event("List Removed From Index: 0"));
                return 0;
            } else {
                listOfLists.remove(listOfLists.size() - 1);
                EventLog.getInstance().logEvent(new Event("List Removed From Index: " + listOfLists.size()));
                return listOfLists.size(); // already removed so this will be the index it removed from
            }
        } else {
            return -1;
        }
    }

    // DATA PERSISTENCE:
    // Methods Modelled after JsonSerialization Demo
    // Credit Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    // EFFECTS: turns this into a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ToDo instance", "Save1");
        json.put("ListOfLists", listsToJson());
        return json;
    }

    // EFFECTS: turns ArrayList<List> listOfLists into a JSONArray instance
    private JSONArray listsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (List l : listOfLists) {
            jsonArray.put(l.toJson());
        }

        return jsonArray;
    }
}
