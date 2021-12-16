package ui;

import model.List;
import model.Task;
import model.ToDo;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Represents an Optional Console GUI that preceded the current swing based GUI
public class ConsoleGui {

    private ToDo toDo;
    private static final String JSON_STORE = "./data/todoinstance.json";
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // CONSTRUCTOR
    // EFFECTS: Starts the ConsoleGUI with a fresh instance and the option to load more from file
    public ConsoleGui() {
        init();
        runToDoList();
    }

    // EFFECTS: Main menu list ui. Processes quit user command else, hands off command to processCommand (back-end)
    private void runToDoList() {
        boolean activeList = true;
        String command;

        while (activeList) {
            System.out.println("List Commands: ");
            System.out.println(" a - 'add list'");
            System.out.println(" q - 'quit'");
            System.out.println(" r - 'remove list'");
            System.out.println(" c - 'choose list'");
            System.out.println(" s - 'save lists'");
            System.out.println(" l - 'load lists'");
            displayLists();

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                activeList = false;
                System.exit(0);
            } else {
                processCommand(command);
            }
        }
    }

    // EFFECTS: instantiates list of to-do lists, scanner, and jsonWriter/jsonReader
    private void init() {
        toDo = new ToDo();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // REQUIRES: List of lists size > 0
    // EFFECTS: Prints listOfLists available to display or none
    private void displayLists() {
        if (toDo.getListOfLists().size() == 0) {
            System.out.println("No lists, please add via 'add list'");
        } else {
            System.out.println("Available Lists to View/Modify: ");
            for (List listOfList : toDo.getListOfLists()) {
                System.out.println("- " + listOfList.getListName());
            }
        }
    }

    // EFFECTS: SOP of all the tasks or "No tasks" if none currently active
    private void displayList(List list) {
        if (list.getTasks().isEmpty()) {
            System.out.println("No tasks");
        } else {
            for (int k = 0; k < list.getTasks().size(); k++) {
                Task currentTask = list.getTasks().get(k);
                System.out.println((k + 1) + ": " + currentTask.readTask() + " | "
                        + currentTask.readDate() + " | "
                        + currentTask.readImportance());
            }
        }
    }

    // MODIFIES: this -> listOfLists
    // EFFECTS: processes user commands in the back-end. Calls all program modifying functions.
    private void processCommand(String command) {
        switch (command) {
            case "s": // save list
                saveToDo();
                break;
            case "l": // load list
                loadToDo();
                break;
            case "c": // choose list
                inputList();
                break;
            case "a": // add list
                System.out.println("Insert list name");
                String addName = input.next();
                toDo.addList(addName);
                System.out.println("Added " + "'" + addName + "'");
                break;
            case "r": // remove list
                removeList();
                break;
            default:
                System.out.println("Selection was not valid...");
                break;
        }
    }

    // MODIFIES: this -> (currently selected) list
    // EFFECTS: Processes commands of selected list view in the backend
    public void processModify(List list, String command) {
        printTask(list, command);
        switch (command) {
            case "a": // add task
                addItemPrint(list);
                break;
            case "r": // remove task
                removeTaskPrint(list);
                break;
            case "m": // modify task
                printModifyTask(list);
                break;
            case "s": // swap tasks
                swapTaskPrint(list);
                break;
            default:
                processModifyExtended(list, command);
                break;
        }
    }

    // MODIFIES: this -> (currently selected) list
    // EFFECTS: Processes commands of selected list view in the backend
    public void processModifyExtended(List list, String command) {
        switch (command) {
            case "si": // sort by importance
                list.sortImportance();
                System.out.println("Sorted by importance");
                break;
            case "sd": // sort by date
                list.sortDate();
                System.out.println("Sorted by date");
                break;
            default:
                System.out.println("Selection was not valid...");
                break;
        }
    }


    // EFFECTS: Console UI and scanner method for selecting a list
    private void inputList() {
        System.out.println("Insert list name");
        String name = input.next();
        int selectIndex = toDo.findList(name);
        if (selectIndex != -1) {
            selectList(toDo.getListOfLists().get(selectIndex));
        } else {
            System.out.println("No list with that name");
        }
    }

    // MODIFIES: this -> list
    // EFFECTS: list selected ui, can go back or hands off further command to processModify
    private void selectList(List list) {
        boolean chooseList = true;
        String command;

        while (chooseList) {
            displayList(list);

            System.out.println("List Commands: ");
            System.out.println("b - 'back'");
            System.out.println("a - 'add task'");
            System.out.println("r - 'remove task'");
            System.out.println("s - 'swap tasks'");
            System.out.println("m - 'modify task'");
            System.out.println("si - 'sort by importance'");
            System.out.println("sd - 'sort by date'");

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                chooseList = false;
            } else {
                processModify(list, command);
            }
        }
    }

    // MODIFIES: toDo.this
    // EFFECTS: Console UI and scanner method for removing the inputted/chosen list
    private void removeList() {
        System.out.println("Insert list index");
        String listIndexInput = input.next();
        int removeIndex = Integer.parseInt(listIndexInput);
        toDo.removeList(removeIndex);
        System.out.println("Removed!");
    }

    // MODIFIES: toDo.this, task.this
    // EFFECTS: UI Method for adding item to a to-do list
    private void addItemPrint(List list) {
        System.out.println("Insert Task Name");
        String name = input.next();

        System.out.println("Insert Date (Format: YEAR-MT-DT e.g. 2021-01-01) or none ''");
        String date = input.next();

        System.out.println("Insert Weight (Scale: 1 - 10)");
        String importance = input.next();

        System.out.println("Insert number where the task should go");
        String index = input.next();
        int indexNum = Integer.parseInt(index);
        int importanceNum = Integer.parseInt(importance);

        Task newTask = new Task(name, date, importanceNum);

        if (date.length() == 0) {
            list.insertTask(newTask, indexNum);
        } else if (!newTask.monthOutOfBounds() && !newTask.dayOutOfBounds() && !newTask.importanceOutOfBounds()) {
            list.insertTask(newTask, indexNum);
        } else {
            System.out.println("Date, weight, or index format invalid, please try again");
        }
    }

    // MODIFIES: toDo.this, task.this
    // EFFECTS: Swaps the position of two tasks given two tasks' indices.
    private void swapTaskPrint(List list) {
        System.out.println("Insert index of first task");
        String index1 = input.next();
        int index1Num = Integer.parseInt(index1);
        System.out.println("Insert index of second task to swap");
        String index2 = input.next();
        int index2Num = Integer.parseInt(index2);
        list.swapTask(index1Num, index2Num);
    }

    // MODIFIES: toDo.this, task.this
    // EFFECTS: UI Method for removing item to a to-do list
    private void removeTaskPrint(List list) {
        System.out.println("Insert Item index");
        String listName = input.next();
        int listIndex = Integer.parseInt(listName);
        list.removeTask(listIndex);
    }

    // EFFECTS: Prints every task in a to-do list's properties concatenated in a SOP
    private void printTask(List list, String command) {
        for (int k = 0; k < list.getTasks().size(); k++) {
            if (command.equals(list.getTasks().get(k).readTask())) {
                Task chosenTask = list.getTasks().get(k);
                String name = chosenTask.readTask();
                String date = chosenTask.readDate();
                int importance = chosenTask.readImportance();
                System.out.println("Task: " + name + " | Date: " + date + " | Importance: " + importance);
            }
        }
    }

    // MODIFIES: toDo.this, task.this
    // EFFECTS: UI for modifying a specifically chosen item.
    private void printModifyTask(List list) {
        System.out.println("Insert Index of Item to Modify");
        String index = input.next();
        int indexNum = Integer.parseInt(index);
        System.out.println("Insert New Name");
        String newName = input.next();
        System.out.println("Insert New Date");
        String newDate = input.next();
        System.out.println("Insert New Importance Level");
        String newImportance = input.next();
        int newImportanceNum = Integer.parseInt(newImportance);
        Task newTask = new Task(newName, newDate, newImportanceNum);
        if (!newTask.monthOutOfBounds() && !newTask.dayOutOfBounds() && !newTask.importanceOutOfBounds()) {
            list.getTasks().get(indexNum).setTask(newName);
            list.getTasks().get(indexNum).setDate(newDate);
            list.getTasks().get(indexNum).setImportance(newImportanceNum);
        } else {
            System.out.println("Date or weight format invalid, please try again");
        }
    }

    // DATA PERSISTENCE:
    // Methods Modelled after JsonSerialization Demo
    // Credit Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    private void saveToDo() {
        try {
            jsonWriter.open();
            jsonWriter.write(toDo);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Successfully saved!", "STATUS",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Unable to write to file: " + JSON_STORE,
                    "STATUS", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadToDo() {
        try {
            toDo = jsonReader.read();
            JOptionPane.showMessageDialog(null, "Successfully loaded!", "STATUS",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE,
                    "STATUS", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
