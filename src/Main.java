import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import service.TaskManager;
import utils.Managers;

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
        List<Task> historyTest;
        Task task1 = new Task("Task-01", "Task-01 desc", StatusOfTasks.NEW);
        Task task2 = new Task("Task-02", "Task-02 desc", StatusOfTasks.IN_PROGRESS);
        final int task1Id = manager.createTask(task1);
        final int task2Id = manager.createTask(task2);
        assert task1.toString().equals(manager.getTaskById(task1Id).toString()) : "Ошибка в создании или " +
                "возврате Task-01";
        assert task2.toString().equals(manager.getTaskById(task2Id).toString()) : "Ошибка в создании или " +
                "возврате Task-02";
        assert task1Id != task2Id : "id равны у Task-02 Task-02";
        historyTest = manager.getHistory();
        assert historyTest.size() == 2 && historyTest.contains(task1) && historyTest.contains(task2) : "Ошибка в " +
                "сохранении истории Task-01 или Task-02";
        ArrayList<Task> tasks = manager.getTasks();
        assert tasks != null : "Нет задач";
        Epic epic1 = new Epic("Epic-01", "Epic-01 desc");
        Epic epic2 = new Epic("Epic-02", "Epic-02 desc");
        final int epic1Id = manager.createEpic(epic1);
        final int epic2Id = manager.createEpic(epic2);
        assert epic1.toString().equals(manager.getEpicById(epic1Id).toString()) : "Ошибка в создании или " +
                "возврате Epic-01";
        assert epic2.toString().equals(manager.getEpicById(epic2Id).toString()) : "Ошибка в создании или " +
                "возврате Epic-02";
        assert epic1Id != epic2Id : "id равны у Epic-01 Epic-02";
        historyTest = manager.getHistory();
        assert historyTest.size() == 4 && historyTest.contains(epic1) && historyTest.contains(epic2) : "Ошибка в " +
                "сохранении истории Epic-01 или Epic-02";
        ArrayList<Epic> epics = manager.getEpics();
        assert epics != null : "Нет эпиков";

        SubTask subTask1 = new SubTask("SubTask-01", "SubTask-01 desc", StatusOfTasks.NEW, epic1Id);
        SubTask subTask2 = new SubTask("SubTask-02", "SubTask-02 desc", StatusOfTasks.NEW, epic1Id);
        SubTask subTask3 = new SubTask("SubTask-03", "SubTask-03 desc", StatusOfTasks.NEW, epic1Id);
        final int subTask1Id = manager.createSubTask(subTask1);
        final int subTask2Id = manager.createSubTask(subTask2);
        final int subTask3Id = manager.createSubTask(subTask3);
        assert subTask1.toString().equals(manager.getSubTaskById(subTask1Id).toString()) :
                "Ошибка в создании или возврате SubTask-01";
        assert subTask2.toString().equals(manager.getSubTaskById(subTask2Id).toString()) :
                "Ошибка в создании или возврате SubTask-02";
        assert subTask3.toString().equals(manager.getSubTaskById(subTask3Id).toString()) :
                "Ошибка в создании или возврате SubTask-03";
        assert subTask3Id != epic2Id : "Что-то с id";
        historyTest = manager.getHistory();
        assert historyTest.size() == 7 && historyTest.contains(subTask1) && historyTest.contains(subTask2)
                && historyTest.contains(subTask3) : "Ошибка в сохранении истории SubTask-01 или SubTask-02, SubTask-03";
        ArrayList<SubTask> subTasks = manager.getSubTasks();
        assert subTasks != null : "Нет подзадач";
        historyTest = manager.getHistory();
        Epic epic = manager.getEpicById(epic1Id);
        historyTest = manager.getHistory();
        assert historyTest.size() == 7 && historyTest.contains(epic1) && historyTest.indexOf(epic1) != 2
                : "Ошибка в сохранении истории Epic-01";
        assert historyTest.contains(task1) : "Нет в истории Task-01";
        assert epic.getSubTaskId() != null : "Нет подзадач";
        assert epic.getSubTaskId().contains(subTask1Id) : "Нет подзадачи SubTask-01";
        assert epic.getSubTaskId().contains(subTask2Id) : "Нет подзадачи SubTask-02";
        assert epic.getSubTaskId().contains(subTask3Id) : "Нет подзадачи SubTask-03";


        SubTask subTask4 = new SubTask("SubTask-04", "SubTask-04 desc", StatusOfTasks.NEW, epic2Id);
        SubTask subTask5 = new SubTask("SubTask-05", "SubTask-05 desc", StatusOfTasks.NEW, epic2Id);
        SubTask subTask6 = new SubTask("SubTask-06", "SubTask-06 desc", StatusOfTasks.NEW, epic2Id);
        SubTask subTask7 = new SubTask("SubTask-07", "SubTask-07 desc", StatusOfTasks.NEW, epic2Id);
        final int subTask4Id = manager.createSubTask(subTask4);
        final int subTask5Id = manager.createSubTask(subTask5);
        final int subTask6Id = manager.createSubTask(subTask6);
        final int subTask7Id = manager.createSubTask(subTask7);
        assert subTask4.toString().equals(manager.getSubTaskById(subTask4Id).toString()) :
                "Ошибка в создании или возврате SubTask-04";
        assert subTask5.toString().equals(manager.getSubTaskById(subTask5Id).toString()) :
                "Ошибка в создании или возврате SubTask-05";
        assert subTask6.toString().equals(manager.getSubTaskById(subTask6Id).toString()) :
                "Ошибка в создании или возврате SubTask-06";
        assert subTask7.toString().equals(manager.getSubTaskById(subTask7Id).toString()) :
                "Ошибка в создании или возврате SubTask-07";
        assert subTask5Id != subTask2Id : "Что-то с id";
        subTasks = manager.getSubTasks();
        assert subTasks != null : "Нет подзадач";
        historyTest = manager.getHistory();
        assert historyTest.size() == 11 && historyTest.contains(subTask4) && historyTest.contains(subTask5)
                && historyTest.contains(subTask6) && historyTest.contains(subTask7) : "Ошибка в сохранении истории " +
                "SubTask-04 или SubTask-05, SubTask-06,SubTask-07";
        epic = manager.getEpicById(epic2Id);
        historyTest = manager.getHistory();
        assert historyTest.indexOf(epic2) == (historyTest.size() - 1) : "В истории Epic-02 не на последнем месте.";
        assert epic.getSubTaskId() != null : "Нет подзадач";
        assert epic.getSubTaskId().contains(subTask4Id) : "Нет подзадачи SubTask-04";
        assert epic.getSubTaskId().contains(subTask5Id) : "Нет подзадачи SubTask-05";
        assert epic.getSubTaskId().contains(subTask6Id) : "Нет подзадачи SubTask-06";
        assert epic.getSubTaskId().contains(subTask7Id) : "Нет подзадачи SubTask-07";

        subTask4.setStatus(StatusOfTasks.IN_PROGRESS);
        manager.updateSubTask(subTask4);
        historyTest = manager.getHistory();
        epic = manager.getEpicById(subTask4.getEpicId());
        assert epic.getStatus().equals(StatusOfTasks.IN_PROGRESS) : "Не верно обновлен статус у Epic-02";
        historyTest = manager.getHistory();
        assert historyTest.indexOf(epic2) == (historyTest.size() - 1)
                && historyTest.indexOf(epic2) == (historyTest.size() - 1) : "В истории Epic-02 не на последнем месте.";
        assert manager.deleteEpicById(epic2Id) : "Epic-02 ошибка удаления";
        historyTest = manager.getHistory();
        assert !historyTest.contains(epic2) && !historyTest.contains(subTask5) && historyTest.size() == 6 :
                "После удаления Epic-02 история не изменилась";
        assert manager.getSubTaskById(subTask4Id) == null : "После удаления Epic-02, осталась SubTask-04";
        assert manager.getSubTaskById(subTask5Id) == null : "После удаления Epic-02, осталась SubTask-05";
        assert manager.getSubTaskById(subTask6Id) == null : "После удаления Epic-02, осталась SubTask-06";
        assert manager.getSubTaskById(subTask7Id) == null : "После удаления Epic-02, осталась SubTask-07";
        manager.getTaskById(task1Id);
        historyTest = manager.getHistory();
        assert historyTest.size() == 6 && historyTest.indexOf(task1) == 5 : "Ошибка в сохранении истории Task-01";
        StatusOfTasks oldStatus = epic1.getStatus();
        epic1.setStatus(StatusOfTasks.DONE);
        manager.updateEpic(epic1);
        assert manager.getEpicById(epic1Id).getStatus().equals(oldStatus) : "Изменился статус эпика Epic-01 " +
                " без изменения статуса подзадач.";

        manager.deleteAllTasks();
        assert manager.getTasks().isEmpty() : "Задачи не удалены";

        subTask1.setStatus(StatusOfTasks.IN_PROGRESS);
        manager.updateSubTask(subTask1);
        assert manager.getEpicById(epic1Id).getStatus().equals(StatusOfTasks.IN_PROGRESS) : "Не изменился статус " +
                "эпика Epic-01 по изменению статуса подзадачи SubTask-01 на IN_PROGRESS";


        manager.deleteSubTaskById(subTask1Id);
        assert !manager.getEpicById(epic1Id).getSubTaskId().contains(subTask1Id) : "Ошибка при удалении подзадачи " +
                "SubTask-01.Отвязка от эпика";

        subTask2.setStatus(StatusOfTasks.DONE);
        manager.updateSubTask(subTask2);
        assert !manager.getEpicById(epic1Id).getStatus().equals(StatusOfTasks.DONE) : "Ошибка! подсчета статуса " +
                "эпика Epic-01  по изменению статуса подзадачи SubTask-02 на DONE";

        subTask3.setStatus(StatusOfTasks.DONE);
        manager.updateSubTask(subTask3);
        assert manager.getEpicById(epic1Id).getStatus().equals(StatusOfTasks.DONE) : "Ошибка! подсчета статуса " +
                "эпика Epic-01 по изменению статуса подзадачи SubTask-02 на DONE";
        assert !manager.deleteEpicById(15) : "Ошибка удаления не существующего эпика ";
        System.out.println("Успех");
    }
}
