import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import service.TaskManager;
import utils.Managers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*
        NEW — задача только создана, но к её выполнению ещё не приступили.
        IN_PROGRESS — над задачей ведётся работа.
        DONE — задача выполнена.
        */
        TaskManager manager = Managers.getDefault();
        LocalDateTime startTime2 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTime3 = LocalDateTime.of(2023, 1, 1, 0, 15);
        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 0, 20);
        Epic epic1 = new Epic("Epic-01", "Epic-01 desc");
        Epic epic2 = new Epic("Epic-02", "Epic-02 desc");
        final int epic1Id = manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask-01", "SubTask-01 desc", StatusOfTasks.NEW, epic1Id, 15,startTime);
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask-02", "SubTask-02 desc", StatusOfTasks.NEW, epic1Id, 10, startTime2);
        SubTask subTask3 = new SubTask("SubTask-03", "SubTask-03 desc", StatusOfTasks.NEW, epic1Id, 20, startTime3);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);
        manager.createEpic(epic2);
        manager.deleteSubTaskById(3);
        List<Task> sort = manager.getPrioritizedTasks();
        System.out.println();
    }
}
