package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String details, int id) {
        super(name, details, id, StatusOfTasks.NEW);
        setType(TaskTypes.EPIC);
    }

    public Epic(String name, String details, int id, LocalDateTime startTime) {
        super(name, details, id, StatusOfTasks.NEW, 0, startTime);
        setType(TaskTypes.EPIC);
    }

    public Epic(String name, String details, int id, StatusOfTasks status, LocalDateTime startTime) {
        super(name, details, id, status, 0, startTime);
        setType(TaskTypes.EPIC);
    }

    public Epic(String name, String details, int id, StatusOfTasks status, long duration,
                LocalDateTime startTime, LocalDateTime endTime) {
        super(name, details, id, status, duration, startTime);
        setType(TaskTypes.EPIC);
        this.endTime = endTime;
    }

    public Epic(String name, String details, LocalDateTime startTime) {
        super(name, details, StatusOfTasks.NEW, 0, startTime);
        setType(TaskTypes.EPIC);
    }

    public Epic(String name, String details) {
        super(name, details, StatusOfTasks.NEW, 0, null);
        setType(TaskTypes.EPIC);
    }

    public void addSubTask(int subTaskId) {
        this.subTaskId.add(subTaskId);
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void deleteSubTaskId(int id) {
        subTaskId.remove((Integer) id);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + this.getName() + '\'' +
                ", details='" + this.getDetails() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", subTasks=" + subTaskId +
                ", duration=" + this.getDuration() +
                ", startTime=" + this.getStartTime() +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        if (!subTaskId.equals(epic.subTaskId)) return false;
        return Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result += 31 * result + subTaskId.hashCode();
        return result;
    }
}
