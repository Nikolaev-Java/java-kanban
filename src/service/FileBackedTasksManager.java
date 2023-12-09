package service;

import exception.ManagerSaveException;
import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import utils.CSVTaskFormat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path fileData;
    private static final String HEADING_STRING = "id,type,name,status,description,epic";

    public FileBackedTasksManager(String fileDataURI) {
        this.fileData = Paths.get(fileDataURI);
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileManager1 = new FileBackedTasksManager("src/resources/data.csv");
        final int task1id = fileManager1.createTask(new Task("Task-01", "Task-01 desc", StatusOfTasks.NEW));
        final int epic1Id = fileManager1.createEpic(new Epic("Epic-01", "Epic-01 desc"));
        final int subTask1Id = fileManager1.createSubTask(new SubTask("SubTask-01", "SubTask-01 desc", StatusOfTasks.NEW, epic1Id));
        fileManager1.getTaskById(task1id);
        fileManager1.getSubTaskById(subTask1Id);
        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile("src/resources/data.csv");
        final int epic2Id = fileManager.createEpic(new Epic("Epic-02", "Epic-02 desc"));
        FileBackedTasksManager fileManager2 = FileBackedTasksManager.loadFromFile("src/resources/data.csv");
        assert epic2Id != task1id && epic2Id != epic1Id && epic2Id != subTask1Id :
                "Неверная генерация id после загрузки истории";
        assert fileManager1.getTaskById(task1id).equals(fileManager.getTaskById(task1id)) :
                "Задачи Task-01 не совпадают";
        assert fileManager.getEpicById(epic1Id).equals(fileManager1.getEpicById(epic1Id)) :
                "Задачи Epic-01 не совпадают";
        assert fileManager1.getSubTaskById(subTask1Id).equals(fileManager.getSubTaskById(subTask1Id)) :
                "Задачи SubTask-01 не совпадают";
        assert fileManager1.getHistory().equals(fileManager.getHistory()) : "История не одинаковая";
    }

    private void save() throws ManagerSaveException {
        List<Task> taskList = getTasks();
        taskList.addAll(getEpics());
        taskList.addAll(getSubTasks());
        StringBuilder resultToSave = new StringBuilder();
        resultToSave.append(HEADING_STRING).append("\n");
        for (Task task : taskList) {
            resultToSave.append(CSVTaskFormat.TaskToSting(task)).append("\n");

        }
        resultToSave.append("\n");
        resultToSave.append(CSVTaskFormat.historyToString(super.getHistoryManager()));
        try {
            Files.writeString(fileData, resultToSave.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e);
        }
    }

    public static FileBackedTasksManager loadFromFile(String fileName) throws ManagerSaveException {
        Path file = Paths.get(fileName);
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileName);
        HashMap<Integer, Task> taskHashMap = new HashMap<>();
        List<String> dataString;
        int maxId = 0;
        try {
            if (!Files.exists(file)) return fileBackedTasksManager;
            dataString = Files.readAllLines(file);
            if (dataString.isEmpty()) return fileBackedTasksManager;
            for (int i = 1; i < dataString.size(); i++) {
                if (dataString.get(i).isEmpty()) {
                    break;
                }
                Task task = CSVTaskFormat.fromString(dataString.get(i));
                if (task != null) {
                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }
                    fileBackedTasksManager.addTask(task,maxId);
                    taskHashMap.put(task.getId(), task);
                }
            }
            if (!dataString.get(dataString.size() - 1).isEmpty()) {
                List<Integer> idHistoriList = CSVTaskFormat.historyFromString(dataString.get(dataString.size() - 1));
                for (Integer i : idHistoriList) {
                    fileBackedTasksManager.getHistoryManager().add(taskHashMap.get(i));
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в работе с файлом", e);
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        int id = super.createSubTask(subTask);
        save();
        return id;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean result = super.updateTask(task);
        save();
        return result;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean result = super.updateEpic(epic);
        save();
        return result;
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        boolean result = super.updateSubTask(subTask);
        save();
        return result;
    }

    @Override
    public boolean deleteTaskById(int id) {
        boolean result = super.deleteTaskById(id);
        save();
        return result;
    }

    @Override
    public boolean deleteEpicById(int id) {
        boolean result = super.deleteEpicById(id);
        save();
        return result;
    }

    @Override
    public boolean deleteSubTaskById(int id) {
        boolean result = super.deleteSubTaskById(id);
        save();
        return result;
    }
}
