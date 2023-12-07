package service;

import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import utils.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private int generateId = 0;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        subTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public int createTask(Task task) {
        if (task == null) {
            return -1; //исключения еще не проходили. Поэтому решил возвращать -1.
        }
        int id = ++generateId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        if (epic == null) {
            return -1;
        }
        int id = ++generateId;
        epic.setId(id);
        epics.put(id, epic);
        calculateEpicStatus(id);
        return id;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask == null) {
            return -1;
        }
        int id = ++generateId;
        subTask.setId(id);
        epics.get(subTask.getEpicId()).addSubTask(id);
        subTasks.put(id, subTask);
        return id;
    }

    @Override
    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            calculateEpicStatus(epic.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            calculateEpicStatus(subTask.getEpicId());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            return false;
        }
        Epic epic = epics.get(id);
        for (Integer i : epic.getSubTaskId()) {
            historyManager.remove(i);
            subTasks.remove(i);
        }
        historyManager.remove(id);
        epics.remove(id);
        return true;
    }

    @Override
    public boolean deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            return false;
        }
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.deleteSubTaskId(id);
        updateEpic(epic);
        calculateEpicStatus(epic.getId());
        historyManager.remove(id);
        subTasks.remove(id);
        return true;
    }

    @Override
    public ArrayList<SubTask> getSubTaskInEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<SubTask> subTasksInEpic = new ArrayList<>();
        if (epic != null) {
            for (Integer id : epic.getSubTaskId()) {
                subTasksInEpic.add(subTasks.get(id));
            }
            return subTasksInEpic;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void calculateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subTaskId = epic.getSubTaskId();
        Set<StatusOfTasks> statusSubTasks = new HashSet<>();
        for (Integer id : subTaskId) {
            statusSubTasks.add(subTasks.get(id).getStatus());
        }
        if (statusSubTasks.isEmpty()) {
            epic.setStatus(StatusOfTasks.NEW);
            epics.put(epic.getId(), epic);
        } else if (statusSubTasks.size() >= 2) {
            epic.setStatus(StatusOfTasks.IN_PROGRESS);
            epics.put(epicId, epic);
        } else {
            for (StatusOfTasks statusSubTask : statusSubTasks) {
                epic.setStatus(statusSubTask);
                epics.put(epicId, epic);
            }
        }
    }

    protected void addTask(Task task) {
        generateId = task.getId();
        String typeTask = String.valueOf(task.getClass()).split(" ")[1];
        switch (typeTask) {
            case "model.Task": {
                tasks.put(task.getId(), task);
                break;
            }
            case "model.Epic": {
                epics.put(task.getId(), (Epic) task);
                break;
            }
            case "model.SubTask":
                subTasks.put(task.getId(), (SubTask) task);
                epics.get(((SubTask) task).getEpicId()).addSubTask(task.getId());
                break;
        }
    }

}
