package ru.otus.pro.reflection;

import ru.otus.pro.reflection.processors.Collector;
import ru.otus.pro.reflection.processors.TestProcessor;
import ru.otus.pro.reflection.processors.TestsCollector;

public class Application {
    public static void main(String[] args) {
        try {
            Collector testsCollector = new TestsCollector("ru.otus.pro.reflection.tests");
            var testProcessor = new TestProcessor(testsCollector);
            testProcessor.process();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
