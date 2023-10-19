package model;

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
        return "Task{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
