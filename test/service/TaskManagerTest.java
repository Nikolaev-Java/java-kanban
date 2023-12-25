package service;

import exception.ManagerCreateException;
import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.Duration;
import java.time.LocalDateTime;
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
    private int task1Id;
    private int task2Id;
    private int epic1Id;
    private int epic2Id;
    private int subTask1Id;
    private int subTask2Id;
    private LocalDateTime startTime1;
    private LocalDateTime startTime2;
    private LocalDateTime startTime3;
    private LocalDateTime startTime4;
    private LocalDateTime startTime5;

    public void setDefault(T manager) {
        this.manager = manager;
        startTime1 = LocalDateTime.of(2023, 1, 1, 15, 0);
        startTime2 = LocalDateTime.of(2023, 1, 2, 15, 0);
        startTime3 = LocalDateTime.of(2023, 1, 3, 15, 0);
        startTime4 = LocalDateTime.of(2023, 1, 4, 15, 0);
        startTime5 = LocalDateTime.of(2023, 1, 5, 15, 0);
        task1 = new Task("Task-01", "Task-01 desc", StatusOfTasks.NEW, 10, startTime4);
        task2 = new Task("Task-02", "Task-02 desc", StatusOfTasks.IN_PROGRESS, 10, startTime3);
        epic1 = new Epic("Epic-01", "Epic-01 desc");
        epic2 = new Epic("Epic-02", "Epic-02 desc");
        task1Id = manager.createTask(task1);
        task2Id = manager.createTask(task2);
        epic1Id = manager.createEpic(epic1);
        epic2Id = manager.createEpic(epic2);
        subTask1 = new SubTask("SubTask-01", "SubTask-01 desc", StatusOfTasks.NEW,
                epic1Id, 10, startTime1);
        subTask2 = new SubTask("SubTask-02", "SubTask-02 desc", StatusOfTasks.DONE,
                epic1Id, 10, startTime2);
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
        manager.createTask(new Task("Test Task", "desc", StatusOfTasks.NEW, 10, LocalDateTime.of(2023, 3, 12, 0, 0)));
        assertEquals(3, manager.getTasks().size());
    }

    @Test
    void shouldNotEmptyEpicsInTaskManagerAfterCreateEpics() {
        manager.createEpic(new Epic("Test Epic", "desc", LocalDateTime.of(2023, 4, 12, 0, 0)));
        assertEquals(3, manager.getEpics().size());
    }

    @Test
    void shouldNotEmptySubTaskInTaskManagerAfterCreateSubTasks() {
        manager.createSubTask(new SubTask("Test SubTask", "desc", StatusOfTasks.NEW,
                epic1Id, 10, LocalDateTime.of(2023, 2, 12, 0, 0)));
        assertEquals(3, manager.getSubTasks().size());
    }

    @Test
    void shouldThrowsExceptionAfterCreateSubTaskNull() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createSubTask(null);
            }
        });
        assertEquals("Задача пустая", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionAfterCreateTaskNull() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(null);
            }
        });
        assertEquals("Задача пустая", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionAfterCreateEpicNull() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createEpic(null);
            }
        });
        assertEquals("Задача пустая", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionAfterCreateTaskCrossingDate() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 42,
                        LocalDateTime.of(2023, 05, 12, 10, 00)));
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 10,
                        LocalDateTime.of(2023, 05, 12, 10, 20)));
            }
        });
        assertEquals("Невозможно создать задачу, из-за пересечения времени", exception.getMessage());
    }


    @Test
    void shouldThrowsExceptionAfterCreateTaskCrossingDateInStartDateEquals() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 42,
                        LocalDateTime.of(2023, 06, 01, 15, 00)));
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                        LocalDateTime.of(2023, 06, 01, 15, 00)));
            }
        });
        assertEquals("Невозможно создать задачу, из-за пересечения времени", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionAfterCreateTaskCrossingDateInStartDate() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 42,
                        LocalDateTime.of(2023, 06, 01, 15, 00)));
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                        LocalDateTime.of(2023, 06, 01, 15, 15)));
            }
        });
        assertEquals("Невозможно создать задачу, из-за пересечения времени", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionAfterCreateTaskCrossingDateInEndDate() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 42,
                        LocalDateTime.of(2023, 05, 06, 16, 00)));
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 120,
                        LocalDateTime.of(2023, 05, 06, 14, 05)));
            }
        });
        assertEquals("Невозможно создать задачу, из-за пересечения времени", exception.getMessage());
    }

    @Test
    void shouldNoThrowExceptionAfterCreateNewTaskEndDateEqualsStartDateExistingTask() {
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 45,
                LocalDateTime.of(2023, 01, 1, 1, 00)));
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 60,
                LocalDateTime.of(2023, 01, 1, 0, 00)));
    }

    @Test
    void shouldNoThrowExceptionAfterCreateNewTaskStartDateEqualsEndDateExistingTask() {
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 45,
                LocalDateTime.of(2023, 07, 18, 1, 00)));
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 60,
                LocalDateTime.of(2023, 07, 18, 1, 45)));
    }


    @Test
    void shouldThrowsExceptionAfterCreateSubTaskCrossingDate() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createSubTask(new SubTask("test Task", "desc", StatusOfTasks.NEW, epic1Id, 120,
                        LocalDateTime.of(2023, 12, 31, 22, 00)));
                manager.createSubTask(new SubTask("test Task", "desc", StatusOfTasks.NEW, epic1Id, 10,
                        LocalDateTime.of(2023, 12, 31, 23, 27)));
            }
        });
        assertEquals("Невозможно создать задачу, из-за пересечения времени", exception.getMessage());
    }

    @Test
    void shouldClearIntervalAfterStatusDone() {
        int idTask = manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 423,
                LocalDateTime.of(2022, 8, 13, 23, 03)));
        Task task = manager.getTaskById(idTask);
        task.setStatus(StatusOfTasks.DONE);
        manager.updateTask(task);
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                LocalDateTime.of(2022, 8, 14, 6, 00)));
    }

    @Test
    void shouldClearIntervalAfterChangeTimeTask() {
        int idTask = manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 60,
                LocalDateTime.of(2023, 12, 31, 23, 00)));
        Task task = new Task("test Task", "desc", idTask, StatusOfTasks.NEW, 65,
                LocalDateTime.of(2023, 12, 30, 22, 00));
        manager.updateTask(task);
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                LocalDateTime.of(2023, 12, 31, 23, 30)));
    }

    @Test
    void shouldClearIntervalAfterChangeTimeSubTask() {
        int idTask = manager.createSubTask(new SubTask("test Task", "desc", StatusOfTasks.NEW, epic2Id,
                65, LocalDateTime.of(2023, 1, 25, 15, 00)));
        SubTask task = new SubTask("test Task", "desc", idTask, StatusOfTasks.NEW, epic2Id, 65,
                LocalDateTime.of(2023, 1, 2, 22, 00));
        manager.updateSubTask(task);
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                LocalDateTime.of(2023, 01, 25, 15, 30)));
    }

    @Test
    void shouldNotUpdateTaskAfterChangeTimeTaskIfIsCrossingDate() {
        int idTask = manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW,
                20, LocalDateTime.of(2023, 1, 25, 15, 00)));
        Task task = new Task("test Task", "desc", idTask, StatusOfTasks.NEW, 65,
                startTime1);
        assertFalse(manager.updateTask(task));
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                        LocalDateTime.of(2023, 01, 25, 15, 00)));
            }
        });
        assertEquals("Невозможно создать задачу, из-за пересечения времени", exception.getMessage());
    }

    @Test
    void shouldNotUpdateSubTaskAfterChangeTimeSubTaskIfIsCrossingDate() {
        int idTask = manager.createSubTask(new SubTask("test Task", "desc", StatusOfTasks.NEW, epic2Id,
                5, LocalDateTime.of(2023, 3, 14, 15, 10)));
        SubTask task = new SubTask("test Task", "desc", idTask, StatusOfTasks.NEW,  epic2Id,65,
                startTime3);
        assertFalse(manager.updateSubTask(task));
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                        LocalDateTime.of(2023, 03, 14, 14, 50)));
            }
        });
        assertEquals("Невозможно создать задачу, из-за пересечения времени", exception.getMessage());
    }

    @Test
    void shouldClearIntervalAfterTaskDelete() {
        int idTask = manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 60,
                LocalDateTime.of(2023, 05, 31, 23, 05)));
        manager.deleteTaskById(idTask);
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                LocalDateTime.of(2023, 06, 1, 0, 00)));
    }

    @Test
    void shouldClearIntervalAfterSubTaskDelete() {
        int idTask = manager.createSubTask(new SubTask("test Task", "desc", StatusOfTasks.NEW, epic2Id,
                65, LocalDateTime.of(2023, 01, 1, 0, 00)));
        manager.deleteSubTaskById(idTask);
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                LocalDateTime.of(2023, 01, 1, 0, 00)));
    }

    @Test
    void shouldClearIntervalAfterEpicDelete() {
        manager.deleteEpicById(epic1Id);
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 15,
                startTime1));
    }

    @Test
    void shouldThrowsExceptionAfterCreateSubTaskNotExistingEpic() {
        final ManagerCreateException exception = assertThrows(ManagerCreateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createSubTask(new SubTask("test Task", "desc", StatusOfTasks.NEW, 15, 120,
                        LocalDateTime.of(2023, 12, 31, 22, 00)));

            }
        });
        assertEquals("Нет эпика с данным ID", exception.getMessage());
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
        Epic newEpic = new Epic("New Task01", "new Details", epic1Id, startTime1);
        manager.updateEpic(newEpic);
        assertEquals(newEpic, manager.getEpicById(epic1Id));
        newEpic.setId(100);
        assertFalse(manager.updateEpic(newEpic));
    }

    @Test
    void shouldNewSubTaskEqualsOldTaskAfterUpdateSubTask() {
        SubTask newSubTask = new SubTask("New Task01", "new Details", subTask1Id, StatusOfTasks.DONE,
                epic1Id, 10, null);
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
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id, 10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id, 10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id, 10));
        assertEquals(StatusOfTasks.NEW, manager.getEpicById(epic2Id).getStatus());
    }

    @Test
    void shouldStatusEpicIsDoneWhenAllSubTaskStatusDone() {
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.DONE, epic2Id, 10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.DONE, epic2Id, 10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.DONE, epic2Id, 10));
        assertEquals(StatusOfTasks.DONE, manager.getEpicById(epic2Id).getStatus());
    }

    @Test
    void shouldStatusEpicIsInProgressWhenSubTaskStatusNewAndDone() {
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id, 10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.DONE, epic2Id, 10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.NEW, epic2Id, 10));
        assertEquals(StatusOfTasks.IN_PROGRESS, manager.getEpicById(epic2Id).getStatus());
    }

    @Test
    void shouldStatusEpicIsInProgressWhenSubTaskStatusInProgress() {
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id, 10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id, 10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id, 10));
        assertEquals(StatusOfTasks.IN_PROGRESS, manager.getEpicById(epic2Id).getStatus());
    }

    @Test
    void shouldPrioritizedTaskSorted() {
        List<Task> sortTask = List.of(epic1, subTask1, subTask2, task2, task1, epic2);
        assertEquals(sortTask, manager.getPrioritizedTasks());
    }

    @Test
    void shouldCorrectCalculateTimeEpic() {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 05, 01, 15, 00);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 05, 06, 16, 00);
        LocalDateTime startTime3 = LocalDateTime.of(2023, 05, 25, 15, 00);
        LocalDateTime endTime = startTime3.plus(Duration.ofMinutes(10));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id,
                10, startTime3));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id,
                10, startTime2));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id,
                10, startTime1));
        assertEquals(startTime1, manager.getEpicById(epic2Id).getStartTime());
        assertEquals(endTime, manager.getEpicById(epic2Id).getEndTime());
        assertEquals(30, manager.getEpicById(epic2Id).getDuration());
    }

    @Test
    void shouldNoThrowExceptionThenCreateTaskBetweenSubTasksOfEpic() {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 05, 01, 15, 00);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 05, 06, 16, 00);
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id,
                10, startTime2));
        manager.createSubTask(new SubTask("test", "desc", StatusOfTasks.IN_PROGRESS, epic2Id,
                10, startTime1));
        manager.createTask(new Task("test Task", "desc", StatusOfTasks.NEW, 120,
                LocalDateTime.of(2023, 05, 5, 14, 05)));
    }
}