package model;

import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String details, int id, StatusOfTasks status, int epicId,
                   long duration, LocalDateTime startTime) {
        super(name, details, id, status, duration, startTime);
        this.epicId = epicId;
        setType(TaskTypes.SUBTASK);
    }

    public SubTask(String name, String details, StatusOfTasks status, int epicId,
                   long duration, LocalDateTime startTime) {
        super(name, details, status, duration, startTime);
        this.epicId = epicId;
        setType(TaskTypes.SUBTASK);
    }

    public SubTask(String name, String details, StatusOfTasks status, int epicId,
                   long duration) {
        super(name, details, status, duration, null);
        this.epicId = epicId;
        setType(TaskTypes.SUBTASK);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + this.getName() + '\'' +
                ", details='" + this.getDetails() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", duration=" + this.getDuration() +
                ", startTime=" + this.getStartTime() +
                ", endTime=" + this.getEndTime() +
                ", epicId=" + this.getEpicId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SubTask subTask = (SubTask) o;

        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result += 31 * result + epicId;
        return result;
    }
}
