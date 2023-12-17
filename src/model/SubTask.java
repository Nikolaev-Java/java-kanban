package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String details, int id, StatusOfTasks status, int epicId) {
        super(name, details, id, status);
        this.epicId = epicId;
        setType(TaskTypes.SubTask);
    }

    public SubTask(String name, String details, StatusOfTasks status, int epicId) {
        super(name, details, status);
        this.epicId = epicId;
        setType(TaskTypes.SubTask);
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
