package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    private final String URL = "src/resources/data.csv";
    private FileBackedTasksManager manager;
    @BeforeEach
    void initFileBackedTasksManager(){
        manager = new FileBackedTasksManager(URL);
        super.setDefault(manager);
    }
    @AfterEach
    void deleteFile() throws IOException {
        Files.deleteIfExists(Path.of(URL));
    }

    @Test
    void shouldLoadTaskManagerFromFile() {
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertNotEquals(0,manager1.getTasks().size());
    }

    @Test
    void shouldEmptyTasksLoadFromFile() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpics();
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(0,manager1.getTasks().size());
    }
    @Test
    void shouldEmptyHistoryLoadFromFile() {
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(0,manager1.getHistory().size());
    }
    @Test
    void shouldHistoryLoadFromFile() {
        manager.getTaskById(1);
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(URL);
        assertEquals(1,manager1.getHistory().size());
    }
}