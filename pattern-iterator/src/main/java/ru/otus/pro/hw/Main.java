package ru.otus.pro.hw;

import ru.otus.pro.hw.iterator.Box;
import ru.otus.pro.hw.iterator.Matryoshka;
import ru.otus.pro.hw.iterator.MatryoshkaImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Matryoshka> matryoshki = new ArrayList<>();
        var redMatryoshka = new MatryoshkaImpl(9, "red");
        redMatryoshka.addRange(List.of("red0", "red1", "red2", "red3", "red4", "red5", "red6", "red7", "red8"));
        matryoshki.add(redMatryoshka);

        var greenMatryoshka = new MatryoshkaImpl(10, "green");
        greenMatryoshka.addRange(List.of("green0", "green1", "green2", "green3", "green4", "green5", "green6", "green7", "green8", "green9"));
        matryoshki.add(greenMatryoshka);

        var blueMatryoshka = new MatryoshkaImpl(7, "blue");
        blueMatryoshka.addRange(List.of("blue0", "blue1", "blue2", "blue3", "blue4", "blue5", "blue6"));
        matryoshki.add(blueMatryoshka);

        var magentaMatryoshka = new MatryoshkaImpl(10, "magenta");
        magentaMatryoshka.addRange(List.of("magenta0", "magenta1", "magenta2", "magenta3", "magenta4"));
        matryoshki.add(magentaMatryoshka);

        var box = new Box<Matryoshka>();
        box.addRange(matryoshki);
        box.getSmallFirstIterator().forEachRemaining(System.out::println);
        box.getColorFirstIterator().forEachRemaining(System.out::println);
    }
}
