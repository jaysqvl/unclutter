package ui;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                new ToDoApp();
                Runtime.getRuntime().addShutdownHook(new Thread(PrintLog::printLog));
            } catch (FileNotFoundException e) {
                System.out.println("Unable to run application: file not found");
            } catch (IOException e) {
                System.out.println("Unable to find Splash Screen Image: file not found");
            }
        });
    }
}