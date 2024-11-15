package ru.otus.pro.hw.streamApi;

import ru.otus.pro.hw.streamApi.tasks.StatusEnum;
import ru.otus.pro.hw.streamApi.tasks.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private final static String ODD = "Нечетные";
    private final static String EVEN = "Четные";
    private final static String OTHER = "Остальные";
    private static Comparator<Task> comparator = Comparator.comparing(Task::getStatus);

    public static void main(String[] args) {
        var tasksList = new ArrayList<Task>(List.of(
                new Task(1L, "Task1", StatusEnum.OPENED),
                new Task(2L, "Task2", StatusEnum.CLOSED),
                new Task(3L, "Task3", StatusEnum.CLOSED),
                new Task(4L, "Task4", StatusEnum.OPENED),
                new Task(5L, "Task5", StatusEnum.IN_PROCESS),
                new Task(6L, "Task6", StatusEnum.OPENED),
                new Task(7L, "Task7", StatusEnum.IN_PROCESS),
                new Task(8L, "Task8", StatusEnum.OPENED),
                new Task(9L, "Task9", StatusEnum.CLOSED),
                new Task(10L, "Task10", StatusEnum.IN_PROCESS),
                new Task(11L, "Task11", StatusEnum.OPENED),
                new Task(12L, "Task12", StatusEnum.OPENED),
                new Task(13L, "Task13", StatusEnum.CLOSED)
        ));

        getTasksByStatusAndPrint(tasksList);
        countTasksByStatusAndPrint(tasksList);
        checkTaskExistsAndPrint(tasksList);
        getSortedTasksByStatusAndPrint(tasksList);
        groupTasksByStatusAndParityAndPrint(tasksList);
        groupTasksByStatusAndPrint(tasksList);
    }

    private static void getTasksByStatusAndPrint(List<Task> sourceTasks) {
        System.out.println("-------------  Task1 (getTasksByStatus)  ------------");
        System.out.println("Список задач со статусом 'В работе': ");
        getTasksByStatus(sourceTasks, StatusEnum.IN_PROCESS).forEach(System.out::println);
        System.out.println("-----------------------------------------------------");
    }

    private static void countTasksByStatusAndPrint(List<Task> sourceTasks) {
        System.out.println("-------------  Task2 (countTasksByStatus)  ------------");
        System.out.println("Количество задач со статусом 'Закрыта': " + countTasksByStatus(sourceTasks, StatusEnum.CLOSED));
        System.out.println("-----------------------------------------------------");
    }

    private static void checkTaskExistsAndPrint(List<Task> sourceTasks) {
        System.out.println("-------------  Task3 (isTaskExists isTaskNotExists)  ------------");
        System.out.println("Наличие задачи с ID = 2: " + isTaskExists(sourceTasks, 2L));
        System.out.println("Отсутствие задачи с ID = 99: " + isTaskNotExists(sourceTasks, 99L));
        System.out.println("-----------------------------------------------------");
    }

    private static void getSortedTasksByStatusAndPrint(List<Task> sourceTasks) {
        System.out.println("-------------  Task4 (getSortedTasksByStatus)  ------------");
        getSortedTasksByStatus(sourceTasks).forEach(System.out::println);
        System.out.println("-----------------------------------------------------");
    }

    private static void groupTasksByStatusAndParityAndPrint(List<Task> sourceTasks) {
        System.out.println("-------------  Task5 (groupTasksByStatusAndParity)  ------------");
        for (var firstLevelGroup : groupTasksByStatusAndParity(sourceTasks).entrySet()) {
            System.out.println("Группа: " + firstLevelGroup.getKey().getDescription());
            for (var secondLevelGroup : firstLevelGroup.getValue().entrySet()) {
                System.out.println("  Группа: " + secondLevelGroup.getKey());
                for (var task : secondLevelGroup.getValue()) {
                    System.out.println("     Задача: " + task.toString());
                }
            }
        }

        System.out.println("-----------------------------------------------------");
    }

    private static void groupTasksByStatusAndPrint(List<Task> sourceTasks) {
        System.out.println("-------------  Task6 (groupTasksByStatus)  ------------");
        for (var group : groupTasksByStatus(sourceTasks).entrySet()) {
            System.out.println("Группа: " + (group.getKey() ? StatusEnum.CLOSED.getDescription() : OTHER));
            for (var task : group.getValue()) {
                System.out.println("     Задача: " + task.toString());
            }
        }

        System.out.println("-----------------------------------------------------");
    }

    private static List<Task> getTasksByStatus(List<Task> sourceTasks, StatusEnum status) {
        return sourceTasks.stream().filter(t -> t.getStatus() == status).toList();
    }

    private static long countTasksByStatus(List<Task> sourceTasks, StatusEnum status) {
        return sourceTasks.stream().filter(t -> t.getStatus() == status).count();
    }

    private static boolean isTaskExists(List<Task> sourceTasks, Long taskId) {
        return sourceTasks.stream().anyMatch(t -> t.getId().equals(taskId));
    }

    private static boolean isTaskNotExists(List<Task> sourceTasks, Long taskId) {
        return sourceTasks.stream().noneMatch(t -> t.getId().equals(taskId));
    }

    private static List<Task> getSortedTasksByStatus(List<Task> sourceTasks) {
        return sourceTasks.stream().sorted(comparator).toList();
    }

    private static Map<StatusEnum, Map<String, List<Task>>> groupTasksByStatusAndParity(List<Task> sourceTasks) {
        return sourceTasks.stream().collect(Collectors.groupingBy(
                Task::getStatus,
                Collectors.groupingBy(t -> t.getId() % 2 == 0 ? EVEN : ODD)));
    }

    private static Map<Boolean, List<Task>> groupTasksByStatus(List<Task> sourceTasks) {
        return sourceTasks.stream().sorted(comparator).collect(Collectors.partitioningBy(
                t -> t.getStatus() == StatusEnum.CLOSED));
    }
}
