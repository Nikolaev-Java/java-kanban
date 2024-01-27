package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private HttpTaskManager httpTaskManager;
    private HttpTaskManager httpTaskManagerLoad;
    private KVServer kvServer;

    @BeforeEach
    void initHttpTaskManager() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskManager = (HttpTaskManager) Managers.getDefault();
        setDefault(httpTaskManager);
        httpTaskManagerLoad = (HttpTaskManager) Managers.getDefault();
        httpTaskManagerLoad.load();
    }

    @AfterEach
    void stopServer() {
        kvServer.stop();
    }

    @Test
    void shouldLoadTasks() {
        assertNotNull(httpTaskManagerLoad.getTasks());
        assertNotNull(httpTaskManagerLoad.getEpics());
        assertNotNull(httpTaskManagerLoad.getSubTasks());
        assertNotNull(httpTaskManagerLoad.getHistory());
    }

    @Test
    void shouldEqualsTasksLoadManagerThisManager() {
        List<Task> expectedListTask = httpTaskManager.getTasks();
        List<Epic> expectedListEpic = httpTaskManager.getEpics();
        List<SubTask> expectedListSubTask = httpTaskManager.getSubTasks();
        List<Task> expectedListHistory = httpTaskManager.getHistory();
        List<Task> expectedPrioritizedTask = httpTaskManager.getPrioritizedTasks();
        assertEquals(expectedListTask, httpTaskManagerLoad.getTasks());
        assertEquals(expectedListEpic, httpTaskManagerLoad.getEpics());
        assertEquals(expectedListSubTask, httpTaskManagerLoad.getSubTasks());
        assertEquals(expectedListHistory, httpTaskManagerLoad.getHistory());
        assertEquals(expectedPrioritizedTask, httpTaskManagerLoad.getPrioritizedTasks());
    }
}