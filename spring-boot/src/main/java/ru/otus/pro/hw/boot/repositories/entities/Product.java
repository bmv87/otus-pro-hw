package ru.otus.pro.hw.boot.repositories.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
	private long id;
	private String name;
	private int price;
}