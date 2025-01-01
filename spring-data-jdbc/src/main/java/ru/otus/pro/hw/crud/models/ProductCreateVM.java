package ru.otus.pro.hw.crud.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateVM {
    @NotEmpty(message = "Title is required")
    private String title;

    @NotNull(message = "Price is required")
    private Integer price;

    @Override
    public String toString() {
        return "Product" +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
