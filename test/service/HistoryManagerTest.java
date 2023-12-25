package service;

import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Managers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HistoryManagerTest {
    private HistoryManager historyManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;
    private LocalDateTime startTime;

    @BeforeEach
    void initHistory() {
        historyManager = Managers.getDefaultHistory();
        startTime = LocalDateTime.of(2023, 12, 1, 15, 0);
        task = new Task("Task-01", "Task-01 desc", 1, StatusOfTasks.NEW, 10, startTime);
        epic = new Epic("Epic-01", "Epic-01 desc", 2, startTime);
        subTask = new SubTask("SubTask-01", "SubTask-01 desc", 3, StatusOfTasks.NEW, 2, 10, startTime);
    }

    @Test
    void shouldAddTasksInHistoryManagerWhenAddTask() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        assertNotEquals(0, historyManager.getHistory().size());
        historyManager.add(null);
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void shouldNoDuplicateTaskInHistoryManagerWhenAddTask() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        assertEquals(3, historyManager.getHistory().size());
        historyManager.add(epic);
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void shouldRemoveTaskInHistoryManagerWhenTaskOnlyOne() {
        historyManager.add(task);
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void shouldRemoveTaskInHistoryManagerWhenTaskBegin() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(1);
        List<Task> historyExpected = List.of(epic, subTask);
        assertEquals(historyExpected, historyManager.getHistory());
    }

    @Test
    void shouldRemoveTaskInHistoryManagerWhenTaskMiddle() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(2);
        List<Task> historyExpected = List.of(task, subTask);
        assertEquals(historyExpected, historyManager.getHistory());
    }

    @Test
    void shouldRemoveTaskInHistoryManagerWhenTaskEnd() {
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