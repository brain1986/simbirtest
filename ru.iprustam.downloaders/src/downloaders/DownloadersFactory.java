package downloaders;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

/**
 * Фабрика по созданию загрузчиков, таких как HTTP, FTP, ....
 */
public class DownloadersFactory {

    /**
     * Создает HTTP загрузчик
     * @param srcUrl
     *        URL требуемой HTML страницы
     * @return HTTP загрузчик
     */
    public static Downloader newHTTPDownloader(URI srcUrl) {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(srcUrl)
                .timeout(Duration.ofSeconds(5))
                .build();

        return new HTTPDownloader(httpClient, httpRequest);
    }

    /**
     * Создает FTP загрузчик
     * @param credentials
     *        предполагаемые данные для соединения по FTP
     * @return FTP загрузчик
     */
    public static Downloader newFTPDownloader(String credentials) {
        return new FTPDownloader(credentials);
    }
}
