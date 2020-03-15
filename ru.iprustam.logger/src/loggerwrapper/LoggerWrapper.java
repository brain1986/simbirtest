package loggerwrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Singleton и обертка для класса java.util.logging.Logger
 */
public class LoggerWrapper {
    private static LoggerWrapper logger;
    private Logger originalLogger;

    private LoggerWrapper() throws IOException {
        originalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        FileHandler fileTxt = new FileHandler("error-log.txt");
        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        originalLogger.addHandler(fileTxt);
        originalLogger.setUseParentHandlers(false);

        originalLogger.setLevel(Level.WARNING);
    }

    /**
     * Инициализирует, либо возвращает объект singleton
     * @return wrapper для класса java.util.logging.Logger
     * @throws IOException
     */
    public static LoggerWrapper getInstance() throws IOException {
        if (logger == null)
            logger = new LoggerWrapper();

        return logger;
    }

    /**
     * wrapper одноименного метода с возможностью добавления данных из StackTrace
     * @param message
     *        сообщение
     * @param e
     *        объект исключения, из которого получим StackTrace
     */
    public void severe(String message, Exception e) {
        originalLogger.severe(messageWithStackTrace(message, e));
    }

    private String messageWithStackTrace(String message, Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        System.out.println(message + "(view log file for details)");
        return message + " " + errors.toString();
    }
}
