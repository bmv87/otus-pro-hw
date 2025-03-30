package ru.otus.pro.hw.di;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public interface Cart {
    void add(int id);

    void delete(int id);

    List<String> view();
}
