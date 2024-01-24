package server;

public class EndPointConst {
    public static String PRIORITIZED_END_POINT = "http://localhost:8080/tasks/";
    public static String TASK_END_POINT = "http://localhost:8080/tasks/task";
    public static String TASK_ID_END_POINT = "http://localhost:8080/tasks/task?id=";
    public static String EPIC_END_POINT = "http://localhost:8080/tasks/epic";
    public static String EPIC_ID_END_POINT = "http://localhost:8080/tasks/epic?id=";
    public static String SUBTASK_END_POINT = "http://localhost:8080/tasks/subtask";
    public static String SUBTASK_ID_END_POINT = "http://localhost:8080/tasks/subtask?id=";
    public static String HISTORY_END_POINT = "http://localhost:8080/tasks/history";
    public static String SUBTASK_IN_EPIC_END_POINT = "http://localhost:8080/tasks/subtask/epic?id=";

    private EndPointConst() {
    }
}
