package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String details, int id, StatusOfTasks status, int epicId) {
        super(name, details, id, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String details, StatusOfTasks status, int epicId) {
        super(name, details, status);
        this.epicId = epicId;
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
                ", epicId=" + this.getEpicId() +
                '}';
    }
}
