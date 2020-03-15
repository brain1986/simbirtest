package main;

import db.JdbcDB;
import downloaders.Downloader;
import downloaders.DownloadersFactory;
import exporters.WordsExporterContext;
import exporters.WordsToConsole;
import exporters.WordsToDB;
import loggerwrapper.LoggerWrapper;
import parsers.FileParser;
import parsers.HTMLParser;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.*;

/**
 * Точка входа приложения
 */
public class Main {

    private String srcURL;
    private String downloadedFileName;
    private String sqlConnectionString;

    {
        srcURL = "https://www.simbirsoft.com/";
        downloadedFileName = "data.htm";
        sqlConnectionString = "jdbc:h2:file:./database.h2";
    }

    /**
     * Обеспечивает рабочий цикл программы, обрадатывает все исключения программы
     */
    public void workFlow() {

        LoggerWrapper logger;
        try {
            logger = LoggerWrapper.getInstance();
        } catch (IOException e) {
            System.out.println("Logger initialization error");
            e.printStackTrace();
            return;
        }

        System.out.printf("Enter URL (current is %s): ", srcURL);
        Scanner scanner = new Scanner(System.in);
        String srcInput = scanner.nextLine();
        if(!srcInput.trim().isEmpty())
            srcURL = srcInput;

        Path path = null;
        try {
            path = download();
        } catch (Exception e) {
            logger.severe("Content download error \n", e);
            return;
        }

        Map<String, Long> wordsMap;
        try {
            wordsMap = parse(path);
        } catch (Exception e) {
            logger.severe("Parsing file error \n", e);
            return;
        }

        try {
            printAndSave(wordsMap);
        } catch (Exception e) {
            logger.severe("Results export error \n", e);
            return;
        }

        System.out.printf("Completed, found: %s words%n", wordsMap.size());
    }

    /**
     * Инициализирует объекты для загрузки файла по HTTP
     * @return дескриптор к файлу с загруженными данными
     * @throws Exception
     */
    public Path download() throws Exception {
        URI src = new URI(srcURL);
        Path dst = Paths.get(downloadedFileName);
        Downloader downloader = DownloadersFactory.newHTTPDownloader(src);
        return downloader.downloadToFile(dst);
    }

    /**
     * Инициализирует класс, подсчитывающий повторения слов и передает ему доступ к загруженному файлу
     * @param path
     *        Дескриптор к файлу с загруженными данными
     * @return Map, у которого в ключах слова, в значениях количество повторений слов
     * @throws Exception
     */
    public Map<String, Long> parse(Path path) throws Exception {
        FileParser fileParser = new HTMLParser();
        var result = fileParser.parseTemplateMethod(path);
        return result;
    }

    /**
     * Создает объект, предназначенный для экспорта полученных слов в различные источники вывода
     * (в данном случае консоль и БД)
     * @param wordsMap
     *        Map, у которого в ключах слова, в значениях количество повторений слов
     * @throws Exception
     */
    public void printAndSave(Map<String, Long> wordsMap) throws Exception {
        WordsExporterContext exporter = new WordsExporterContext(wordsMap);
        exporter.addDestination(new WordsToConsole());
        exporter.addDestination(new WordsToDB(new JdbcDB(sqlConnectionString)));
        exporter.send();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.workFlow();
    }
}
