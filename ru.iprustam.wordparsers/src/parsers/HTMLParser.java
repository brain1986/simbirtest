package parsers;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Построчный HTML парсер слов, с удалением тегов из содержимого и
 * разбиением содержимого на отдельные слова
 * Удаляет открытые, но не закрытые теги из строки
 * Удаляет закрытые, но не открытые теги из строки
 * Если в текущей строке обнаруживается не закрытый тег, то все,
 * что будет в данной и последующих строках будет проигнорировано, до
 * тех пор пока не будет найден символ закрытия тега
 * !!! Не удаляет содержимое между тегами <script></script>
 */
public class HTMLParser extends FileParser {

    private boolean tagNotClosed;

    @Override
    protected List<String> splitWords(String line) {
        List<String> result = new LinkedList<>();
        Pattern reg;
        Matcher m;

        if (tagNotClosed) {
            tagNotClosed = !line.contains(">");
            if (tagNotClosed)
                return result;
        }

        String filteredLine = line.replaceAll("(\\<.*?\\>)|([^\\<]*\\>)|(\\<[^\\>]*)", "");  //

        reg = Pattern.compile("[^\\s,\\.!\\?\\\";:\\[\\]\\(\\)\\r\\t]+");
        m = reg.matcher(filteredLine);
        while(m.find())
            result.add(m.group());

        reg = Pattern.compile("\\<[^\\>]*$");
        m = reg.matcher(line);
        tagNotClosed = m.find();

        return result;
    }
}
