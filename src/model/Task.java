package model;

import java.util.Objects;

public class Task {
    private String name;
    private String details;
    private int id;
    private StatusOfTasks status;

    public Task(String name, String details, int id, StatusOfTasks status) {
        this.name = name;
        this.details = details;
        this.id = id;
        this.status = status;
    }

    public Task(String name, String details, StatusOfTasks status) {
        this.name = name;
        this.details = details;
        this.status = status;
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

    @Override
    public String toString() {
        return id +
                ",TASK," + name +
                "," + status +
                "," + details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!name.equals(task.name)) return false;
        if (!details.equals(task.details)) return false;
        return status == task.status;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result += 31 * result + details.hashCode();
        result += 31 * result + id;
        result += 31 * result + status.hashCode();
        return result;
    }
}
