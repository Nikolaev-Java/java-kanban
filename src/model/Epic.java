package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskId = new ArrayList<>();

    public Epic(String name, String details, int id) {
        super(name, details, id, StatusOfTasks.NEW);
    }

    public Epic(String name, String details, int id, StatusOfTasks status) {
        super(name, details, id, status);
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
        return getId() +
                ",EPIC," + getName() +
                "," + getStatus() +
                "," + getDetails();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        return subTaskId.equals(epic.subTaskId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result += 31 * result + subTaskId.hashCode();
        return result;
    }
}
