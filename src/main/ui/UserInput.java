package ui;

import javax.swing.*;
import java.awt.*;

// Represents JOptionPane input instantiating shortcut class
public class UserInput {

    private static final String LOGO_ICON = "./data/logo.png";
    private static final String TASK_ICON = "./data/task.png";
    private static final String DATE_ICON = "./data/date.png";
    private static final String IMPORTANCE_ICON = "./data/importance.png";

    private static String importanceInput;

    // CONSTRUCTOR
    // Effects: Instantiates a UserInput object whose methods can be referenced to retrieve valid user input
    public UserInput() {

    }

    // EFFECTS: Default JOptionPane input window that prompts the user to enter an integer or return null
    public static java.lang.Integer getInteger(String text) {
        ImageIcon logo = new ImageIcon(LOGO_ICON);
        String s;

        // Ensures that the JOptionPane never reaches an error given a potential null value
        if (text != null) {
            s = (String) JOptionPane.showInputDialog(
                    null,
                    text,
                    "Jay's To Do Application",
                    JOptionPane.INFORMATION_MESSAGE,
                    logo, null, "");
        } else {
            return null;
        }

        if (s == null) { // If cancel button is pressed
            return null;
        } else if (s.matches(".*[a-zA-Z]+.*")) { // if not a valid number (contains any member of the alphabet
            return -1;
        } else { // If valid number
            return Integer.parseInt(importanceInput);
        }
    }

    // EFFECTS: Default JOptionPane input window that prompts the user to enter a String or return null if cancelled
    public static String getString(String text) {
        ImageIcon logo = new ImageIcon(LOGO_ICON);

        if (text != null) {
            return (String) JOptionPane.showInputDialog(
                    null,
                    text,
                    "Jay's To Do Application",
                    JOptionPane.INFORMATION_MESSAGE,
                    logo, null, "");
        } else {
            return null;
        }
    }

    // EFFECTS: Creates a JOptionPane that prompts the user to enter an String or return null if cancelled
    public static String getTaskInput(String text) {
        ImageIcon taskIcon = new ImageIcon(TASK_ICON);
        Image iconToImg = taskIcon.getImage();
        Image newImg = iconToImg.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
        taskIcon = new ImageIcon(newImg);

        if (text != null) {
            return (String) JOptionPane.showInputDialog(
                    null,
                    text,
                    "Task: Task selection",
                    JOptionPane.INFORMATION_MESSAGE,
                    taskIcon, null, "");
        } else {
            return null;
        }
    }

    // EFFECTS: Creates a JOptionPane that prompts the user to enter an String or return null if cancelled
    public static String getDateInput(String text) {
        ImageIcon dateIcon = new ImageIcon(DATE_ICON);
        Image iconToImg = dateIcon.getImage();
        Image newImg = iconToImg.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
        dateIcon = new ImageIcon(newImg);

        if (text != null) {
            return (String) JOptionPane.showInputDialog(
                    null,
                    text,
                    "Task: Date selection",
                    JOptionPane.INFORMATION_MESSAGE,
                    dateIcon, null, "");
        } else {
            return null;
        }
    }

    // EFFECTS: Creates a JOptionPane that prompts the user to enter an integer or return 0 if cancelled
    public static java.lang.Integer getImportanceInput(String text) {
        ImageIcon importanceIcon = new ImageIcon(IMPORTANCE_ICON);
        Image iconToImg = importanceIcon.getImage();
        Image newImg = iconToImg.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
        importanceIcon = new ImageIcon(newImg);

        if (text != null) {
            importanceInput = (String) JOptionPane.showInputDialog(
                    null,
                    text,
                    "Task: Importance selection",
                    JOptionPane.INFORMATION_MESSAGE,
                    importanceIcon, null, "");
        }

        // If user inputs correct format, return it as a parsed integer, else if format wrong, return -1, finally,
        //      return null (user cancelled)

        if (importanceInput == null) {
            return null;
        } else if (importanceInput.matches(".*[a-zA-Z]+.*")) {
            return -1;
        } else {
            return Integer.parseInt(importanceInput);
        }
    }
}