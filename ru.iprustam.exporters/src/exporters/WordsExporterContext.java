package exporters;

import loggerwrapper.LoggerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс для экспорта найденных слов и их повторений в различные источники (консоль, бд, файл...)
 * Представляет собой контекст паттерна Strategy с возможностью добавления нескольких стратегий
 * и поочередного вызова их методов
 */
public class WordsExporterContext {
    private List<WordsExporterStrategy> strategies;
    private Map<String, Long> wordsMap;

    {
        strategies = new ArrayList();
    }

    /**
     * Конструктор
     * @param wordsMap
     *        Map, у которого в ключах слова, в значениях количество повторений слов
     */
    public WordsExporterContext(Map<String, Long> wordsMap) {
        this.wordsMap = wordsMap;
    }

    /**
     * Добавляет источник для экспорта слов и их повторений
     * @param strategy
     *        Источник экспорта слова и повторений
     */
    public void addDestination(WordsExporterStrategy strategy) {
        strategies.add(strategy);
    }

    /**
     * Выполняет обход добавленных слов и их повторений, выполняя отправку каждого слова и повторения
     * во все источники экспорта
     * @throws Exception
     */
    public void send() throws Exception {
        var iterator = wordsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            for (var strategy : strategies) {
                try {
                    strategy.send(entry);
                } catch (Exception e) {
                    LoggerWrapper logger = LoggerWrapper.getInstance();
                    logger.severe("Single word failed to export", e);
                }
            }
        }

        for (var strategy : strategies) {
            strategy.close();
        }
    }
}
