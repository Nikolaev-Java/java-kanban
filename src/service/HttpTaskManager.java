package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exception.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import server.KVTaskClient;

import java.io.IOException;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    public static final String KEY_TASK = "task";
    public static final String KEY_SUBTASK = "subtask";
    public static final String KEY_EPIC = "epic";
    public static final String KEY_HISTORY = "history";
    private final Gson gson;

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        super(url);
        client = new KVTaskClient(url);
        gson = Managers.getGson();
    }

    @Override
    protected void save() throws ManagerSaveException {
        List<Task> taskList = getTasks();
        List<SubTask> subTaskList = getSubTasks();
        List<Epic> epicList = getEpics();
        List<Task> taskHistory = getHistory();
        try {
            client.put(KEY_TASK, gson.toJson(taskList));
            client.put(KEY_SUBTASK, gson.toJson(subTaskList));
            client.put(KEY_EPIC, gson.toJson(epicList));
            client.put(KEY_HISTORY, gson.toJson(taskHistory));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() throws IOException, InterruptedException {
        String jsonTask = client.load(KEY_TASK);
        String jsonSubTask = client.load(KEY_SUBTASK);
        String jsonEpic = client.load(KEY_EPIC);
        String jsonHistory = client.load(KEY_HISTORY);
        int maxId;
        List<Task> tasks = gson.fromJson(jsonTask, new TypeToken<List<Task>>() {
        }.getType());
        tasks.addAll(gson.fromJson(jsonEpic, new TypeToken<List<Epic>>() {
        }.getType()));
        tasks.addAll(gson.fromJson(jsonSubTask, new TypeToken<List<SubTask>>() {
        }.getType()));
        maxId = tasks.stream()
                .map(Task::getId)
                .max(Integer::compare)
                .orElse(0);
        List<Task> history = gson.fromJson(jsonHistory, new TypeToken<List<Task>>() {
        }.getType());

        tasks.forEach(task -> addTask(task, maxId));
        history.forEach(task -> getHistoryManager().add(task));
    }
}
