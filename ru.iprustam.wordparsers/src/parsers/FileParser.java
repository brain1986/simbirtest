package parsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Выполняет парсинг и поиск количества совпадений слов из заданного источника
 * Реализация паттерна Template method
 */
public abstract class FileParser {

    /**
     * Шаблонный метод, прозводящий поиск количества совпадений слов
     * @param path
     *        дескриптор файла, по которому производится поиск слов
     * @return Map, у которого в ключах слова, в значениях количество повторений слов
     * @throws IOException
     */
    public Map<String, Long> parseTemplateMethod(Path path) throws IOException {
        Map result = Files.lines(path)
                .flatMap(line -> splitWords(line).stream())
                .collect(groupingBy(v -> v.toLowerCase(), Collectors.counting() ));
        return result;
    }

    protected abstract List<String> splitWords(String line);
}
