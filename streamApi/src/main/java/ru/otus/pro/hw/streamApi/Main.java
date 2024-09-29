package ru.otus.pro.hw.streamApi;

import ru.otus.pro.hw.streamApi.tasks.StatusEnum;
import ru.otus.pro.hw.streamApi.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
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

        doTask1(tasksList);
        doTask2(tasksList);
        doTask3(tasksList);
        doTask4(tasksList);
        doTask5(tasksList);
        doTask6(tasksList);
    }

    private static void doTask1(List<Task> sourceTasks) {
        System.out.println("-------------  Task1 (getTasksByStatus)  ------------");
        System.out.println("Список задач со статусом 'В работе': ");
        getTasksByStatus(sourceTasks, StatusEnum.IN_PROCESS).forEach(t -> System.out.println(t.toString()));
        System.out.println("-----------------------------------------------------");
    }

    private static void doTask2(List<Task> sourceTasks) {
        System.out.println("-------------  Task2 (countTasksByStatus)  ------------");
        System.out.println("Количество задач со статусом 'Закрыта': " + countTasksByStatus(sourceTasks, StatusEnum.CLOSED));
        System.out.println("-----------------------------------------------------");
    }

    private static void doTask3(List<Task> sourceTasks) {
        System.out.println("-------------  Task3 (isTaskExists isTaskNotExists)  ------------");
        System.out.println("Наличие задачи с ID = 2: " + isTaskExists(sourceTasks, 2L));
        System.out.println("Отсутствие задачи с ID = 99: " + isTaskNotExists(sourceTasks, 99L));
        System.out.println("-----------------------------------------------------");
    }

    private static void doTask4(List<Task> sourceTasks) {
        System.out.println("-------------  Task4 (getSortedTasksByStatus)  ------------");
        getSortedTasksByStatus(sourceTasks).forEach(t -> System.out.println(t.toString()));
        System.out.println("-----------------------------------------------------");
    }

    private static void doTask5(List<Task> sourceTasks) {
        System.out.println("-------------  Task5 (groupTasksByStatusAndParity)  ------------");
        groupTasksByStatusAndParity(sourceTasks).forEach((key, group) -> {
            System.out.println("Группа: " + key.getDescription());
            group.forEach((key1, value) -> {
                System.out.println("  Группа: " + key1);
                value.forEach(t -> System.out.println("     Задача: " + t.toString()));
            });
        });
        System.out.println("-----------------------------------------------------");
    }

    private static void doTask6(List<Task> sourceTasks) {
        System.out.println("-------------  Task6 (groupTasksByStatus)  ------------");
        groupTasksByStatus(sourceTasks).forEach((key, value) -> {
            System.out.println("Группа: " + key);
            value.forEach(t -> System.out.println("     Задача: " + t.toString()));
        });
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
        return sourceTasks.stream().sorted().toList();
    }

    private static Map<StatusEnum, Map<String, List<Task>>> groupTasksByStatusAndParity(List<Task> sourceTasks) {
        return sourceTasks.stream().sorted().collect(Collectors.groupingBy(
                Task::getStatus,
                Collectors.groupingBy(t -> t.getId() % 2 == 0 ? "Четные" : "Нечетные")));
    }

    private static Map<String, List<Task>> groupTasksByStatus(List<Task> sourceTasks) {
        return sourceTasks.stream().sorted().collect(Collectors.groupingBy(
                t -> t.getStatus() == StatusEnum.CLOSED ? StatusEnum.CLOSED.getDescription() : "Остальные",
                Collectors.toList()));
    }
}
