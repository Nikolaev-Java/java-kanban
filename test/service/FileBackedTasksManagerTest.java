package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final String URL = "src/resources/data.csv";
    private FileBackedTasksManager manager;

    @BeforeEach
    void initFileBackedTasksManager() {
        manager = new FileBackedTasksManager(URL);
        super.setDefault(manager);
    }

    @AfterEach
    void deleteFile() throws IOException {
        Files.deleteIfExists(Path.of(URL));
    }

    @Test
    void shouldFileIsFilled() throws IOException {
        List<String> expectedStringList = List.of("id,type,name,status,description,startTime,duration,endTime,epic",
                "1,TASK,Task-01,NEW,Task-01 desc,2023-01-04T15:00,10,2023-01-04T15:10",
                "2,TASK,Task-02,IN_PROGRESS,Task-02 desc,2023-01-03T15:00,10,2023-01-03T15:10",
                "3,EPIC,Epic-01,IN_PROGRESS,Epic-01 desc,2023-01-01T15:00,10,2023-01-02T15:10",
                "4,EPIC,Epic-02,NEW,Epic-02 desc,null,0,null",
                "5,SUBTASK,SubTask-01,NEW,SubTask-01 desc,2023-01-01T15:00,10,2023-01-01T15:10,3",
                "6,SUBTASK,SubTask-02,DONE,SubTask-02 desc,2023-01-02T15:00,10,2023-01-02T15:10,3",
                "");
        List<String> stringList = Files.readAllLines(Path.of(URL));
        assertEquals(expectedStringList,stringList);
    }

    @Test
    void shouldLoadTaskManagerFromFile() {
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertNotEquals(0, manager1.getTasks().size());
    }

    @Test
    void shouldEqualsTaskManagerVsLoadTaskManagerFromFile() {
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(manager.getTasks(), manager1.getTasks());
        assertEquals(manager.getEpics(), manager1.getEpics());
        assertEquals(manager.getSubTasks(), manager1.getSubTasks());
    }

    @Test
    void shouldEmptyTasksLoadFromFile() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpics();
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(0, manager1.getTasks().size());
    }

    @Test
    void shouldEmptyHistoryLoadFromFile() {
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(0, manager1.getHistory().size());
    }

    @Test
    void shouldEqualsPrioritizedTasksLoadFromFile() {
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(manager.getPrioritizedTasks(), manager1.getPrioritizedTasks());
    }

    @Test
    void shouldEqualsIntervalGrid15MinLoadFromFile() {
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(manager.getIntervalGrid15Min(), manager1.getIntervalGrid15Min());
    }

    @Test
    void shouldHistoryLoadFromFile() {
        manager.getTaskById(1);
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(1, manager1.getHistory().size());
    }

    @Test
    void shouldEqualsHistoryLoadFromFile() {
        manager.getTaskById(1);
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(manager.getHistory(), manager1.getHistory());
    }
}