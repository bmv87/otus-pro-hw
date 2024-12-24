package ru.otus.pro.hw.webServer.routing;

import ru.otus.pro.hw.webServer.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoutePath {
    HttpMethod method();
    String path();

}
