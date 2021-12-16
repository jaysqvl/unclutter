package ui;

import model.EventLog;
import model.ToDo;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Instantiates a list of To-do Lists and Runs the UI methods of the program sequentially by command
public class ToDoApp extends JFrame implements ListSelectionListener, ActionListener {

    private ToDo toDo;

    private static final String SPLASH_IMAGE = "./data/logo.gif";
    private static final String LOGO_IMAGE = "./data/logo.png";

    private static final String JSON_STORE = "./data/todoinstance.json";
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JFrame frame;
    private JButton clickImgButton;
    private JButton addButton;
    private JButton removeButton;
    private JButton modifyButton;
    private JMenuItem clearButton;
    private JMenuItem saveButton;
    private JMenuItem loadButton;
    private JMenuItem clearLogsButton;
    private JTextField listNameField;

    private JList<String> listLists;
    private DefaultListModel<String> listListsModel;

    // CONSTRUCTOR
    // EFFECTS: Instantiates new to do instance/starts the GUI. Throws IOException to main
    //                  (encompasses FileNotFoundException)
    public ToDoApp() throws IOException {
        init();

        initFrame();
        // runToDoList(); // console gui activate
    }

    // EFFECTS: instantiates list of to-do lists, scanner, and jsonWriter/jsonReader
    private void init() {
        toDo = new ToDo();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: Instantiates new frame with initial splash screen
    public void initFrame() throws IOException {
        // Initialize frame
        BufferedImage logo = ImageIO.read(new File(LOGO_IMAGE));
        frame = new JFrame("To Do App");
        frame.setIconImage(logo);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Creates splash screen out of button and button action with no border
        Image image = Toolkit.getDefaultToolkit().createImage(SPLASH_IMAGE);
        ImageIcon splashImage = new ImageIcon(image);

        clickImgButton = new JButton(splashImage);
        clickImgButton.addActionListener(this);
        clickImgButton.setBorder(BorderFactory.createEmptyBorder());
        clickImgButton.setContentAreaFilled(false);

        // Add Splash screen button
        frame.add(clickImgButton);

        frame.pack();
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: After clickImgButton is pressed, it is removed and officially starts the program by adding
    //                  JMenuBar, ListPane, and ActionPane to frame
    public void initComponents() {
        // Add all elements to the frame
        frame.remove(clickImgButton);

        frame.add(initJMenuBar(), BorderLayout.PAGE_START); // initializes top menu bar
        frame.add(initListPane(), BorderLayout.CENTER); // initializes listview pane
        frame.add(initActionPane(), BorderLayout.PAGE_END); // initializes button and modify pane

        frame.pack();
    }

    // MODIFIES: this
    // EFFECTS: Returns instantiated JMenuBar with Menu and MenuItems
    public JMenuBar initJMenuBar() {
        // Initialize JMenuBar and JMenu
        JMenuBar saveLoadBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        saveLoadBar.add(fileMenu);

        //Initialize MenuButtons
        clearButton = new JMenuItem("Clear Lists");
        clearButton.addActionListener(this);
        saveButton = new JMenuItem("Save Lists");
        saveButton.addActionListener(this);
        loadButton = new JMenuItem("Load Lists");
        loadButton.addActionListener(this);
        clearLogsButton = new JMenuItem("Clear Logs");
        clearLogsButton.addActionListener(this);

        // Add Menu Buttons to fileMenu
        fileMenu.add(clearLogsButton);
        fileMenu.add(clearButton);
        fileMenu.add(saveButton);
        fileMenu.add(loadButton);

        return saveLoadBar;
    }

    // MODIFIES: this
    // EFFECTS: Returns Instantiated list display gui using JScrollPane and JList
    public JScrollPane initListPane() {
        // Initialize JScrollPane and List Vector Model with Current ToDoList or none
        listListsModel = new DefaultListModel<>();
        initializeListModel(); // populates listModel with current active lists

        listLists = new JList<>(listListsModel); // Adds listListsModel to JList
        listLists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listLists.setSelectedIndex(0);
        listLists.addListSelectionListener(this);
        listLists.setVisibleRowCount(10);

        return new JScrollPane(listLists);
    }

    // EFFECTS: Returns instantiated bottom button/action pane and assigns their functionality and structure
    public JPanel initActionPane() {
        // Initialize Action Buttons and Text Field
        listNameField = new JTextField(20);

        addButton = new JButton("Add List");
        addButton.setEnabled(true);
        addButton.addActionListener(this);

        removeButton = new JButton("Remove List");
        removeButton.setEnabled(false);
        removeButton.addActionListener(this);

        modifyButton = new JButton("View/Modify List");
        modifyButton.setEnabled(false);
        modifyButton.addActionListener(this);

        // Create and add buttons to a JPanel
        JPanel buttonPane = new JPanel();
        buttonPane.add(addButton);
        buttonPane.add(listNameField);
        buttonPane.add(removeButton);
        buttonPane.add(modifyButton);

        return buttonPane;
    }

    // MODIFIES: this
    // EFFECTS: Fills listListModel with the current TaskLists names
    public void initializeListModel() {
        for (int i = 0; i < toDo.numLists(); i++) {
            listListsModel.addElement(toDo.getListOfLists().get(i).getListName());
        }
    }

    // MODIFIES: this, toDo.this
    // EFFECTS: if field empty or name already in list, do nothing
    //              else creates new list in toDoInstance, updates listListModel, clears textField, makes list visible
    //              and enables the removeButton if the button was previously disabled
    public void addButtonAction(int index) {
        String textFieldVal = listNameField.getText();

        // Ensures textField input for new list is valid
        if (textFieldVal.equals("") || alreadyInList(textFieldVal)) {
            Toolkit.getDefaultToolkit().beep();
            listNameField.requestFocusInWindow();
            listNameField.selectAll();
            return;
        }

        if (index == -1) { // no selection made yet
            index = 0;
        } else { // Inserts above the currently selected index
            index++;
        }

        // Add item into the list of lists and then create a new model
        toDo.insertNewList(index, textFieldVal);
        listListsModel.insertElementAt(textFieldVal, index);

        // Reset the text field
        listNameField.requestFocusInWindow();
        listNameField.setText("");

        // Make the new item visible
        listLists.setSelectedIndex(index);
        listLists.ensureIndexIsVisible(index);

        // Enable the remove and modify button if they were off due to not having any elements
        removeButton.setEnabled(true);
        modifyButton.setEnabled(true);
    }

    // EFFECTS: Returns true if given String ListName already exists, false otherwise
    public Boolean alreadyInList(String textFieldVal) {
        for (int k = 0; k < toDo.numLists(); k++) {
            if (toDo.getListOfLists().get(k).getListName().equals(textFieldVal)) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this, toDo.this
    // EFFECTS: if selected index is valid, index of list is removed from listListsModel and toDoInstance
    //              and sets the next possible selected index. if size of list is 0, disables removeButton
    public void removeButtonAction(int index) {
        int size = listListsModel.size();

        if (size == 0) { // Nothing in list, disable firing
            removeButton.setEnabled(false);
        } else { // Select an index
            int removedIndex = toDo.removeList(index); // removes last element
            listListsModel.remove(removedIndex);

            listLists.setSelectedIndex(removedIndex - 1);
            listLists.ensureIndexIsVisible(removedIndex - 1);
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates a new task panel view with buttons for modifying a list.
    public void modifyButtonAction(int index) {
        // Will open a new JPanel with an entirely different panel view and set of buttons
        int size = listListsModel.size();

        if (size == 0) { // Nothing in list, disable firing
            modifyButton.setEnabled(false);
        } else { // Select an index
            frame.setVisible(false);
            try {
                new ListGui(toDo.getListOfLists().get(index), frame); // creates the new frame view
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: If selected index is valid, keeps removeButton and modifyButton on, else off
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            removeButton.setEnabled(listLists.getSelectedIndex() != -1);
            modifyButton.setEnabled(listLists.getSelectedIndex() != -1);
        }
    }

    // MODIFIES: this
    // EFFECTS: ActionListener which directs the button pressed and current selection
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clickImgButton) {
            initComponents();
        } else {
            int index = listLists.getSelectedIndex();
            // Assign each button to an action
            if (e.getSource() == addButton) {
                addButtonAction(index);
            } else if (e.getSource() == removeButton) {
                removeButtonAction(index);
            } else if (e.getSource() == modifyButton) {
                modifyButtonAction(index);
            } else if (e.getSource() == clearButton) {
                listListsModel.clear();
                toDo.removeAll();
                JOptionPane.showMessageDialog(null, "Cleared all lists", "STATUS",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getSource() == saveButton) {
                saveToDo();
            } else if (e.getSource() == loadButton) {
                loadToDo();
            } else if (e.getSource() == clearLogsButton) {
                EventLog.getInstance().clear(); // clears all logs
            }
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
            listListsModel.clear();
            initializeListModel();
            JOptionPane.showMessageDialog(null, "Successfully loaded!", "STATUS",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE,
                    "STATUS", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
