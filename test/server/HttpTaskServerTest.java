package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;
import service.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private HttpClient client;

    private TaskManager taskManager;
    private final Gson gson = Managers.getGson();

    @BeforeEach
    void init() throws IOException, InterruptedException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        Task expectedTask = new Task("task", "desc", StatusOfTasks.NEW,
                10, LocalDateTime.of(2023, 01, 01, 15, 00));
        int idTask = taskManager.createTask(expectedTask);
        List<Task> expectedListTask = List.of(expectedTask);
        URI url = URI.create(EndPointConst.TASK_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(200, response.statusCode());
        assertEquals(expectedListTask, taskList);
        url = URI.create(EndPointConst.TASK_ID_END_POINT + idTask);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals(expectedTask, task);
        url = URI.create(EndPointConst.TASK_ID_END_POINT + 2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        URI url = URI.create(EndPointConst.TASK_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteByIDTasks() throws IOException, InterruptedException {
        Task expectedTask = new Task("task", "desc", StatusOfTasks.NEW,
                10, LocalDateTime.of(2023, 01, 01, 15, 00));
        int idTask = taskManager.createTask(expectedTask);
        URI url = URI.create(EndPointConst.TASK_ID_END_POINT + idTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        url = URI.create(EndPointConst.TASK_ID_END_POINT + 5);
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        String jsonTask = gson.toJson(new Task("task-01", "desc", StatusOfTasks.NEW, 10));
        URI url = URI.create(EndPointConst.TASK_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        jsonTask = "";
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(400, response.statusCode());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task expectedTask = new Task("task", "desc", StatusOfTasks.NEW,
                10, LocalDateTime.of(2023, 01, 01, 15, 00));
        int idTask = taskManager.createTask(expectedTask);
        String jsonTask = gson.toJson(expectedTask);
        URI url = URI.create(EndPointConst.TASK_ID_END_POINT + idTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        jsonTask = "";
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(400, response.statusCode());
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        List<Epic> expectedListEpic = List.of(expectedEpic);
        URI url = URI.create(EndPointConst.EPIC_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> epicList = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType());
        assertEquals(200, response.statusCode());
        assertEquals(expectedListEpic, epicList);
        url = URI.create(EndPointConst.EPIC_ID_END_POINT + idEpic);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response.body(), Epic.class);
        assertEquals(expectedEpic, epic);
        url = URI.create(EndPointConst.EPIC_ID_END_POINT + 3);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void deleteEpics() throws IOException, InterruptedException {
        URI url = URI.create(EndPointConst.EPIC_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteByIDEpic() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        URI url = URI.create(EndPointConst.EPIC_ID_END_POINT + idEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        url = URI.create(EndPointConst.EPIC_ID_END_POINT + 5);
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void addEpic() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        String jsonTask = gson.toJson(expectedEpic);
        System.out.println(jsonTask);
        URI url = URI.create(EndPointConst.EPIC_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        jsonTask = "";
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(400, response.statusCode());
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        String jsonTask = gson.toJson(expectedEpic);
        URI url = URI.create(EndPointConst.EPIC_ID_END_POINT + idEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        jsonTask = "";
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(400, response.statusCode());
    }

    @Test
    void getSubTask() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        SubTask expectedSubTask = new SubTask("subTask", "desc", StatusOfTasks.NEW, idEpic,
                10, LocalDateTime.of(2023, 01, 02, 15, 00));
        int idSubTask = taskManager.createSubTask(expectedSubTask);
        List<Task> expectedListSubTask = List.of(expectedSubTask);
        URI url = URI.create(EndPointConst.SUBTASK_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTaskList = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(200, response.statusCode());
        assertEquals(expectedListSubTask, subTaskList);
        url = URI.create(EndPointConst.SUBTASK_ID_END_POINT + idSubTask);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(expectedSubTask, subTask);
        url = URI.create(EndPointConst.SUBTASK_ID_END_POINT + 4);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void addSubTask() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        String jsonTask = gson.toJson(new SubTask("subTask-02", "desc", StatusOfTasks.NEW,
                idEpic, 10));
        URI url = URI.create(EndPointConst.SUBTASK_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        jsonTask = "";
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(400, response.statusCode());
    }

    @Test
    void deleteSubTasks() throws IOException, InterruptedException {
        URI url = URI.create(EndPointConst.SUBTASK_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteByIDSubTasks() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        SubTask expectedSubTask = new SubTask("subTask", "desc", StatusOfTasks.NEW, idEpic,
                10, LocalDateTime.of(2023, 01, 02, 15, 00));
        int idSubTask = taskManager.createSubTask(expectedSubTask);
        URI url = URI.create(EndPointConst.SUBTASK_ID_END_POINT + idSubTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        url = URI.create(EndPointConst.SUBTASK_ID_END_POINT + 5);
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void updateSubTask() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        SubTask expectedSubTask = new SubTask("subTask", "desc", StatusOfTasks.NEW, idEpic,
                10, LocalDateTime.of(2023, 01, 02, 15, 00));
        int idSubTask = taskManager.createSubTask(expectedSubTask);
        String jsonTask = gson.toJson(expectedSubTask);
        URI url = URI.create(EndPointConst.SUBTASK_ID_END_POINT + idSubTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        jsonTask = "";
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(jsonTask.getBytes()))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(400, response.statusCode());
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task expectedTask = new Task("task", "desc", StatusOfTasks.NEW,
                10, LocalDateTime.of(2023, 01, 01, 15, 00));
        int idTask = taskManager.createTask(expectedTask);
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        SubTask expectedSubTask = new SubTask("subTask", "desc", StatusOfTasks.NEW, idEpic,
                10, LocalDateTime.of(2023, 01, 02, 15, 00));
        int idSubTask = taskManager.createSubTask(expectedSubTask);
        taskManager.getEpicById(idEpic);
        taskManager.getTaskById(idTask);
        taskManager.getSubTaskById(idSubTask);
        URI url = URI.create(EndPointConst.HISTORY_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
    }

    @Test
    void getPrioritizedTask() throws IOException, InterruptedException {
        URI url = URI.create(EndPointConst.PRIORITIZED_END_POINT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
    }

    @Test
    void getSubTaskInEpic() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("epic", "desc");
        int idEpic = taskManager.createEpic(expectedEpic);
        SubTask expectedSubTask = new SubTask("subTask", "desc", StatusOfTasks.NEW, idEpic,
                10, LocalDateTime.of(2023, 01, 02, 15, 00));
        int idSubTask = taskManager.createSubTask(expectedSubTask);
        URI url = URI.create(EndPointConst.SUBTASK_IN_EPIC_END_POINT + idEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
        url = URI.create(EndPointConst.SUBTASK_IN_EPIC_END_POINT + 5);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}