package db;

import java.util.List;

/**
 * Интерфейс для реляционных баз данных
 */
public interface DB {
    void connect(String user, String password) throws Exception;
    void close() throws Exception;
    <V> int insert(String table, List<V> values) throws Exception;
    int delete(String table, String condition) throws Exception;
    void query(String query) throws Exception;
}
