package downloaders;

import java.nio.file.Path;

/**
 * Интерфейс для предполагаемых загрузчиков из разных источников
 */
public interface Downloader {
    Path downloadToFile(Path dstPath) throws Exception;
}
