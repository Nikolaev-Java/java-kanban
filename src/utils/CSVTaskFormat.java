package utils;

import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import model.TaskTypes;
import service.HistoryManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class
CSVTaskFormat {
    private CSVTaskFormat() {
    }

    public static String TaskToSting(Task task) {
        TaskTypes typeTask = task.getType();
        String result = task.getId() +
                "," + task.getType() +
                "," + task.getName() +
                "," + task.getStatus() +
                "," + task.getDetails() +
                "," + task.getStartTime() +
                "," + task.getDuration() +
                "," + task.getEndTime();

        if (typeTask.equals(TaskTypes.SUBTASK)) {
            result += "," + ((SubTask) task).getEpicId();
        }
        return result;
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
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (!valueArr[5].equals("null")) startTime = LocalDateTime.parse(valueArr[5]);
        if (!valueArr[7].equals("null")) endTime = LocalDateTime.parse(valueArr[7]);
        switch (valueArr[1]) {
            case "TASK": {
                return new Task(valueArr[2], valueArr[4], Integer.parseInt(valueArr[0]),
                        StatusOfTasks.valueOf(valueArr[3]), Long.parseLong(valueArr[6]), startTime);
            }
            case "EPIC": {
                return new Epic(valueArr[2], valueArr[4], Integer.parseInt(valueArr[0]),
                        StatusOfTasks.valueOf(valueArr[3]), Long.parseLong(valueArr[6]), startTime, endTime);
            }
            case "SUBTASK": {
                return new SubTask(valueArr[2], valueArr[4], Integer.parseInt(valueArr[0]),
                        StatusOfTasks.valueOf(valueArr[3]), Integer.parseInt(valueArr[8]),
                        Long.parseLong(valueArr[6]), startTime);
            }
            default: {
                return null;
            }
        }
    }
}
