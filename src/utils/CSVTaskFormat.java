package utils;

import model.Epic;
import model.StatusOfTasks;
import model.SubTask;
import model.Task;
import service.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {
    private CSVTaskFormat() {
    }

    public static String TaskToSting(Task task) {
        if (task instanceof Epic) {
            return task.getId() +
                    ",EPIC," + task.getName() +
                    "," + task.getStatus() +
                    "," + task.getDetails();
        }
        if (task instanceof SubTask) {
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
        ArrayList<String> idHistory = new ArrayList<>();
        for (Task task : taskList) {
            idHistory.add(String.valueOf(task.getId()));
        }
        return String.join(",", idHistory.toArray(new String[0]));
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        String[] valueArr = value.split(",");
        for (String s : valueArr) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
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
