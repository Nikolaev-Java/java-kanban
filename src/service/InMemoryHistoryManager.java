package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Node next;
        Node prev;
        Task data;

        private Node(Node prev, Task data, Node next) {
            this.next = next;
            this.prev = prev;
            this.data = data;
        }
    }

    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head = null;
    private Node tail = null;

    @Override
    public void add(Task task) {
        if (task == null) return;
        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
            historyMap.remove(task.getId());
        }
        historyMap.put(task.getId(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.remove(id));
    }

    @Override

    public List<Task> getHistory() {
        return getTasks();
    }

    private Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        if (head == null) return taskList;
        taskList.add(head.data);
        Node node = head;
        while (node.next != null) {
            taskList.add(node.next.data);
            node = node.next;
        }
        return taskList;
    }

    /*
     * Если узел еденственный в списке. Голова и хвост устанавливается в NULL. Разве нет?
     * */
    private void removeNode(Node node) {
        if(node == null) return;
        Node prev = node.prev;
        Node next = node.next;
        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }
        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }
    }
}

