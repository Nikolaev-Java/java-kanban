package service;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> historyTask = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyTask.size() == 10) {
            historyTask.removeFirst();
        }
        historyTask.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
