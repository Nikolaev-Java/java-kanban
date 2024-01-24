package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import exception.ManagerCreateException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskTypes;
import service.TaskManager;
import utils.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final Gson gson;
    private final TaskManager taskManager;
    private final HttpServer httpServer;


    public HttpTaskServer() throws IOException, InterruptedException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        gson = Managers.getGson();
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks/", this::getPrioritizedTasks);
        httpServer.createContext("/tasks/task", this::handlerTask);
        httpServer.createContext("/tasks/epic", this::handlerTask);
        httpServer.createContext("/tasks/subtask", this::handlerTask);
        httpServer.createContext("/tasks/history", this::getHistory);
        httpServer.createContext("/tasks/subtask/epic", this::getSubTaskInEpic);
    }

    private void getSubTaskInEpic(HttpExchange httpExchange) {
        int id = parseQueryUriToId(httpExchange.getRequestURI());
        String methodRequest = httpExchange.getRequestMethod();
        try {
            if (!methodRequest.equals("GET")) {
                System.out.println("неверный метод для получения SubTask в Epic");
                httpExchange.sendResponseHeaders(405, 0);
                return;
            }
            if (id == -1) {
                System.out.println("Не указан параметр");
                httpExchange.sendResponseHeaders(405, 0);
                return;
            }
            String response = gson.toJson(taskManager.getSubTaskInEpic(id));
            if (response.equals("[]")) {
                System.out.println("id не верный");
                httpExchange.sendResponseHeaders(404, 0);
                return;
            }
            sendResponse(httpExchange, response);
        } catch (IOException e) {

        } finally {
            httpExchange.close();
        }
    }

    private void getHistory(HttpExchange httpExchange) {
        String methodRequest = httpExchange.getRequestMethod();
        try {
            if (!methodRequest.equals("GET")) {
                System.out.println("неверный метод для получения истории");
                httpExchange.sendResponseHeaders(405, 0);
                return;
            }
            String response = gson.toJson(taskManager.getHistory());
            sendResponse(httpExchange, response);
        } catch (IOException e) {

        } finally {
            httpExchange.close();
        }
    }

    private void handlerTask(HttpExchange httpExchange) {
        try {
            int id = parseQueryUriToId(httpExchange.getRequestURI());
            TaskTypes taskType = TaskTypes.valueOf(httpExchange.getRequestURI().getPath().split("/")[2].toUpperCase());
            String methodRequest = httpExchange.getRequestMethod();
            switch (methodRequest) {
                case "GET": {
                    String response;
                    if (id != -1) {
                        response = gson.toJson(getTaskById(taskType, id));
                    } else {
                        response = gson.toJson(getTasks(taskType));
                    }
                    if (response.equals("null")) {
                        httpExchange.sendResponseHeaders(404, 0);
                        return;
                    }
                    sendResponse(httpExchange, response);
                    return;
                }
                case "POST": {
                    String request = readRequest(httpExchange);
                    if (request.isEmpty()) {
                        System.out.println("Тело запроса пустое");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    Task task = taskFromJson(taskType, request);
                    try {
                        if (id != -1) {
                            task.setId(id);
                            if (!updateTask(taskType, task)) {
                                httpExchange.sendResponseHeaders(418, 0);
                                return;
                            }
                        } else {
                            createTask(taskType, task);
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                    } catch (ManagerCreateException exception) {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                    return;
                }
                case "DELETE": {
                    if (id != -1) {
                        if (!deleteTaskById(taskType, id)) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                    } else {
                        deleteTask(taskType);
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
                default: {
                    System.out.println("не верный метод для Task");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void getPrioritizedTasks(HttpExchange httpExchange) {
        String methodRequest = httpExchange.getRequestMethod();
        try {
            if (!methodRequest.equals("GET")) {
                System.out.println("Не верный метод для получения приоритетного списка задач");
                httpExchange.sendResponseHeaders(405, 0);
                return;
            }
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            sendResponse(httpExchange, response);
        } catch (IOException e) {

        } finally {
            httpExchange.close();
        }

    }

    public void start() {
        System.out.println("Запуск сервера");
        System.out.println("http://localhost:" + PORT);
        httpServer.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен");
        httpServer.stop(0);
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String readRequest(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes());
    }

    private int parseQueryUriToId(URI uri) {
        String query = uri.getQuery();
        if (query == null) {
            return -1;
        }
        return Integer.parseInt(query.split("=")[1]);
    }

    private Task getTaskById(TaskTypes taskType, int id) {
        switch (taskType) {
            case TASK: {
                return taskManager.getTaskById(id);
            }
            case EPIC: {
                return taskManager.getEpicById(id);
            }
            case SUBTASK: {
                return taskManager.getSubTaskById(id);
            }
            default: {
                return null;
            }
        }
    }

    private List<? extends Task> getTasks(TaskTypes taskType) {
        switch (taskType) {
            case TASK: {
                return taskManager.getTasks();
            }
            case EPIC: {
                return taskManager.getEpics();
            }
            case SUBTASK: {
                return taskManager.getSubTasks();
            }
            default: {
                return null;
            }
        }
    }

    private int createTask(TaskTypes taskType, Task task) {
        switch (taskType) {
            case TASK: {
                return taskManager.createTask(task);
            }
            case EPIC: {
                return taskManager.createEpic((Epic) task);
            }
            case SUBTASK: {
                return taskManager.createSubTask((SubTask) task);
            }
            default: {
                return 0;
            }
        }
    }

    private boolean deleteTaskById(TaskTypes taskType, int id) {
        switch (taskType) {
            case TASK: {
                return taskManager.deleteTaskById(id);
            }
            case EPIC: {
                return taskManager.deleteEpicById(id);
            }
            case SUBTASK: {
                return taskManager.deleteSubTaskById(id);
            }
            default: {
                return false;
            }
        }
    }

    private void deleteTask(TaskTypes taskType) {
        switch (taskType) {
            case TASK: {
                taskManager.deleteAllTasks();
                break;
            }
            case EPIC: {
                taskManager.deleteAllEpics();
                break;
            }
            case SUBTASK: {
                taskManager.deleteAllSubTasks();
                break;
            }
        }
    }

    private boolean updateTask(TaskTypes taskType, Task task) {
        switch (taskType) {
            case TASK: {
                return taskManager.updateTask(task);
            }
            case EPIC: {
                return taskManager.updateEpic((Epic) task);
            }
            case SUBTASK: {
                return taskManager.updateSubTask((SubTask) task);
            }
            default: {
                return false;
            }
        }
    }

    private Task taskFromJson(TaskTypes taskType, String request) {
        switch (taskType) {
            case TASK: {
                return gson.fromJson(request, Task.class);
            }
            case EPIC: {
                return gson.fromJson(request, Epic.class);
            }
            case SUBTASK: {
                return gson.fromJson(request, SubTask.class);
            }
            default: {
                return null;
            }
        }
    }
}
