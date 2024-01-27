package service;

import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void initHistory() {
        historyManager = Managers.getDefaultHistory();
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 15, 0);
        Task task = new Task("Task-01", "Task-01 desc", 1, StatusOfTasks.NEW, 10, startTime);
        Epic epic = new Epic("Epic-01", "Epic-01 desc", 2, startTime);
        SubTask subTask = new SubTask("SubTask-01", "SubTask-01 desc", 3, StatusOfTasks.NEW,
                2, 10, startTime);
    }

    @Test
    void shouldAddTasksInHistoryManagerWhenAddTask() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 15, 0);
        Task task = new Task("Task-01", "Task-01 desc", 1, StatusOfTasks.NEW, 10, startTime);
        Epic epic = new Epic("Epic-01", "Epic-01 desc", 2, startTime);
        SubTask subTask = new SubTask("SubTask-01", "SubTask-01 desc", 3, StatusOfTasks.NEW,
                2, 10, startTime);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        assertNotEquals(0, historyManager.getHistory().size());
        historyManager.add(null);
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void shouldNoDuplicateTaskInHistoryManagerWhenAddTask() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 15, 0);
        Task task = new Task("Task-01", "Task-01 desc", 1, StatusOfTasks.NEW, 10, startTime);
        Epic epic = new Epic("Epic-01", "Epic-01 desc", 2, startTime);
        SubTask subTask = new SubTask("SubTask-01", "SubTask-01 desc", 3, StatusOfTasks.NEW,
                2, 10, startTime);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        assertEquals(3, historyManager.getHistory().size());
        historyManager.add(epic);
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void shouldRemoveTaskInHistoryManagerWhenTaskOnlyOne() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 15, 0);
        Task task = new Task("Task-01", "Task-01 desc", 1, StatusOfTasks.NEW, 10, startTime);
        historyManager.add(task);
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void shouldRemoveTaskInHistoryManagerWhenTaskBegin() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 15, 0);
        Task task = new Task("Task-01", "Task-01 desc", 1, StatusOfTasks.NEW, 10, startTime);
        Epic epic = new Epic("Epic-01", "Epic-01 desc", 2, startTime);
        SubTask subTask = new SubTask("SubTask-01", "SubTask-01 desc", 3, StatusOfTasks.NEW,
                2, 10, startTime);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(1);
        List<Task> historyExpected = List.of(epic, subTask);
        assertEquals(historyExpected, historyManager.getHistory());
    }

    @Test
    void shouldRemoveTaskInHistoryManagerWhenTaskMiddle() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 15, 0);
        Task task = new Task("Task-01", "Task-01 desc", 1, StatusOfTasks.NEW, 10, startTime);
        Epic epic = new Epic("Epic-01", "Epic-01 desc", 2, startTime);
        SubTask subTask = new SubTask("SubTask-01", "SubTask-01 desc", 3, StatusOfTasks.NEW,
                2, 10, startTime);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(2);
        List<Task> historyExpected = List.of(task, subTask);
        assertEquals(historyExpected, historyManager.getHistory());
    }

    @Test
    void shouldRemoveTaskInHistoryManagerWhenTaskEnd() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 15, 0);
        Task task = new Task("Task-01", "Task-01 desc", 1, StatusOfTasks.NEW, 10, startTime);
        Epic epic = new Epic("Epic-01", "Epic-01 desc", 2, startTime);
        SubTask subTask = new SubTask("SubTask-01", "SubTask-01 desc", 3, StatusOfTasks.NEW,
                2, 10, startTime);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(3);
        List<Task> historyExpected = List.of(task, epic);
        assertEquals(historyExpected, historyManager.getHistory());
    }

    @Test
    void shouldEmptyHistoryInHistoryManager() {
        assertEquals(0, historyManager.getHistory().size());
    }
}