package utils;

import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import model.TaskTypes;
import service.HistoryManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVTaskFormat {
    private CSVTaskFormat() {
    }

    public static String TaskToSting(Task task) {
        TaskTypes typeTask = task.getType();
        if (typeTask.equals(TaskTypes.Epic)) {
            return task.getId() +
                    ",EPIC," + task.getName() +
                    "," + task.getStatus() +
                    "," + task.getDetails();
        }
        if (typeTask.equals(TaskTypes.SubTask)) {
            return task.getId() +
                    ",SUBTASK," + task.getName() +
                    "," + task.getStatus() +
                    "," + task.getDetails() +
                    "," + ((SubTask) task).getEpicId();
        }
        return task.getId() +
                ",TASK," + task.getName() +
                "," + task.getStatus() +
                "," + task.getDetails();
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> taskList = manager.getHistory();
        return String.join(",", taskList.stream()
                .map(task -> String.valueOf(task.getId()))
                .toArray(String[]::new));
    }

    public static List<Integer> historyFromString(String value) {
        String[] valueArr = value.split(",");
        return Arrays.stream(valueArr)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static Task fromString(String value) {
        String[] valueArr = value.split(",");
        switch (valueArr[1]) {
            case "TASK": {
                return new Task(valueArr[2], valueArr[4], Integer.parseInt(valueArr[0]), StatusOfTasks.valueOf(valueArr[3]));
            }
            case "EPIC": {
                return new Epic(valueArr[2], valueArr[4], Integer.parseInt(valueArr[0]), StatusOfTasks.valueOf(valueArr[3]));
            }
            case "SUBTASK": {
                return new SubTask(valueArr[2], valueArr[4], Integer.parseInt(valueArr[0]), StatusOfTasks.valueOf(valueArr[3]), Integer.parseInt(valueArr[5]));
            }
            default: {
                return null;
            }
        }
    }
}
