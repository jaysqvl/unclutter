package ui;

import model.Event;
import model.EventLog;

public class PrintLog {

    public PrintLog() {
    }

    // EFFECTS: prints all current logs to console right before termination
    public static void printLog()  {
        EventLog el = EventLog.getInstance();
        for (Event next : el) {
            System.out.println(next.toString());
            System.out.println("\n");
        }
    }
}