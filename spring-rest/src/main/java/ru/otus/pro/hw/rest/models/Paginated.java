package ru.otus.pro.hw.rest.models;

import java.util.List;

public record Paginated<T>(long totalCount, List<T> items) {
}
