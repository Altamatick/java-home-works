package app;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance = null;
    private final List<String> logs;
    private final DateTimeFormatter formatter;

    private Logger() {
        logs = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = "[" + timestamp + "] " + message;
        logs.add(logEntry);
        System.out.println(logEntry);
    }

    public void printAllLogs() {
        System.out.println("\n--- All Log Messages ---");
        for (String log : logs) {
            System.out.println(log);
        }
        System.out.println("--- End of Logs ---\n");
    }

    public int getLogCount() {
        return logs.size();
    }
}
