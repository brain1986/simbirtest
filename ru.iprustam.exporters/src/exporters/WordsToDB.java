package exporters;

import db.DB;

import java.util.List;
import java.util.Map;

/**
 * Выполняет отправку слова и повторения в БД
 */
public class WordsToDB implements WordsExporterStrategy {

    private DB db;

    /**
     * Конструктор
     * @param db
     *        объект БД
     * @throws Exception
     */
    public WordsToDB(DB db) throws Exception {
        this.db = db;
        this.db.connect("", "");
        this.db.query("CREATE TABLE IF NOT EXISTS words (word VARCHAR(256), wordnum INTEGER )");
        this.db.delete("words", "true");
    }

    /**
     * Отправляет слово и повторения в БД
     * @param entry
     *        Ключ - слово, значение - количество повторений
     */
    @Override
    public void send(Map.Entry<String, Long> entry) throws Exception {
        db.insert("words", List.of(entry.getKey(), entry.getValue()));
    }

    /**
     * Закрытие ресурса источника
     */
    @Override
    public void close() throws Exception {
        db.close();
    }
}
