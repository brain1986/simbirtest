package exporters;

import java.util.Map;

/**
 * Выполняет отправку слова и повторения в консоль
 */
public class WordsToConsole implements WordsExporterStrategy {

    /**
     * Отправляет слово и повторения в консоль
     * @param entry
     *        Ключ - слово, значение - количество повторений
     */
    @Override
    public void send(Map.Entry<String, Long> entry) {
        System.out.printf("Word %s matches %d times%n", entry.getKey(), entry.getValue());
    }

    /**
     * Закрытие ресурса источника
     */
    @Override
    public void close() {
    }
}
