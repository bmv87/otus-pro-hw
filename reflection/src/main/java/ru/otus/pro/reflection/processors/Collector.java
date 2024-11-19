package ru.otus.pro.reflection.processors;

import java.util.List;
import java.util.Map;

public interface Collector {
     Map<Class<?>, List<Wrapper>> collect();
}
