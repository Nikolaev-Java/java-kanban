package service;

import exception.ManagerCreateException;
import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import model.TaskTypes;
import utils.Helpers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime, Comparator.nullsLast(LocalDateTime::compareTo)).thenComparingInt(Task::getId);
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    private final HashMap<Integer, Boolean> intervalGrid15Min = new HashMap<>();
    private int generateId = 0;

    public InMemoryTaskManager() {
        initIntervalGrid15Min();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

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
            deleteSubTaskInEpic(id);
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
            throw new ManagerCreateException("Задача пустая");
        }
        if (isCrossingTimeTask(task)) {
            throw new ManagerCreateException("Невозможно создать задачу, из-за пересечения времени");
        }
        int id = ++generateId;
        task.setId(id);
        tasks.put(id, task);
        prioritizedTasks.add(task);
        setIntervalGrid(task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        if (epic == null) {
            throw new ManagerCreateException("Задача пустая");
        }
        int id = ++generateId;
        epic.setId(id);
        calculateTimeEpic(epic);
        epics.put(id, epic);
        calculateEpicStatus(id);
        prioritizedTasks.add(epic);
        return id;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new ManagerCreateException("Задача пустая");
        }
        if (isCrossingTimeTask(subTask)) {
            throw new ManagerCreateException("Невозможно создать задачу, из-за пересечения времени");
        }
        if (!epics.containsKey(subTask.getEpicId())) {
            throw new ManagerCreateException("Нет эпика с данным ID");
        }
        int id = ++generateId;
        subTask.setId(id);
        subTasks.put(id, subTask);
        Epic epic = epics.get(subTask.getEpicId());
        prioritizedTasks.remove(epic);
        epic.addSubTask(id);
        calculateTimeEpic(epic);
        calculateEpicStatus(epic.getId());
        prioritizedTasks.add(subTask);
        prioritizedTasks.add(epic);
        setIntervalGrid(subTask);
        return id;
    }

    @Override
    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (!tasks.get(task.getId()).getStartTime().equals(task.getStartTime())) {
                tearDownInterval(tasks.get(task.getId()));
                if (isCrossingTimeTask(task)) {
                    setIntervalGrid(tasks.get(task.getId()));
                    return false;
                }
            }
            prioritizedTasks.remove(tasks.get(task.getId()));
            tearDownInterval(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            setIntervalGrid(task);
            prioritizedTasks.add(task);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            prioritizedTasks.remove(epics.get(epic.getId()));
            epics.put(epic.getId(), epic);
            calculateEpicStatus(epic.getId());
            calculateTimeEpic(epic);
            prioritizedTasks.add(epic);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            if (!subTasks.get(subTask.getId()).getStartTime().equals(subTask.getStartTime())) {
                tearDownInterval(subTasks.get(subTask.getId()));
                if (isCrossingTimeTask(subTask)) {
                    setIntervalGrid(subTasks.get(subTask.getId()));
                    return false;
                }
            }
            prioritizedTasks.remove(subTasks.get(subTask.getId()));
            subTasks.put(subTask.getId(), subTask);
            calculateEpicStatus(subTask.getEpicId());
            calculateTimeEpic(epics.get(subTask.getEpicId()));
            setIntervalGrid(subTask);
            prioritizedTasks.add(subTask);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tearDownInterval(tasks.get(id));
            prioritizedTasks.remove(tasks.get(id));
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
        prioritizedTasks.remove(epic);
        for (Integer i : epic.getSubTaskId()) {
            historyManager.remove(i);
            prioritizedTasks.remove(subTasks.get(i));
            tearDownInterval(subTasks.get(i));
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
        deleteSubTaskInEpic(id);
        historyManager.remove(id);
        tearDownInterval(subTasks.get(id));
        prioritizedTasks.remove(subTasks.get(id));
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

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public HashMap<Integer, Boolean> getIntervalGrid15Min() {
        return intervalGrid15Min;
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

    protected void addTask(Task task, int id) {
        generateId = id;
        TaskTypes type = task.getType();
        setIntervalGrid(task);
        prioritizedTasks.add(task);
        if (type.equals(TaskTypes.EPIC)) {
            epics.put(task.getId(), (Epic) task);
        } else if (type.equals(TaskTypes.SUBTASK)) {
            subTasks.put(task.getId(), (SubTask) task);
        } else {
            tasks.put(task.getId(), task);
        }
    }

    private void deleteSubTaskInEpic(int idSubTask) {
        Epic epic = epics.get(subTasks.get(idSubTask).getEpicId());
        epic.deleteSubTaskId(idSubTask);
        updateEpic(epic);
    }

    private void calculateTimeEpic(Epic epic) {
        List<SubTask> subTaskList = getSubTaskInEpic(epic.getId());
        epic.setEndTime(subTaskList.stream()
                .filter(subTask -> !subTask.getStatus().equals(StatusOfTasks.DONE))
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null));
        epic.setStartTime(subTaskList.stream()
                .filter(subTask -> !subTask.getStatus().equals(StatusOfTasks.DONE))
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null));
        epic.setDuration(subTaskList.stream()
                .filter(subTask -> !subTask.getStatus().equals(StatusOfTasks.DONE))
                .map(SubTask::getDuration)
                .reduce(0L, Long::sum));
    }

    private void initIntervalGrid15Min() {
        final int countDayInYear = Helpers.isLeapYear(LocalDateTime.now().getYear());
        final int minutesInDay = 24 * 60;
        final int countInterval15MinInYear = countDayInYear * (minutesInDay / Helpers.MINUTES_IN_INTERVAL);
        for (int i = 0; i < countInterval15MinInYear; i++) {
            intervalGrid15Min.put(i, false);
        }
    }

    private int calculateEndInterval(Task task) {
        LocalDateTime data = task.getEndTime();
        int interval = Helpers.calculateIntervalFromDate(data);
        if (interval == 0) return intervalGrid15Min.size() - 1;
        if (data.getMinute() % 15 == 0) {
            return interval - 1;
        } else {
            return interval;
        }
    }


    private void setIntervalGrid(Task task) {
        if (task.getStartTime() == null) return;
        int intervalStart = Helpers.calculateIntervalFromDate(task.getStartTime());
        if (task.getEndTime() != null && !task.getType().equals(TaskTypes.EPIC)) {
            int intervalEnd = calculateEndInterval(task);
            while (intervalStart <= intervalEnd) {
                if (task.getStatus() == StatusOfTasks.DONE) {
                    intervalGrid15Min.put(intervalStart, false);
                } else {
                    intervalGrid15Min.put(intervalStart, true);
                }
                intervalStart++;
            }
        }
    }

    private boolean isCrossingTimeTask(Task task) {
        if (task.getStartTime() == null) return false;
        int intervalStart = Helpers.calculateIntervalFromDate(task.getStartTime());
        int intervalEnd = calculateEndInterval(task);
        boolean isCross = false;
        while (intervalStart <= intervalEnd) {
            isCross = intervalGrid15Min.get(intervalStart);
            if (isCross) return isCross;
            intervalStart++;
        }
        return isCross;
    }

    private void tearDownInterval(Task task) {
        if (task.getStartTime() == null) return;
        int intervalStart = Helpers.calculateIntervalFromDate(task.getStartTime());
        int intervalEnd = calculateEndInterval(task);
        while (intervalStart <= intervalEnd) {
            intervalGrid15Min.put(intervalStart, false);
            intervalStart++;
        }
    }
}
