package ru.otus.pro.hw.iterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Box<T extends Matryoshka> {
    Map<String, Matryoshka> matryoshki = new HashMap<>();

    public void add(T matryoshka) {
        var color = matryoshka.getColor().toLowerCase();
        if (matryoshki.containsKey(color)) {
            throw new IllegalArgumentException("Такая матрешка в коробке уже есть!");
        }
        matryoshki.put(color, matryoshka);
    }

    public void addRange(List<T> list) {
        for (T t : list) {
            add(t);
        }
    }

    public Iterator<String> getSmallFirstIterator() {
        return new SmallFirstIterator();
    }

    // expected: "red0", "red1", ..., "red9", "green0", "green1", ..., "green9", ...
    public Iterator<String> getColorFirstIterator() {
        return new ColorFirstIterator();
    }


    public class SmallFirstIterator implements Iterator<String> {

        private int stepCount = 0;
        private int index;
        private int colorIndex;
        private String[] colors;
        private String color;
        private int totalItemsCount;

        public SmallFirstIterator() {
            totalItemsCount = matryoshki.values().stream().mapToInt(Matryoshka::getSize).sum();
            colors = matryoshki.keySet().toArray(new String[0]);
            colorIndex = 0;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return stepCount < totalItemsCount;
        }

        @Override
        public String next() {
            if (colors.length <= colorIndex) {
                colorIndex = 0;
                index++;
            }
            color = colors[colorIndex];
            var matryoshka = matryoshki.get(color);
            while (matryoshka.getSize() <= index) {
                colorIndex++;
                if (colors.length <= colorIndex) {
                    colorIndex = 0;
                    index++;
                }
                color = colors[colorIndex];
                matryoshka = matryoshki.get(color);
            }
            stepCount++;
            colorIndex++;
            return matryoshka.get(index);
        }
    }

    public class ColorFirstIterator implements Iterator<String> {

        private int stepCount = 0;
        private int index;
        private int colorIndex;
        private String[] colors;
        private String color;
        private int totalItemsCount;

        public ColorFirstIterator() {
            totalItemsCount = matryoshki.values().stream().mapToInt(Matryoshka::getSize).sum();
            colors = matryoshki.keySet().toArray(new String[0]);
            colorIndex = 0;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return stepCount < totalItemsCount;
        }

        @Override
        public String next() {

            color = colors[colorIndex];
            var matryoshka = matryoshki.get(color);
            while (matryoshka.getSize() <= index) {
                index = 0;
                colorIndex++;
                color = colors[colorIndex];
                matryoshka = matryoshki.get(color);
            }
            stepCount++;
            return matryoshka.get(index++);
        }
    }
}
