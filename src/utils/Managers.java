package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.FileBackedTasksManager;
import service.HistoryManager;
import service.HttpTaskManager;
import service.InMemoryHistoryManager;
import service.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFiledBackedManager() {
        return new FileBackedTasksManager("src/resources/data.csv");
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
    }

}
