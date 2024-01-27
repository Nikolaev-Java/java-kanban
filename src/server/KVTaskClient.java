package server;

import exception.TaskClientException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        client = HttpClient.newHttpClient();
        this.url = url;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register"))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Ошибка при регистрации",e);
        }
        apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(json.getBytes()))
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() != 200) {
            throw new TaskClientException("Сохранение не удалось. Код ошибки - " + response.statusCode());
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new TaskClientException("Загрузить не удалось. Код ошибки - " + response.statusCode());
        }
        return response.body();
    }
}
