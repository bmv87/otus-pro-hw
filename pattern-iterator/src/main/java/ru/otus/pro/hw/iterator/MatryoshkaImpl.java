package ru.otus.pro.hw.iterator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MatryoshkaImpl implements Matryoshka {
    private final List<String> items;
    @Getter
    private final String color;
    private final int size;

    public MatryoshkaImpl(int size, String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("Цвет матрешки не задан");
        }
        this.items = new ArrayList<>(size);
        this.color = color;
        this.size = size;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    public void add(String name) {
        if (name == null || name.isBlank() || !name.startsWith(color)) {
            throw new IllegalArgumentException("Такую матрешку добавить нельзя");
        }
        if (items.size() == size) {
            throw new IndexOutOfBoundsException("Матрешка не влезет");
        }
        items.add(name);
    }

    public void addRange(List<String> names) {
        for (String name : names) {
            add(name);
        }
    }

    @Override
    public String get(int index) {
        return items.get(index);
    }

}
