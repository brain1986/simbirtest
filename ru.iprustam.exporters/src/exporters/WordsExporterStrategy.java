package exporters;

import java.util.Map;

public interface WordsExporterStrategy extends AutoCloseable {
    void send(Map.Entry<String, Long> entry) throws Exception;
}
