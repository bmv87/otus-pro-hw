package ru.otus.pro.hw.streamApi.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Task {
    private Long id;
    private String name;
    private StatusEnum status;


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status.getDescription() +
                '}';
    }
}
