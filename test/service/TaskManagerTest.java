package service;

import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    private T manager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private SubTask subTask1;
    private SubTask subTask2;
    private SubTask subTask3;
    private int task1Id;
    private int task2Id;
    private int epic1Id;
    private int epic2Id;
    private int subTask1Id;
    private int subTask2Id;

    public void setDefault(T manager) {
        this.manager = manager;
        task1 = new Task("Task-01", "Task-01 desc", StatusOfTasks.NEW);
        task2 = new Task("Task-02", "Task-02 desc", StatusOfTasks.IN_PROGRESS);
        epic1 = new Epic("Epic-01", "Epic-01 desc");
        epic2 = new Epic("Epic-02", "Epic-02 desc");
        task1Id = manager.createTask(task1);
        task2Id = manager.createTask(task2);
        epic1Id = manager.createEpic(epic1);
        epic2Id = manager.createEpic(epic2);
        subTask1 = new SubTask("SubTask-01", "SubTask-01 desc", StatusOfTasks.NEW, epic1Id);
        subTask2 = new SubTask("SubTask-02", "SubTask-02 desc", StatusOfTasks.DONE, epic1Id);
        subTask3 = new SubTask("SubTask-03", "SubTask-03 desc", StatusOfTasks.IN_PROGRESS, epic1Id);
        subTask1Id = manager.createSubTask(subTask1);
        subTask2Id = manager.createSubTask(subTask2);
    }

    @Test
    void shouldReturnsTwoTasksFromTaskManagerAfterCreateTwoTask() {
        assertEquals(2, manager.getTasks().size());
    }

    @Test
    void shouldReturnsTwoEpicsFromTaskManagerAfterCreateTwoEpic() {
        assertEquals(2, manager.getEpics().size());
    }

    @Test
    void shouldReturnsTwoEpicsFromTaskManagerAfterCreateTwoSubTask() {
        assertEquals(2, manager.getSubTasks().size());
    }

    @Test
    void shouldEmptyTasksInTaskManagerAfterDeleteAllTasks() {
        manager.deleteAllTasks();
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    void shouldEmptyEpicsInTaskManagerAfterDeleteAllEpics() {
        manager.deleteAllEpics();
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    void shouldEmptySubTasksInTaskManagerAfterDeleteAllSubTasks() {
        manager.deleteAllSubTasks();
        assertEquals(0, manager.getSubTasks().size());
    }

    @Test
    void shouldEmptySubTaskInEpic01AfterDeleteAllSubTasks() {
        manager.deleteAllSubTasks();
        assertEquals(0, manager.getEpicById(epic1Id).getSubTaskId().size());
    }

    @Test
    void shouldEqualsTaskAfterGetTaskById() {
        assertEquals(task1, manager.getTaskById(task1Id));
        assertNotEquals(task1, manager.getTaskById(task2Id));
    }

    @Test
    void shouldEqualsEpicAfterGetEpicById() {
        assertEquals(epic1, manager.getEpicById(epic1Id));
        assertNotEquals(epic1, manager.getEpicById(epic2Id));
    }

    @Test
    void shouldEqualsSubTaskAfterGetSubTaskById() {
        assertEquals(subTask1, manager.getSubTaskById(subTask1Id));
        assertNotEquals(subTask1, manager.getSubTaskById(subTask2Id));
    }

    @Test
    void shouldNotEmptyTasksInTaskManagerAfterCreateTask() {
        manager.createTask(new Task("Test Task", "desc", StatusOfTasks.NEW));
        assertEquals(3, manager.getTasks().size());
        assertEquals(-1, manager.createTask(null));
    }

    @Test
    void shouldNotEmptyEpicsInTaskManagerAfterCreateEpics() {
        manager.createEpic(new Epic("Test Epic", "desc"));
        assertEquals(3, manager.getEpics().size());
        assertEquals(-1, manager.createEpic(null));
    }

    @Test
    void shouldNotEmptySubTaskInTaskManagerAfterCreateSubTasks() {
        manager.createSubTask(new SubTask("Test SubTask", "desc", StatusOfTasks.NEW, epic1Id));
        assertEquals(3, manager.getSubTasks().size());
        assertEquals(-1, manager.createSubTask(null));
    }

    @Test
    void shouldNewTaskEqualsOldTaskAfterUpdateTask() {
        Task newTask = new Task("New Task01", "new Details", task1Id, StatusOfTasks.DONE);
        manager.updateTask(newTask);
        assertEquals(newTask, manager.getTaskById(task1Id));
        newTask.setId(100);
        assertFalse(manager.updateTask(newTask));
    }

    @Test
    void shouldNewEpicEqualsOldTaskAfterUpdateEpic() {
        Epic newEpic = new Epic("New Task01", "new Details", epic1Id);
        manager.updateEpic(newEpic);
        assertEquals(newEpic, manager.getEpicById(epic1Id));
        newEpic.setId(100);
        assertFalse(manager.updateEpic(newEpic));
    }

    @Test
    void shouldNewSubTaskEqualsOldTaskAfterUpdateSubTask() {
        SubTask newSubTask = new SubTask("New Task01", "new Details", subTask1Id, StatusOfTasks.DONE, epic1Id);
        manager.updateSubTask(newSubTask);
        assertEquals(newSubTask, manager.getSubTaskById(subTask1Id));
        newSubTask.setId(100);
        assertFalse(manager.updateSubTask(newSubTask));
    }

    @Test
    void shouldReturnsSubTasksInEpic() {
        List<SubTask> subTaskList = manager.getSubTaskInEpic(epic1Id);
        assertNotEquals(0, subTaskList.size());
        subTaskList = manager.getSubTaskInEpic(100);
        assertEquals(0, subTaskList.size());
    }

    @Test
    void shouldReturnsEmptySubTasksInEpic() {
        List<SubTask> subTaskList = manager.getSubTaskInEpic(epic2Id);
        assertEquals(0, subTaskList.size());
    }

    @Test
    void shouldReturnsEmptyHistoryInTaskManager() {
        List<Task> taskHistory = manager.getHistory();
        assertEquals(0, taskHistory.size());
    }

    @Test
    void shouldReturnsHistoryInTaskManager() {
        manager.getTaskById(task1Id);
        List<Task> taskHistory = manager.getHistory();
        assertEquals(1, taskHistory.size());
    }

    @Test
    void shouldStatusEpicIsNewWhenNoSubTask() {
        assertEquals(StatusOfTasks.NEW, manager.getEpicById(epic2Id).getStatus());
    }

    @Test
    void shouldDeleteTaskInTaskManagerAfterDeleteTaskById() {
        assertTrue(manager.deleteTaskById(task1Id));
        assertFalse(manager.deleteTaskById(epic1Id));
        assertNull(manager.getTaskById(task1Id));
    }

    @Test
    void shouldDeleteEpicInTaskManagerAfterDeleteEpicById() {
        assertTrue(manager.deleteEpicById(epic1Id));
        assertFalse(manager.deleteEpicById(task1Id));
        assertNull(manager.getEpicById(epic1Id));
    }

    @Test
    void shouldDeleteSubTaskInTaskManagerAfterDeleteSubTaskById() {
        assertTrue(manager.deleteSubTaskById(subTask1Id));
        assertFalse(manager.deleteSubTaskById(task1Id));
        assertNull(manager.getSubTaskById(subTask1Id));
    }

    @Test
    void shouldStatusEpicIsNewWhenAllSubTaskStatusNew() {
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id));
        assertEquals(StatusOfTasks.NEW, manager.getEpicById(epic2Id).getStatus());
    }

    @Test
    void shouldStatusEpicIsDoneWhenAllSubTaskStatusDone() {
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.DONE, epic2Id));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.DONE, epic2Id));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.DONE, epic2Id));
        assertEquals(StatusOfTasks.DONE, manager.getEpicById(epic2Id).getStatus());
    }

    @Test
    void shouldStatusEpicIsInProgressWhenSubTaskStatusNewAndDone() {
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.DONE, epic2Id));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id));
        assertEquals(StatusOfTasks.IN_PROGRESS, manager.getEpicById(epic2Id).getStatus());
    }

    @Test
    void shouldStatusEpicIsInProgressWhenSubTaskStatusInProgress() {
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id));
        assertEquals(StatusOfTasks.IN_PROGRESS, manager.getEpicById(epic2Id).getStatus());
    }
}