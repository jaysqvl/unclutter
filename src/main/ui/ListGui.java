package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import model.List;
import model.Task;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Represents a selected Lists modifying/viewing gui
public class ListGui extends JFrame implements ActionListener, ListSelectionListener {

    private final JFrame frame;
    private final List list;
    private static final String LOGO_ICON = "./data/logo.png";

    private JTable taskTable;
    private DefaultTableModel taskTableModel;

    private JFrame taskFrame;

    private JButton addTaskButton;
    private JButton removeTaskButton;
    private JButton returnHomeButton;
    private JButton modifyTaskButton;

    private JMenuItem sortImportance;
    private JMenuItem sortDate;
    private JMenuItem clearButton;

    // CONSTRUCTOR
    // EFFECTS: Constructs a new JFrame GUI view of passed in list and previous frame so that it may return as needed
    public ListGui(List list, JFrame frame) throws IOException {
        this.list = list; // Represents the list to view and modify
        this.frame = frame; // Represents the previous frame

        initJFrame();

        taskFrame.pack();
        taskFrame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initialize JFrame task view
    public void initJFrame() throws IOException {
        BufferedImage logo = ImageIO.read(new File(LOGO_ICON));

        // Initialize frame
        taskFrame = new JFrame("Viewing List: " + list.getListName());
        taskFrame.setIconImage(logo);
        taskFrame.setVisible(true);
        taskFrame.setResizable(false);
        taskFrame.setLocationRelativeTo(null);
        taskFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane taskScrollPane = new JScrollPane(initTaskJTable());

        taskFrame.add(initJMenuBar(), BorderLayout.PAGE_START); // Add JMenuBar
        taskFrame.add(taskScrollPane, BorderLayout.CENTER);
        taskFrame.add(initButtonPane(), BorderLayout.PAGE_END);
    }

    // MODIFIES: this
    // EFFECTS: Creates an unmodifiable JTable for tasks, fill the table, and return it
    public JTable initTaskJTable() {
        String[] columnNames = {"Task", "Date (YEAR/MM//DD)", "Importance"};

        taskTableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(taskTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel selectionModel = taskTable.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removeTaskButton.setEnabled(taskTable.getSelectedRow() != -1);
                modifyTaskButton.setEnabled(taskTable.getSelectedRow() != -1);
            }
        });

        updateTableModel();

        return taskTable;
    }

    // MODIFIES: this
    // EFFECTS: Creates buttonPane to be added to listFrame
    public JPanel initButtonPane() {
        addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(this);

        removeTaskButton = new JButton("Remove Task");
        removeTaskButton.addActionListener(this);
        if (list.getTasks().size() == 0 || taskTable.getSelectedRow() == -1) {
            removeTaskButton.setEnabled(false);
        }

        modifyTaskButton = new JButton("Modify Task");
        modifyTaskButton.addActionListener(this);
        if (list.getTasks().size() == 0 || taskTable.getSelectedRow() == -1) {
            modifyTaskButton.setEnabled(false);
        }

        returnHomeButton = new JButton("Return");
        returnHomeButton.addActionListener(this);

        JPanel buttonPane = new JPanel();
        buttonPane.add(addTaskButton);
        buttonPane.add(removeTaskButton);
        buttonPane.add(modifyTaskButton);
        buttonPane.add(returnHomeButton);

        return buttonPane;
    }

    // MODIFIES: this
    // EFFECTS: Creates JMenuBar with two menu items for sorting and clearing
    public JMenuBar initJMenuBar() {
        // Initialize JMenuBar and JMenu for sorting
        JMenuBar sortBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        sortBar.add(fileMenu);

        clearButton = new JMenuItem("Clear Tasks");
        clearButton.addActionListener(this);

        fileMenu.add(clearButton);

        JMenu sortMenu = new JMenu("Sort by");
        sortBar.add(sortMenu);

        //Initialize Menu Buttons for sorting
        sortImportance = new JMenuItem("Sort by importance");
        sortImportance.addActionListener(this);
        sortDate = new JMenuItem("Sort by date");
        sortDate.addActionListener(this);

        sortMenu.add(sortImportance);
        sortMenu.add(sortDate);

        return sortBar;
    }

    // MODIFIES: this
    // EFFECTS: Fills the table model with all elements in the current lists taskList
    public void updateTableModel() {
        Object[] rowData = new Object[3];
        for (int k = 0; k < list.getTasks().size(); k++) {
            Task currentTask = list.getTasks().get(k);
            rowData[0] = currentTask.readTask();
            rowData[1] = currentTask.readDate();
            rowData[2] = currentTask.readImportance();
            taskTableModel.addRow(rowData);
        }
    }

    // MODIFIES: this
    // EFFECTS: clears taskTableModel to clear the list display
    public void clearTableModel() {
        for (int k = list.getTasks().size() - 1; k >= 0; k--) {
            taskTableModel.removeRow(k);
        }
    }

    // MODIFIES: this
    // EFFECTS: inserts object in taskTableModel given input values
    public void insertTaskTableModel(String taskName, String taskDate, int taskImportance, int index) {
        // Create object
        Object[] rowData = new Object[3];
        rowData[0] = taskName;
        rowData[1] = taskDate;
        rowData[2] = taskImportance;

        // Insert to table model
        if (index > list.getTasks().size()) {
            taskTableModel.addRow(rowData);
        } else {
            taskTableModel.insertRow(Math.max(index, 0), rowData);
        }
    }

    // MODIFIES: this, toDo.this
    // EFFECTS: Adds new task given user input gui to listTaskModel as Object[] and list as a task
    public void addTaskAction(int index) {
        if (index == -1) { // no selection made
            index = 0;
        } else {
            index++;
        }

        Task validTask = getValidTask();

        if (validTask == null) {
            return;
        } else {
            list.insertTask(validTask, index);

            insertTaskTableModel(validTask.readTask(), validTask.readDate(), validTask.readImportance(), index);

            // Enable the remove and modify button if they were off due to not having any elements
            removeTaskButton.setEnabled(true);
            modifyTaskButton.setEnabled(true);
        }
    }

    // MODIFIES: this, toDo.this
    // EFFECTS: if list size == 0, disabled modifyTaskButton and removeTaskButton, else remove item from the appropriate
    //               given index from taskTableModel and current list
    public void removeTaskAction(int index) {
        int size = list.getTasks().size();

        if (size == 0) {
            removeTaskButton.setEnabled(false);
            modifyTaskButton.setEnabled(false);
        } else {
            int removedIndex = list.removeTask(index);
            taskTableModel.removeRow(removedIndex);
        }
    }

    // MODIFIES: this, toDo.this
    // EFFECTS: if list size == 0, disabled modifyTaskButton and removeTaskButton, else modify item from the appropriate
    //               given index from taskTableModel and current list using user input
    public void modifyTaskAction(int index) {
        int size = list.getTasks().size();

        if (size == 0) { // no selection was made
            modifyTaskButton.setEnabled(false);
            removeTaskButton.setEnabled(false);
        } else {
            Task validTask = getValidTask();
            Task currentTask = list.getTasks().get(index);

            if (validTask == null) {
                return;
            } else {
                currentTask.setTask(validTask.readTask());
                currentTask.setDate(validTask.readDate());
                currentTask.setImportance(validTask.readImportance());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Takes in 3 user inputs for taskName, taskDate, and taskImportance and creates a valid task
    public Task getValidTask() {
        return new Task(getValidTaskString(), getValidDateString(), getValidImportanceInt());
    }

    public String getValidTaskString() {
        Task testTaskName;
        String taskName;
        do {
            taskName = UserInput.getTaskInput("Enter Task");
            if (taskName == null) {
                return null; // Handles cancel button;
            }
            testTaskName = new Task(taskName, "", 1);
        } while (testTaskName.invalidName() || list.containsTask(taskName));
        return taskName;
    }

    public String getValidDateString() {
        String taskDate;
        Task testTaskDate;
        do {
            taskDate = UserInput.getDateInput("Enter Date (Format: 'YEAR/MM/DD')");
            if (taskDate == null) {
                return null; // Handles cancel button;
            }
            testTaskDate = new Task("Test", taskDate, 1);
        } while (testTaskDate.dateOutOfBounds());
        return taskDate;
    }

    public Integer getValidImportanceInt() {
        Integer taskImportance;
        Task testTaskImportance;
        do {
            taskImportance = UserInput.getImportanceInput("Enter Importance (Scale: 1 - 10)");
            if (taskImportance == null) {
                return null; // Handles cancel button;
            }
            testTaskImportance = new Task("Test", "", taskImportance);
        } while (testTaskImportance.importanceOutOfBounds());
        return taskImportance;
    }

    // MODIFIES: this
    // EFFECTS: Enables removeTaskButton and modifyTaskButton if the current taskTable selected row is valid
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            removeTaskButton.setEnabled(taskTable.getSelectedRow() != -1);
            modifyTaskButton.setEnabled(taskTable.getSelectedRow() != -1);
        }
    }


    // EFFECTS: ActionListener implemented which directs the button pressed to the appropriate action.
    //              Clears and updates TableModel as appropriate
    @Override
    public void actionPerformed(ActionEvent e) {
        int index = taskTable.getSelectedRow();

        if (e.getSource() == addTaskButton) {
            addTaskAction(index);
        } else if (e.getSource() == removeTaskButton) {
            removeTaskAction(index);
        } else if (e.getSource() == returnHomeButton) {
            returnToMain();
        } else if (e.getSource() == modifyTaskButton) {
            modifyTaskAction(index);
            clearTableModel();
            updateTableModel();
        } else if (e.getSource() == sortImportance) {
            list.sortDate();
            clearTableModel();
            updateTableModel();
        } else if (e.getSource() == sortDate) {
            list.sortImportance();
            clearTableModel();
            updateTableModel();
        } else if (e.getSource() == clearButton) {
            clearTableModel();
            list.removeAll();
        }
    }

    // MODIFIES: this
    // EFFECTS: disables current list view frame and reopens the home screen of ListOfLists
    public void returnToMain() {
        taskFrame.dispose();
        frame.setVisible(true);
    }
}