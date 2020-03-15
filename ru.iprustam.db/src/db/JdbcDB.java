package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Реализация интерфейса DB под jdbc
 */
public class JdbcDB implements DB {
    private String connectionString;
    private Connection conn;

    /**
     * Конструктор
     * @param connectionString
     *        строка соединения для jdbc, формата jdbc:[driver]:..
     */
    public JdbcDB(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * Выполняет соединение с БД
     * @param user
     * @param password
     * @throws Exception
     */
    @Override
    public void connect(String user, String password) throws Exception {
        conn = DriverManager.getConnection(connectionString, user, password);
    }

    /**
     * Закрытие соединения с БД
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        conn.close();
    }

    /**
     * Добавление записи в БД
     * @param table
     *        название таблицы
     * @param values
     *        список добавляемых значений, в порядке следования столбцов таблицы
     * @param <V>
     * @return количество добавленных строк
     * @throws SQLException
     */
    public <V> int insert(String table, List<V> values) throws SQLException {

        String questionPlaceholder = "?,".repeat(values.size());
        questionPlaceholder = questionPlaceholder.substring(0, questionPlaceholder.length()-1);

        String query = "INSERT INTO "
                .concat(table)
                .concat(" VALUES (")
                .concat(questionPlaceholder)
                .concat(")");

        try (var statement = conn.prepareStatement(query)) {
            for(int i = 1; i <= values.size(); i++) {
                statement.setObject(i, values.get(i-1));
            }
            return statement.executeUpdate();
        }
    }

    /**
     * Выполняет удаление записи
     * @param table
     *        название таблицы
     * @param condition
     *        условие удаления, которое следует после ключевого слова WHERE в sql запросе
     * @return количество удаленных строк
     * @throws SQLException
     */
    @Override
    public int delete(String table, String condition) throws SQLException {
        String query = "DELETE FROM "
                .concat(table)
                .concat(" WHERE ").concat(condition);
        try (var statement = conn.prepareStatement(query)) {
            return statement.executeUpdate();
        }
    }

    @Override
    public void query(String query) throws Exception {
        try (var statement = conn.prepareStatement(query)) {
            statement.execute();
        }
    }
}
