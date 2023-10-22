package service;

import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int generateId = 0;


    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public int createTask(Task task) {
        if (task == null) {
            return -1; //исключения еще не проходили. Поэтому решил возвращать -1.
        }
        int id = ++generateId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

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

    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            return true;
        }
        return false;
    }

    public boolean updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            calculateEpicStatus(epic.getId());
            return true;
        }
        return false;
    }

    public boolean updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            calculateEpicStatus(subTask.getEpicId());
            return true;
        }
        return false;
    }

    public boolean deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            return true;
        }
        return false;
    }

    public boolean deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            return false;
        }
        Epic epic = epics.get(id);
        for (Integer i : epic.getSubTaskId()) {
            subTasks.remove(i);
        }
        epics.remove(id);
        return true;
    }

    public boolean deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            return false;
        }
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.deleteSubTaskId(id);
        updateEpic(epic);
        calculateEpicStatus(epic.getId());
        subTasks.remove(id);
        return false;
    }

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

    private void calculateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subTaskId = epic.getSubTaskId();
        Set<StatusOfTasks> statusSubTasks = new HashSet<>();
        for (Integer id : subTaskId) {
            statusSubTasks.add(subTasks.get(id).getStatus());
        }
        if (subTaskId.isEmpty() || (statusSubTasks.contains(StatusOfTasks.NEW) && statusSubTasks.size() == 1)) {
            epic.setStatus(StatusOfTasks.NEW);
            epics.put(epic.getId(), epic);
            return;
        }
        if (statusSubTasks.contains(StatusOfTasks.DONE) && statusSubTasks.size() == 1) {
            epic.setStatus(StatusOfTasks.DONE);
            epics.put(epic.getId(), epic);
            return;
        }
        epic.setStatus(StatusOfTasks.IN_PROGRESS);
        epics.put(epic.getId(), epic);
    }
}
