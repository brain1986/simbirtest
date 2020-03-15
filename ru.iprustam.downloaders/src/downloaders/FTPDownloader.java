package downloaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * FTP загрузчик
 */
public class FTPDownloader implements Downloader {

    protected FTPDownloader(String credentials) {
    }

    /**
     * Загружает в требуемый файл
     * @param dstPath
     *        дескриптор файла, в который будем загружать содержимое
     * @return дескриптор файла, в который будем загружать содержимое
     * @throws IOException
     */
    @Override
    public Path downloadToFile(Path dstPath) throws IOException {
        return Files.writeString(dstPath, "FTP FTP", StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
