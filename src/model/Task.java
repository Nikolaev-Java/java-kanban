package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String details;
    private int id;
    private StatusOfTasks status;
    private TaskTypes type;
    private long duration;
    private LocalDateTime startTime;

    public Task(String name, String details, int id, StatusOfTasks status) {
        this.name = name;
        this.details = details;
        this.id = id;
        this.status = status;
        this.type = TaskTypes.TASK;
    }

    public Task(String name, String details, int id, StatusOfTasks status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.details = details;
        this.id = id;
        this.status = status;
        this.type = TaskTypes.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String details, StatusOfTasks status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.details = details;
        this.status = status;
        this.type = TaskTypes.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String details, StatusOfTasks status, long duration) {
        this.name = name;
        this.details = details;
        this.status = status;
        this.type = TaskTypes.TASK;
        this.duration = duration;
        this.startTime = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusOfTasks getStatus() {
        return status;
    }

    public void setStatus(StatusOfTasks status) {
        this.status = status;
    }

    public TaskTypes getType() {
        return type;
    }

    public void setType(TaskTypes type) {
        this.type = type;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) return null;
        return startTime.plus(Duration.ofMinutes(duration));
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!name.equals(task.name)) return false;
        if (!details.equals(task.details)) return false;
        if (status != task.status) return false;
        if (duration != task.duration) return false;
        if (!Objects.equals(startTime, task.startTime)) return false;
        return type == task.type;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result += 31 * result + details.hashCode();
        result += 31 * result + id;
        result += 31 * result + status.hashCode();
        result += 31 * result + type.hashCode();
        result += 31 * result + (int) duration;
        result += 31 * result + startTime.hashCode();
        return result;
    }
}
