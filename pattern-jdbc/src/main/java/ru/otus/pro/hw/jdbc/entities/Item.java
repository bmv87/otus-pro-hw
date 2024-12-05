package ru.otus.pro.hw.jdbc.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private int id;
    private String title;
    private BigDecimal price;

    public Item(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }
}
