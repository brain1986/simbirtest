package downloaders;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class HTTPDownloader implements Downloader {

    private HttpClient httpClient;
    private HttpRequest httpRequest;

    protected HTTPDownloader(HttpClient httpClient, HttpRequest httpRequest) {
        this.httpClient = httpClient;
        this.httpRequest = httpRequest;
    }

    /**
     * Загружает в требуемый файл
     * @param dstPath
     *        дескриптор файла, в который будем загружать содержимое
     * @return дескриптор файла, в который будем загружать содержимое
     * @throws IOException
     */
    @Override
    public Path downloadToFile(Path dstPath) throws IOException, InterruptedException {
        Files.deleteIfExists(dstPath);
        HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofFile(dstPath));
        if(response.statusCode() != 200)
            throw new IOException("HTTP status code was " + response.statusCode());
        return dstPath;
    }
}
