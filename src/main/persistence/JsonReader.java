package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import model.*;
import org.json.*;

// Class Modelled after JsonSerialization Demo
// Credit Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ToDo read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseToDo(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses td from JSON object and returns it
    private ToDo parseToDo(JSONObject jsonObject) {
        ToDo td = new ToDo();
        addListLists(td, jsonObject);
        return td;
    }

    // MODIFIES: td
    // EFFECTS: parses listOfLists from JSON object and adds them to td
    // reads all lists in listOfLists
    private void addListLists(ToDo td, JSONObject jsonObject) {
        // jsonobject should be a tdinstance
        JSONArray jsonArray = jsonObject.getJSONArray("ListOfLists"); // accessing tdinstances lists
        for (Object json : jsonArray) { // iterates through every ToDoList "list" and runs addList
            JSONObject nextList = (JSONObject) json; // every list in list
            addList(td, nextList);
        }
    }

    // MODIFIES: td
    // EFFECTS: parses thingy from JSON object and adds it to td
    // reads listName and all tasks in list
    private void addList(ToDo td, JSONObject jsonObject) {
        // jsonobject should be a list
        String listName = jsonObject.getString("listName");
        JSONArray jsonArray = jsonObject.getJSONArray("tasks"); // list of tasks
        List newList = new List(new ArrayList<>(), listName);

        for (Object json : jsonArray) { // iterates through every tasklist "list" and runs addTask
            JSONObject nextTask = (JSONObject) json;
            newList.addTask(parseTask(nextTask)); // should parse the next task and add it to the newList
        }

        td.addList(newList);
    }

    // MODIFIES: list
    // EFFECTS: parses task from JSON object and adds it to list
    // reads task
    private Task parseTask(JSONObject jsonObject) {
        String name = jsonObject.getString("task name");
        String date = jsonObject.getString("date");
        int importance = jsonObject.getInt("importance");
        return new Task(name, date, importance);
    }
}
