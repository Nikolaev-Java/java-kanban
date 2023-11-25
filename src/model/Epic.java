package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskId = new ArrayList<>();

    public Epic(String name, String details, int id) {
        super(name, details, id, StatusOfTasks.NEW);
    }

    public Epic(String name, String details) {
        super(name, details, StatusOfTasks.NEW);
    }

    public void addSubTask(int subTaskId) {
        this.subTaskId.add(subTaskId);
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void deleteSubTaskId(int id) {
        subTaskId.remove(subTaskId.indexOf(id));
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + this.getName() + '\'' +
                ", details='" + this.getDetails() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", subTasks=" + subTaskId.toString() +
                '}';
    }
}
