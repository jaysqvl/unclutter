package model;

import org.json.JSONObject;
import persistence.Writeable;

// Represents a task with due date and importance rating
public class Task implements Writeable {

    private String task;
    private String date;
    private int importance;

    // CONSTRUCTOR
    // EFFECTS: Instantiates a task with string "task" as a description, string due date, and int importance of task
    public Task(String task, String date, int importance) {
        this.task = task;
        this.date = date;
        this.importance = importance;
    }

    // EFFECTS: Returns String To-Do Task
    public String readTask() {
        return task;
    }

    // EFFECTS: Returns String due date of To-do task
    public String readDate() {
        return date;
    }

    // REQUIRES: Date is in the correct format YEAR-MM-DD or empty string
    // EFFECTS: Returns date as one large number going from most important numbers
    //              left to right to be most easily comparable (for sorting)
    public int readDateInt() {
        if (date.equals("")) {
            return Integer.MAX_VALUE; // So that those without date values will be sorted to the end
        } else {
            String year = date.substring(0, 4);
            String month = date.substring(5, 7);
            String day = date.substring(8);
            String concat = year + month + day;

            return Integer.parseInt(concat);
        }
    }

    // EFFECTS: Returns integer of importance rating
    public int readImportance() {
        return importance;
    }

    // MODIFIES: this
    // EFFECTS: Replaces/modifies existing task's action
    public void setTask(String task) {
        EventLog.getInstance().logEvent(new Event("Modified task from: '"
                + this.task
                + "' to '"
                + task
                + "'"));
        this.task = task;
    }

    // REQUIRES: Integer importance is > 0 < 10
    // MODIFIES: this importance
    // EFFECTS: Sets new value of Task importance to any given integer
    //          (being that the out-of-bounds checks happen in a different function within the ui)
    public void setImportance(int importance) {
        EventLog.getInstance().logEvent(new Event("Modified task importance from: '"
                + this.importance
                + "' to '"
                + importance
                + "'"));
        this.importance = importance;
    }

    // REQUIRES: String s's new value to be in the format YEAR-MM-DD
    // MODIFIES: this
    // EFFECTS: Sets new value of Task due date to given string
    public void setDate(String date) {
        EventLog.getInstance().logEvent(new Event("Modified task date from: '"
                + this.date
                + "' to '"
                + date
                + "'"));
        this.date = date;
    }

    // EFFECTS: return true if no task name
    public Boolean invalidName() {
        return (readTask().equals(""));
    }

    // REQUIRES: date is the correct format (numbers in their appropriate positions)
    // EFFECTS: Returns true if year, month, or day in date string is not in valid format or range, else false
    public Boolean dateOutOfBounds() {
        if (date.equals("")) {
            return false;
        } else if (date.length() != 10) {
            return true;
        } else {
            return (yearOutOfBounds() || monthOutOfBounds() || dayOutOfBounds());
        }
    }

    // REQUIRES: date is the correct format (numbers in their appropriate positions)
    // EFFECTS: Returns true if the year is not a 4 digit number representing a year
    public Boolean yearOutOfBounds() {
        String year = date.substring(0,4);
        int yearNum = Integer.parseInt(year);

        return (yearNum < 1000) || (yearNum > 3000);
    }

    // REQUIRES: date is the correct format (numbers in their appropriate positions)
    // EFFECTS: Returns true if month of Tasks due date is within 1 (january) to 12 (december)
    public Boolean monthOutOfBounds() {
        String month = date.substring(5, 7);
        int monthNum = Integer.parseInt(month);

        return (monthNum < 1) || (monthNum > 12);
    }

    // REQUIRES: date is the correct format (numbers in their appropriate positions)
    // EFFECTS: Returns true if day of Tasks due date is within the 1st to the 31st (doesn't change based on month)
    public Boolean dayOutOfBounds() {
        String day = date.substring(8);
        int dayNum = Integer.parseInt(day);

        return (dayNum < 1) || (dayNum > 31);
    }

    // EFFECTS: returns true if importance rating is within 1 and 10
    public Boolean importanceOutOfBounds() {
        // CASES: importance <= 0, importance > 0; importance >= 11, importance <= 11
        return (importance < 1) || (importance > 10);
    }

    // DATA PERSISTENCE:
    // Method Modelled after JsonSerialization Demo
    // Credit Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    /// EFFECTS: converts this into JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("task name", task);
        json.put("date", date);
        json.put("importance", importance);
        return json;
    }
}
